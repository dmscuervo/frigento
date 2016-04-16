package com.soutech.frigento.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.dto.ItemDTO;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.exception.ReporteException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.service.EstadoService;
import com.soutech.frigento.service.PedidoService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelPedidoProductoService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.SendMailSSL;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.reports.ReportManager;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;

@Controller
@RequestMapping(value="/pedido")
@SessionAttributes(names="estadoList")
@Secured({"ROLE_ADMIN"})
public class PedidoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "pedido?estados="+Constantes.ESTADO_PEDIDO_PENDIENTE+","+Constantes.ESTADO_PEDIDO_CONFIRMADO+"&sortFieldName=id&sortOrder=asc";
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new FormatoDateTruncateValidator(new SimpleDateFormat("dd/MM/yyyy HH:mm"), true));   
    }
    
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private RelPedidoProductoService relPedidoProductoService;
    @Autowired
    private EstadoService estadoService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ReportManager reportManager;
    @Autowired
    private SendMailSSL sndMailSSL;
    
    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	List<Producto> productos = productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion", "asc");
    	List<Estado> estados = estadoService.obtenerEstadosPedido();
    	Pedido pedido = new Pedido();
    	pedido.setFecha(new Date());
    	pedido.setItems(new ArrayList<ItemDTO>(productos.size()));
    	pedido.setEstado(estados.get(0));
    	//Lo inicializo para que no falle la validación
    	pedido.setCosto(BigDecimal.ZERO);
    	for (Producto producto : productos) {
    		ItemDTO item = new ItemDTO();
    		item.setCantidad((short)0);
    		item.setProducto(producto);
    		pedido.getItems().add(item);
		}
		uiModel.addAttribute("pedidoForm", pedido);
		uiModel.addAttribute("estadoList", estados);
        return "pedido/alta";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	if (bindingResult.hasErrors()) {
        	return "pedido/alta";
        }

    	String mensaje;
    	boolean ok = false;
		try {
			ok = pedidoService.generarPedido(pedidoForm);
		} catch (ProductoSinCostoException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "pedido/alta";
		}
    	if(ok){
    		mensaje = getMessage("pedido.confirmar.ok");
    	}else{
    		mensaje = getMessage("pedido.confirmar.sin.items");
    		httpServletRequest.setAttribute("msgError", mensaje);
    		return "pedido/alta";
    	}
    	
		uiModel.asMap().clear();

        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "estados", required = true) Short estadoPedido[], @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, HttpServletRequest httpServletRequest) {
    	httpServletRequest.setAttribute("pedidos", pedidoService.obtenerPedidos(estadoPedido, sortFieldName, sortOrder));
    	httpServletRequest.setAttribute("estadoSel", estadoPedido);
        if(informar != null){
        	httpServletRequest.setAttribute("informar", informar);
        }
        return "pedido/grilla";
    }
    
    @RequestMapping(params = "detalle", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String detalle(@PathVariable("id") Integer idPedido, Model uiModel) {
        uiModel.addAttribute("relPedProdList", relPedidoProductoService.obtenerByPedido(idPedido));
        return "pedido/detalle";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer idPed, Model uiModel, HttpServletRequest httpServletRequest) {
    	List<Producto> productos = productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion", "asc");
    	List<Estado> estados = estadoService.obtenerEstadosPedido();
    	List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(idPed);
    	Pedido pedido = relPedProdList.get(0).getPedido();
    	if(pedido.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)){
        	httpServletRequest.setAttribute("informar", getMessage("pedido.editar.estado.error", new Object[]{estados.get(0).getDescripcion().concat(" o ").concat(estados.get(1).getDescripcion())}));
        	return "pedido/grilla";
        }
    	
    	pedido.setItems(new ArrayList<ItemDTO>(productos.size()));
    	for (Producto producto : productos) {
    		ItemDTO item = new ItemDTO();
    		for (RelPedidoProducto rpp : relPedProdList) {
    			if(rpp.getProductoCosto().getProducto().getId().equals(producto.getId())){
    				item.setCantidad(rpp.getCantidad().shortValue());
    				break;
    			}else{
    				item.setCantidad((short)0);
    			}
			}
    		item.setProducto(producto);
    		pedido.getItems().add(item);
		}
    	
    	List<Estado> estadosPosibles = new ArrayList<Estado>();
    	for (Estado estado : estados) {
			if(estado.getId() <= new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)){
				estadosPosibles.add(estado);
			}
		}
    		
		uiModel.addAttribute("pedidoForm", pedido);
		uiModel.addAttribute("estadoList", estadosPosibles);
        return "pedido/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	if (bindingResult.hasErrors()) {
        	return "pedido/editar";
        }

    	String mensaje;
    	boolean ok = false;
		try {
			ok = pedidoService.actualizarPedido(pedidoForm);
		} catch (ProductoSinCostoException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "pedido/editar";
		}
    	if(ok){
    		mensaje = getMessage("pedido.editar.ok");
    	}else{
    		mensaje = getMessage("pedido.confirmar.sin.items");
    		httpServletRequest.setAttribute("msgError", mensaje);
    		return "pedido/editar";
    	}
    	
    	if(pedidoForm.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) && pedidoForm.getEnvioMail()){
    		List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(pedidoForm.getId());
    		ByteArrayOutputStream bytes = reportManager.generarRemito(relPedProdList);
			Pedido pedido = relPedProdList.get(0).getPedido();
			String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
			
			sndMailSSL.enviarCorreoPedido(pedido, bytes, fileDownload);
			try {
				bytes.flush();
				bytes.close();
			} catch (IOException e) {}
    	}
    	
		uiModel.asMap().clear();

        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
    
    @RequestMapping(params = "descargar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public void descargar(@PathVariable("id") Integer idPed, HttpServletRequest httpServletRequest, HttpServletResponse response) {
    	List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(idPed);
    	
		ByteArrayOutputStream bytes = reportManager.generarRemito(relPedProdList);
		Pedido pedido = relPedProdList.get(0).getPedido();
		String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);

		response.setHeader("Content-Disposition", "attachment;filename=" + fileDownload + ".pdf");
		response.setContentType( "application/pdf" );
        response.setContentLength((int) bytes.size());

        OutputStream outStream;
		try {
			outStream = response.getOutputStream();
			bytes.writeTo(outStream);
			outStream.flush();
			outStream.close();
			bytes.close();
		} catch (IOException e) {
			throw new ReporteException("pedido.generar.reporte.error");
		}
        
    }
//    
//    @RequestMapping(produces = "text/html")
//    public String listar(@RequestParam(value = "estado", required = true) String estado, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, HttpServletRequest httpServletRequest) {
//    	httpServletRequest.setAttribute("pedidos", pedidoService.obtenerPedidos(estado, sortFieldName, sortOrder));
//    	httpServletRequest.setAttribute("estadoSel", estado);
//        if(informar != null){
//        	httpServletRequest.setAttribute("informar", informar);
//        }
//        return "pedido/grilla";
//    }
//    
//    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
//    public String preEdit(@PathVariable("id") Integer id, Model uiModel) {
//    	Date fechaHastaMin = relPedidoCategoriaService.obtenerMinFechaDesde(id);
//    	Date fechaHastaMin2 = pedidoCostoService.obtenerMinFechaHasta(id);
//    	Date fechaHastaMin3 = relPedidoPedidoService.obtenerMinFechaPedido(id);
//    	//Me quedo con la mas vieja
//    	Date fechaMinD = new Date();
//    	//Inicializo valores en caso de nulos
//    	if(fechaHastaMin != null){
//    		fechaHastaMin2 = fechaHastaMin2 == null ? fechaHastaMin : fechaHastaMin2;
//    		fechaHastaMin3 = fechaHastaMin3 == null ? fechaHastaMin : fechaHastaMin3;
//    	}else if(fechaHastaMin2 != null){
//    		fechaHastaMin = fechaHastaMin == null ? fechaHastaMin2 : fechaHastaMin;
//    		fechaHastaMin3 = fechaHastaMin3 == null ? fechaHastaMin2 : fechaHastaMin3;
//    	}else if(fechaHastaMin3 != null){
//    		fechaHastaMin = fechaHastaMin == null ? fechaHastaMin3 : fechaHastaMin;
//    		fechaHastaMin2 = fechaHastaMin2 == null ? fechaHastaMin3 : fechaHastaMin2;
//    	}
//    	if(fechaHastaMin != null){
//    		fechaMinD = fechaHastaMin.before(fechaHastaMin2) ? fechaHastaMin : fechaHastaMin2;
//    		fechaMinD = fechaMinD.before(fechaHastaMin3) ? fechaMinD : fechaHastaMin3;
//    	}
//    	
//    	uiModel.addAttribute("maxDateAlta", fechaMinD.getTime());
//    	Date fechaDesdeMin = pedidoCostoService.obtenerMinFechaDesde(id);
//    	Pedido prod = pedidoService.obtenerPedido(id);
//    	prod.setFechaAlta(fechaDesdeMin);
//    	prod.setStockPrevio(prod.getStock());
//    	uiModel.addAttribute("pedidoForm", prod);
//        return "pedido/editar";
//    }
//    
//    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
//    public String edit(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//    	formValidator.validate(pedidoForm, bindingResult);
//        if (bindingResult.hasErrors()) {
//        	return "pedido/editar";
//        }
//        uiModel.asMap().clear();
//        try {
//			pedidoService.actualizarPedido(pedidoForm);
//		} catch (StockAlteradoException e) {
//			logger.info("El pedido a actualizar habia alterado su stock en pararelo. Se reintenta la edicion.");
//			e.getPedidoRecargado().setStockPrevio(e.getPedidoRecargado().getStock());
//	    	uiModel.addAttribute("pedidoForm", e.getPedidoRecargado());
//	    	httpServletRequest.setAttribute("stockAlterado", getMessage("pedido.editar.error.stock"));
//	        return "pedido/editar";
//		}
//        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("pedido.editar.ok", pedidoForm.getDescripcion())));
//    }
//    
//    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
//    public String preDelete(@PathVariable("id") Integer id, Model uiModel) {
//    	uiModel.addAttribute("pedidoForm", pedidoService.obtenerPedido(id));
//        return "pedido/borrar";
//    }
//    
//    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
//    public String delete(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//    	pedidoService.eliminarPedido(pedidoForm);
//        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("pedido.borrar.ok", pedidoForm.getDescripcion())));
//    }
//    
//    @RequestMapping(params = "activar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
//    public String preActivar(@PathVariable("id") Integer id, Model uiModel) {
//    	uiModel.addAttribute("pedidoForm", pedidoService.obtenerPedido(id));
//        return "pedido/activar";
//    }
//    
//    @RequestMapping(value = "/activar", method = RequestMethod.POST, produces = "text/html")
//    public String activar(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//    	pedidoService.reactivarPedido(pedidoService.obtenerPedido(pedidoForm.getId()));
//        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("pedido.activar.ok", pedidoForm.getDescripcion())));
//    }
//    
//    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
//        String enc = httpServletRequest.getCharacterEncoding();
//        if (enc == null) {
//            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
//        }
//        try {
//            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
//        } catch (UnsupportedEncodingException uee) {}
//        return pathSegment;
//    }
}