package com.soutech.frigento.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.soutech.frigento.dto.ItemPedidoDTO;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.service.EstadoService;
import com.soutech.frigento.service.PedidoService;
import com.soutech.frigento.service.ProductoCostoService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelPedidoProductoService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.PrinterStack;
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
    public static final String BUSQUEDA_DEFAULT = "pedido?sortFieldName=id&sortOrder=desc";
    
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
    private ProductoCostoService productoCostoService;
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
    	pedido.setVersion((short)0);
    	pedido.setItems(new ArrayList<ItemPedidoDTO>(productos.size()));
    	pedido.setEstado(estados.get(0));
    	//Lo inicializo para que no falle la validación
    	pedido.setCosto(BigDecimal.ZERO);
    	for (Producto producto : productos) {
    		ItemPedidoDTO item = new ItemPedidoDTO();
    		item.setCantidad((short)0);
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
    		mensaje = getMessage("pedido.alta.ok");
    	}else{
    		mensaje = getMessage("pedido.sin.items");
    		httpServletRequest.setAttribute("msgError", mensaje);
    		return "pedido/alta";
    	}
    	
    	if(pedidoForm.getEstado().getId() >= new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO) && pedidoForm.getEnvioMail()){
    		List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(pedidoForm.getId(), "productoCosto.producto.codigo", "asc");
    		ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoPedido(relPedProdList);
				Pedido pedido = relPedProdList.get(0).getPedido();
				String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
				
				sndMailSSL.enviarCorreoPedido(pedido, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {}
			} catch (Exception e) {
				logger.info(getMessage("pedido.confirmar.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("pedido.confirmar.remito.error");
			}
    	}
		uiModel.asMap().clear();
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "estados", required = false) Short estadoPedido[], @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, HttpServletRequest httpServletRequest) {
    	httpServletRequest.setAttribute("pedidos", pedidoService.obtenerPedidos(estadoPedido, sortFieldName, sortOrder));
    	httpServletRequest.setAttribute("estadoSel", estadoPedido);
        if(informar != null){
        	httpServletRequest.setAttribute("informar", informar);
        }
        return "pedido/grilla";
    }
    
    @RequestMapping(params = "detalle", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String detalle(@PathVariable("id") Integer idPedido, Model uiModel) {
        uiModel.addAttribute("relPedProdList", relPedidoProductoService.obtenerByPedido(idPedido, "productoCosto.producto.codigo", "asc"));
        return "pedido/detalle";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer idPed, Model uiModel, HttpServletRequest httpServletRequest) {
    	List<Producto> productos = productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion", "asc");
    	List<Estado> estados = estadoService.obtenerEstadosPedido();
    	List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(idPed, "productoCosto.producto.codigo", "asc");
    	Pedido pedido = relPedProdList.get(0).getPedido();
    	if(pedido.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)){
        	httpServletRequest.setAttribute("informar", getMessage("pedido.editar.estado.error", new Object[]{estados.get(0).getDescripcion().concat(" o ").concat(estados.get(1).getDescripcion())}));
        	return "pedido/grilla";
        }
    	
    	pedido.setItems(new ArrayList<ItemPedidoDTO>(productos.size()));
    	for (Producto producto : productos) {
    		ItemPedidoDTO item = new ItemPedidoDTO();
    		for (RelPedidoProducto rpp : relPedProdList) {
    			if(rpp.getProductoCosto().getProducto().getId().equals(producto.getId())){
    				item.setCantidad(new BigDecimal(rpp.getCantidad()/producto.getPesoCaja()).setScale(0, RoundingMode.HALF_UP).shortValue());
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
    		mensaje = getMessage("pedido.sin.items");
    		httpServletRequest.setAttribute("msgError", mensaje);
    		return "pedido/editar";
    	}
    	
    	if(pedidoForm.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) && pedidoForm.getEnvioMail()){
    		List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(pedidoForm.getId(), "productoCosto.producto.codigo", "asc");
    		ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoPedido(relPedProdList);
				Pedido pedido = relPedProdList.get(0).getPedido();
				String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
				
				sndMailSSL.enviarCorreoPedido(pedido, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {}
			} catch (Exception e) {
				logger.info(getMessage("pedido.editar.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("pedido.editar.remito.error");
			}
    	}
    	
		uiModel.asMap().clear();

        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
    
    @RequestMapping(params = "descargar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String descargar(@PathVariable("id") Integer idPed, HttpServletRequest httpServletRequest, HttpServletResponse response) {
    	List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(idPed, "productoCosto.producto.codigo", "asc");
    	
    	try {
			ByteArrayOutputStream bytes = reportManager.generarRemitoPedido(relPedProdList);
			Pedido pedido = relPedProdList.get(0).getPedido();
			String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
	
			response.setHeader("Content-Disposition", "attachment;filename=" + fileDownload + ".pdf");
			response.setContentType( "application/pdf" );
	        response.setContentLength((int) bytes.size());
	
	        OutputStream outStream = response.getOutputStream();
			bytes.writeTo(outStream);
			outStream.flush();
			outStream.close();
			bytes.close();
    	} catch (Exception e) {
			return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("pedido.generar.remito.error")));
		}
        return null;
    }

    @RequestMapping(params = "anular", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preAnular(@PathVariable("id") Integer idPed, Model uiModel, HttpServletRequest httpServletRequest) {
    	Pedido pedido = pedidoService.obtenerPedido(idPed);
    	if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))
    			|| pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
        	httpServletRequest.setAttribute("informar", getMessage("pedido.anular.estado.error", pedido.getEstado().getDescripcion()));
        	return "pedido/grilla";
        }
    	uiModel.addAttribute("pedidoForm", pedido);
    	return "pedido/anular";
    }
    
    @RequestMapping(value = "/anular", method = RequestMethod.POST, produces = "text/html")
    public String anular(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	pedidoService.anularPedido(pedidoForm.getId());
    	String mensaje = getMessage("pedido.anular.ok", pedidoForm.getId());
    	
		List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(pedidoForm.getId(), "productoCosto.producto.codigo", "asc");
		ByteArrayOutputStream bytes;
		try {
			bytes = reportManager.generarRemitoPedido(relPedProdList);
			Pedido pedido = relPedProdList.get(0).getPedido();
			String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
			
			sndMailSSL.enviarCorreoPedido(pedido, bytes, fileDownload);
			try {
				bytes.flush();
				bytes.close();
			} catch (IOException e) {}
		} catch (Exception e) {
			logger.info(getMessage("pedido.anular.remito.error"));
			logger.error(PrinterStack.getStackTraceAsString(e));
			mensaje = getMessage("pedido.anular.remito.error");
		}
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
    
    @RequestMapping(params = "cumplir", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preCumplir(@PathVariable("id") Integer idPed, Model uiModel, HttpServletRequest httpServletRequest) {
    	List<Estado> estados = estadoService.obtenerEstadosPedido();
    	List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(idPed, "productoCosto.producto.codigo", "asc");
    	Pedido pedido = relPedProdList.get(0).getPedido();
    	if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))
    			|| pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
        	httpServletRequest.setAttribute("informar", getMessage("pedido.cumplir.estado.error", pedido.getEstado().getDescripcion()));
        	return "pedido/grilla";
        }
    	
    	List<ProductoCosto> prodCostoList = productoCostoService.obtenerProductosCosto(null, pedido.getFecha(), "producto.codigo", "asc");
    	pedido.setItems(new ArrayList<ItemPedidoDTO>(prodCostoList.size()));
    	for (ProductoCosto prodCosto : prodCostoList) {
    		ItemPedidoDTO item = new ItemPedidoDTO();
    		item.setCantidad((short)0);
    		item.setCostoCumplir(prodCosto.getCosto());
    		for (RelPedidoProducto rpp : relPedProdList) {
    			if(rpp.getProductoCosto().getProducto().getId().equals(prodCosto.getProducto().getId())){
    				item.setCantidad(new BigDecimal(rpp.getCantidad()/prodCosto.getProducto().getPesoCaja()).setScale(0, RoundingMode.HALF_UP).shortValue());
    				item.setCostoCumplir(rpp.getCosto());
    				break;
    			}
			}
    		item.setProducto(prodCosto.getProducto());
    		pedido.getItems().add(item);
		}
    	
    	List<Estado> estadosPosibles = new ArrayList<Estado>();
    	for (Estado estado : estados) {
			if(estado.getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
				estadosPosibles.add(estado);
			}
		}
    		
		uiModel.addAttribute("pedidoForm", pedido);
		uiModel.addAttribute("estadoList", estadosPosibles);
        return "pedido/cumplir";
    }
    
    @RequestMapping(value = "/cumplir", method = RequestMethod.POST, produces = "text/html")
    public String cumplir(@Valid @ModelAttribute("pedidoForm") Pedido pedidoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	if (bindingResult.hasErrors()) {
        	return "pedido/editar";
        }
    	//Valido costo no nulo
    	String mensaje;
    	Boolean hayCantidades = Boolean.FALSE;
    	for (ItemPedidoDTO item : pedidoForm.getItems()) {
			if(item.getCostoCumplir() == null){
				mensaje = getMessage("pedido.cumplir.sin.costo");
				httpServletRequest.setAttribute("msgError", mensaje);
				return "pedido/cumplir";
			}
			if(item.getCantidad() != 0){
				hayCantidades = Boolean.TRUE;
			}
		}
    	//Valido que exista al menos un item con cantidad
    	if(!hayCantidades){
    		mensaje = getMessage("pedido.cumplir.sin.items");
			httpServletRequest.setAttribute("msgError", mensaje);
			return "pedido/cumplir";
    	}

    	try{
    		pedidoService.cumplirPedido(pedidoForm);
    	}catch (ProductoSinCostoException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "pedido/cumplir";
    	}
    	mensaje = getMessage("pedido.cumplir.ok");
    	
    	if(pedidoForm.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO)) && pedidoForm.getEnvioMail()){
    		List<RelPedidoProducto> relPedProdList = relPedidoProductoService.obtenerByPedido(pedidoForm.getId(), "productoCosto.producto.codigo", "asc");
    		ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoPedido(relPedProdList);
				Pedido pedido = relPedProdList.get(0).getPedido();
				String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
				
				sndMailSSL.enviarCorreoPedido(pedido, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {}
			} catch (Exception e) {
				logger.info(getMessage("pedido.cumplir.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("pedido.cumplir.remito.error");
			}
    	}
    	
		uiModel.asMap().clear();

        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
}