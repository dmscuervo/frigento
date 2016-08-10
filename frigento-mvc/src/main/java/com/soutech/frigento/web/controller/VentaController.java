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

import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.exception.ProductoSinCategoriaException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.Promocion;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.EstadoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.service.RelVentaProductoService;
import com.soutech.frigento.service.UsuarioService;
import com.soutech.frigento.service.VentaService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.util.SendMailSSL;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.reports.ReportManager;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;

@Controller
@RequestMapping(value = "/venta")
@SessionAttributes(names = "estadoList")
@Secured({ "ROLE_ADMIN" })
public class VentaController extends GenericController {

	protected final Log logger = LogFactory.getLog(getClass());
	// public static final String BUSQUEDA_DEFAULT =
	// "venta?estados="+Constantes.ESTADO_PEDIDO_PENDIENTE+","+Constantes.ESTADO_PEDIDO_CONFIRMADO+"&sortFieldName=id&sortOrder=asc";
	public static final String BUSQUEDA_DEFAULT = "venta?estado=A&sortFieldName=id&sortOrder=desc";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class,
				new FormatoDateTruncateValidator(new SimpleDateFormat("dd/MM/yyyy HH:mm"), true));
	}

	@Autowired
	private VentaService ventaService;
	@Autowired
	private RelProductoCategoriaService relProductoCategoriaService;
	@Autowired
	private RelVentaProductoService relVentaProductoService;
	@Autowired
	private EstadoService estadoService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ReportManager reportManager;
	@Autowired
	private SendMailSSL sndMailSSL;

	@RequestMapping(params = "preAlta", produces = "text/html")
	public String preAlta(Model uiModel) {
		Venta venta = new Venta();
		venta.setFecha(new Date());
		uiModel.addAttribute("ventaForm", venta);
		List<Usuario> usuarios = usuarioService.obtenerUsuariosConCategoria(null, new String[] { "nombre", "apellido" }, new String[] { "asc" });
		if(usuarios.isEmpty()){
			uiModel.addAttribute("msgTitle", getMessage("venta.alta.title"));
			uiModel.addAttribute("msgResult", getMessage("venta.preAlta.sin.usuarios"));
			return "generic/mensajeException";
		}
		uiModel.addAttribute("usuarioList", usuarios);
		return "venta/preAlta";
	}

	@RequestMapping(params = "alta", method = RequestMethod.GET, produces = "text/html")
	public String preAlta(@ModelAttribute("ventaForm") Venta ventaForm, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Usuario usuario = usuarioService.obtenerUsuario(ventaForm.getUsuario().getId());

		List<RelProductoCategoria> relProdCatList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(
				ventaForm.getFecha(), usuario.getCategoriaProducto().getId());
		List<Estado> estados = estadoService.obtenerEstadosVenta();
		Venta venta = new Venta();
		venta.setUsuario(usuario);
		venta.setFecha(ventaForm.getFecha());
		venta.setVersion((short) 0);
		venta.setItems(new ArrayList<ItemVentaDTO>(relProdCatList.size()));
		venta.setEstado(estados.get(0));
		// Lo inicializo para que no falle la validación
		venta.setImporte(BigDecimal.ZERO);
		venta.setIncrementoIva(BigDecimal.ZERO);
		for (RelProductoCategoria rpc : relProdCatList) {
			Producto producto = rpc.getProducto();
			ItemVentaDTO item = new ItemVentaDTO();
			item.setProducto(producto);
			item.setCantidad(0f);
			item.setRelProductoCategoriaId(rpc.getId());
			item.setImporteVenta(rpc.getProducto().getImporteVenta());
			venta.getItems().add(item);
			for (Promocion promo : rpc.getPromociones()) {
				if(Utils.estaDentroDeRelacion(ventaForm.getFecha(), promo.getFechaDesde(), promo.getFechaHasta())){
					item = new ItemVentaDTO();
					item.setProducto(producto);
					item.setCantidad(0f);
					item.setRelProductoCategoriaId(rpc.getId());
					BigDecimal descuento = rpc.getProducto().getImporteVenta().multiply(promo.getDescuento()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
					item.setImporteVenta(rpc.getProducto().getImporteVenta().subtract(descuento));
					item.setPromocion(promo);
					venta.getItems().add(item);
				}
			}
		}

		List<Estado> estadosPosibles = new ArrayList<Estado>();
		for (Estado estado : estados) {
			if (estado.getId() <= new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) {
				estadosPosibles.add(estado);
			}
		}
		uiModel.addAttribute("ventaForm", venta);
		uiModel.addAttribute("estadoList", estadosPosibles);
		return "venta/alta";
	}

	@RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
	public String alta(@Valid @ModelAttribute("ventaForm") Venta ventaForm, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			return "venta/alta";
		}

		String mensaje;
		for (ItemVentaDTO item : ventaForm.getItems()) {
			Float cantidadMinima = new BigDecimal(item.getCantidad()).add(new BigDecimal(Parametros.getValor(Parametros.TOLERANCIA_GRAMOS_PROMOCION_VTA))).setScale(3, RoundingMode.HALF_UP).floatValue();
			if(item.getPromocion() != null && item.getCantidad() != 0 && cantidadMinima < item.getPromocion().getCantidadMinima()){
				String nombrePromo = getMessage("venta.promocion", item.getPromocion().getCantidadMinima());
				Object pesoMinimo = new BigDecimal(item.getPromocion().getCantidadMinima()).subtract(new BigDecimal(Parametros.getValor(Parametros.TOLERANCIA_GRAMOS_PROMOCION_VTA)).setScale(3, RoundingMode.HALF_UP));
				mensaje = getMessage("venta.promocion.minimo.error", new Object[]{nombrePromo, pesoMinimo});
				httpServletRequest.setAttribute("msgError", mensaje);
				return "venta/alta";
			}
		}
		
		boolean ok = false;
		try {
			ok = ventaService.generarVenta(ventaForm);
		} catch (ProductoSinCategoriaException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "venta/alta";
		}
		if (ok) {
			mensaje = getMessage("venta.alta.ok");
		} else {
			mensaje = getMessage("venta.sin.items");
			httpServletRequest.setAttribute("msgError", mensaje);
			//Esto lo hago para que la pantalla quede sin valores precargados 
			for (ItemVentaDTO item : ventaForm.getItems()) {
				item.setCantidad(null);
			}
			return "venta/alta";
		}

		if (ventaForm.getEstado().getId() >= new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)
				&& ventaForm.getEnvioMail()) {
			ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoVenta(ventaForm);
				String fileDownload = "Venta_" + Utils.generarNroRemito(ventaForm);

				sndMailSSL.enviarCorreoVenta(ventaForm, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {
				}
			} catch (Exception e) {
				logger.info(getMessage("venta.confirmar.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("venta.confirmar.remito.error");
			}
		}
		uiModel.asMap().clear();
		return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	}

	@RequestMapping(params = "editar", value = "/{id}", method = RequestMethod.GET, produces = "text/html")
	public String preEdit(@PathVariable("id") Integer idVta, Model uiModel, HttpServletRequest httpServletRequest) {
		
		List<RelVentaProducto> relVtaProdList = relVentaProductoService.obtenerByVenta(idVta, "relProductoCategoria.producto.codigo", "asc");
		Venta venta = relVtaProdList.get(0).getVenta();
		
		Usuario usuario = usuarioService.obtenerUsuario(venta.getUsuario().getId());

		List<RelProductoCategoria> relProdCatList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(venta.getFecha(), usuario.getCategoriaProducto().getId());
		List<Estado> estados = estadoService.obtenerEstadosVenta();
		
		if (venta.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) {
			httpServletRequest.setAttribute("informar", getMessage("venta.editar.estado.error", new Object[] {
					estados.get(0).getDescripcion().concat(" o").concat(estados.get(1).getDescripcion()) }));
			return "venta/grilla";
		}
		
		venta.setItems(new ArrayList<ItemVentaDTO>(relProdCatList.size()));
		venta.setConIva(venta.getIncrementoIva() != null && !venta.getIncrementoIva().equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)));
		for (RelProductoCategoria rpc : relProdCatList) {
			Producto producto = rpc.getProducto();
			ItemVentaDTO item = new ItemVentaDTO();
			item.setProducto(producto);
			item.setRelProductoCategoriaId(rpc.getId());
			item.setImporteVenta(rpc.getProducto().getImporteVenta());
			for (RelVentaProducto rvp : relVtaProdList) {
				if (rvp.getRelProductoCategoria().getProducto().getId().equals(producto.getId())
						&& rvp.getPromocion() == null) {
					item.setCantidad(rvp.getCantidad());
					break;
				} else {
					item.setCantidad((float) 0);
				}
			}
			venta.getItems().add(item);
			for (Promocion promo : rpc.getPromociones()) {
				if(Utils.estaDentroDeRelacion(venta.getFecha(), promo.getFechaDesde(), promo.getFechaHasta())){
					item = new ItemVentaDTO();
					item.setProducto(producto);
					item.setRelProductoCategoriaId(rpc.getId());
					BigDecimal descuento = rpc.getProducto().getImporteVenta().multiply(promo.getDescuento()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
					item.setImporteVenta(rpc.getProducto().getImporteVenta().subtract(descuento));
					item.setPromocion(promo);
					for (RelVentaProducto rpp : relVtaProdList) {
						item.setCantidad((float) 0);
						if (rpp.getRelProductoCategoria().getProducto().getId().equals(producto.getId())) {
							if(rpp.getPromocion() != null && rpp.getPromocion().getId().equals(promo.getId())){
								item.setCantidad(rpp.getCantidad());
								break;
							}
						}
					}
					venta.getItems().add(item);
				}
			}
		}
		
		List<Estado> estadosPosibles = new ArrayList<Estado>();
		for (Estado estado : estados) {
			if(estado.getId().equals(new Short(Constantes.ESTADO_PEDIDO_PENDIENTE))
					&& venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_PENDIENTE))){
				estadosPosibles.add(estado);
			}
			if(estado.getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))) {
				estadosPosibles.add(estado);
			}
		}
		uiModel.addAttribute("ventaForm", venta);
		uiModel.addAttribute("estadoList", estadosPosibles);
		return "venta/editar";
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
	public String edit(@Valid @ModelAttribute("ventaForm") Venta ventaForm, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
    	if (bindingResult.hasErrors()) {
        	return "venta/editar";
        }

    	String mensaje;
    	for (ItemVentaDTO item : ventaForm.getItems()) {
    		Float cantidadMinima = new BigDecimal(item.getCantidad()).add(new BigDecimal(Parametros.getValor(Parametros.TOLERANCIA_GRAMOS_PROMOCION_VTA))).setScale(3, RoundingMode.HALF_UP).floatValue();
			if(item.getPromocion() != null && item.getCantidad() != 0 && cantidadMinima < item.getPromocion().getCantidadMinima()){
				String nombrePromo = getMessage("venta.promocion", item.getPromocion().getCantidadMinima());
				Object pesoMinimo = new BigDecimal(item.getPromocion().getCantidadMinima()).subtract(new BigDecimal(Parametros.getValor(Parametros.TOLERANCIA_GRAMOS_PROMOCION_VTA)).setScale(3, RoundingMode.HALF_UP));
				mensaje = getMessage("venta.promocion.minimo.error", new Object[]{nombrePromo, pesoMinimo});
				httpServletRequest.setAttribute("msgError", mensaje);
				return "venta/editar";
			}
		}
    	boolean ok = false;
		try {
			ok = ventaService.actualizarVenta(ventaForm);
		} catch (ProductoSinCategoriaException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "venta/editar";
		}
    	if(ok){
    		mensaje = getMessage("venta.editar.ok");
    	}else{
    		mensaje = getMessage("venta.sin.items");
    		httpServletRequest.setAttribute("msgError", mensaje);
    		return "venta/editar";
    	}
    	
    	if(ventaForm.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) && ventaForm.getEnvioMail()){
    		List<RelVentaProducto> relVtaProdList = relVentaProductoService.obtenerByVenta(ventaForm.getId(), "relProductoCategoria.producto.codigo", "asc");
    		ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoVenta(relVtaProdList);
				Venta venta = relVtaProdList.get(0).getVenta();
				String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
				
				sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {}
			} catch (Exception e) {
				logger.info(getMessage("venta.editar.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("venta.editar.remito.error");
			}
    	}
    	
		uiModel.asMap().clear();

        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
	
	@RequestMapping(produces = "text/html")
	public String listar(@RequestParam(value = "estados", required = false) Short estadoVenta[],
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "informar", required = false) String informar,
			HttpServletRequest httpServletRequest) {
			
		httpServletRequest.setAttribute("ventas", ventaService.obtenerVentas(estadoVenta, sortFieldName, sortOrder));
		httpServletRequest.setAttribute("estadoSel", estadoVenta);
		if (informar != null) {
			httpServletRequest.setAttribute("informar", informar);
		}
		return "venta/grilla";
	}

	 @RequestMapping(params = "descargar", value="/{id}", method =
	 RequestMethod.GET, produces = "text/html")
	 public String descargar(@PathVariable("id") Integer idVta,
	 HttpServletRequest httpServletRequest, HttpServletResponse response) {
		 List<RelVentaProducto> relVtaProdList = relVentaProductoService.obtenerByVenta(idVta, "relProductoCategoria.producto.codigo", "asc");
		
		 try {
		 ByteArrayOutputStream bytes =
		 reportManager.generarRemitoVenta(relVtaProdList);
		 Venta venta = relVtaProdList.get(0).getVenta();
		 String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
		
		 response.setHeader("Content-Disposition", "attachment;filename=" +
		 fileDownload + ".pdf");
		 response.setContentType( "application/pdf" );
		 response.setContentLength((int) bytes.size());
		
		 OutputStream outStream = response.getOutputStream();
		 bytes.writeTo(outStream);
		 outStream.flush();
		 outStream.close();
		 bytes.close();
		 } catch (Exception e) {
		 return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("venta.generar.remito.error")));
		 }
		 return null;
	 }
	
	@RequestMapping(params = "anular", value="/{id}", method = RequestMethod.GET, produces = "text/html")
	public String preAnular(@PathVariable("id") Integer idVta, Model uiModel, HttpServletRequest httpServletRequest) {
		Venta venta = ventaService.obtenerVenta(idVta);
		if (venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))
				|| venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))) {
			httpServletRequest.setAttribute("informar", getMessage("venta.anular.estado.error", venta.getEstado().getDescripcion()));
			return "venta/grilla";
		}
		uiModel.addAttribute("ventaForm", venta);
		return "venta/anular";
	}

	@RequestMapping(value = "/anular", method = RequestMethod.POST, produces = "text/html")
	public String anular(@Valid @ModelAttribute("ventaForm") Venta ventaForm, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (ventaForm.getFechaAnulado().before(ventaForm.getFecha())) {
			return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("venta.anular.fecha.error", Utils.formatDate(ventaForm.getFecha(), Utils.SDF_DDMMYYYY_HHMM))));
		}
		ventaService.anularVenta(ventaForm.getId(), ventaForm.getFechaAnulado());
		String mensaje = getMessage("venta.anular.ok", ventaForm.getId());

		if(ventaForm.getUsuario().getEmail() != null && ventaForm.getEnvioMail()){
			List<RelVentaProducto> relVtaProdList = relVentaProductoService.obtenerByVenta(ventaForm.getId(), "relProductoCategoria.producto.codigo", "asc");
			ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoVenta(relVtaProdList);
				Venta venta = relVtaProdList.get(0).getVenta();
				String fileDownload = "Venta_" + Utils.generarNroRemito(venta);
	
				sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {
				}
			} catch (Exception e) {
				logger.info(getMessage("venta.anular.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("venta.anular.remito.error");
			}
		}
		return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	}
	
	@RequestMapping(params = "cumplir", value="/{id}", method = RequestMethod.GET, produces = "text/html")
	public String preCumplir(@PathVariable("id") Integer idVta, Model uiModel, HttpServletRequest httpServletRequest) {
		Venta venta = ventaService.obtenerVenta(idVta);
		if (venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))
				|| venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))) {
			httpServletRequest.setAttribute("informar", getMessage("venta.cumplir.estado.error", venta.getEstado().getDescripcion()));
			return "venta/grilla";
		}
		uiModel.addAttribute("ventaForm", venta);
		return "venta/cumplir";
	}

	@RequestMapping(value = "/cumplir", method = RequestMethod.POST, produces = "text/html")
	public String cumplir(@Valid @ModelAttribute("ventaForm") Venta ventaForm, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (ventaForm.getFechaEntregado().before(ventaForm.getFecha())) {
			return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("venta.cumplir.fecha.error", Utils.formatDate(ventaForm.getFecha(), Utils.SDF_DDMMYYYY_HHMM))));
		}
		ventaService.cumplirVenta(ventaForm.getId(), ventaForm.getFechaEntregado());
		String mensaje = getMessage("venta.cumplir.ok", ventaForm.getId());

		if(ventaForm.getUsuario().getEmail() != null && ventaForm.getEnvioMail()){
			List<RelVentaProducto> relVtaProdList = relVentaProductoService.obtenerByVenta(ventaForm.getId(), "relProductoCategoria.producto.codigo", "asc");
			ByteArrayOutputStream bytes;
			try {
				bytes = reportManager.generarRemitoVenta(relVtaProdList);
				Venta venta = relVtaProdList.get(0).getVenta();
				String fileDownload = "Venta_" + Utils.generarNroRemito(venta);
	
				sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
				try {
					bytes.flush();
					bytes.close();
				} catch (IOException e) {
				}
			} catch (Exception e) {
				logger.info(getMessage("venta.cumplir.remito.error"));
				logger.error(PrinterStack.getStackTraceAsString(e));
				mensaje = getMessage("venta.anular.remito.error");
			}
		}
		return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	}

	@RequestMapping(params = "detalle", value = "/{id}", method = RequestMethod.GET, produces = "text/html")
	public String detalle(@PathVariable("id") Integer idVenta, @RequestParam(value="urlBack", required=false) String urlBack, Model uiModel) {
		if(urlBack == null){
			urlBack = "venta?sortFieldName=id&sortOrder=desc";
		}
		uiModel.addAttribute("relVtaProdList", relVentaProductoService.obtenerByVenta(idVenta, "relProductoCategoria.producto.codigo", "asc"));
		uiModel.addAttribute("urlVolver", urlBack);
		return "venta/detalle";
	}

}