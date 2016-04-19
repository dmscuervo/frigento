package com.soutech.frigento.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.EstadoService;
import com.soutech.frigento.service.ProductoService;
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
	private ProductoService productoService;
	@Autowired
	private ReportManager reportManager;
	@Autowired
	private SendMailSSL sndMailSSL;

	@RequestMapping(params = "preAlta", produces = "text/html")
	public String preAlta(Model uiModel) {
		Venta venta = new Venta();
		venta.setFecha(new Date());
		uiModel.addAttribute("ventaForm", venta);
		uiModel.addAttribute("usuarioList", usuarioService.obtenerUsuariosConCategoria(null,
				new String[] { "nombre", "apellido" }, new String[] { "asc" }));
		return "venta/preAlta";
	}

	@RequestMapping(params = "alta", method = RequestMethod.GET, produces = "text/html")
	public String preAlta(@ModelAttribute("ventaForm") Venta ventaForm, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Usuario usuario = usuarioService.obtenerUsuario(ventaForm.getUsuario().getId());

		List<RelProductoCategoria> relProdCatList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(
				ventaForm.getFecha(), usuario.getCategoriaProducto().getId(), Constantes.ESTADO_REL_VIGENTE);
		List<Estado> estados = estadoService.obtenerEstadosVenta();
		Venta venta = new Venta();
		venta.setUsuario(usuario);
		venta.setFecha(new Date());
		venta.setVersion((short) 0);
		venta.setItems(new ArrayList<ItemVentaDTO>(relProdCatList.size()));
		venta.setEstado(estados.get(0));
		// Lo inicializo para que no falle la validación
		venta.setImporte(BigDecimal.ZERO);
		for (RelProductoCategoria rpc : relProdCatList) {
			Producto producto = rpc.getProducto();
			ItemVentaDTO item = new ItemVentaDTO();
			//item.setCantidad((float) 0);
			item.setProducto(producto);
			item.setCostoVenta(rpc.getProducto().getCostoVenta());
			item.setRelProductoCategoriaId(rpc.getId());
			item.setImporteVenta(rpc.getProducto().getImporteVenta());
			venta.getItems().add(item);
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
		boolean ok = false;
		try {
			ok = ventaService.generarVenta(ventaForm);
		} catch (ProductoSinCostoException e) {
			mensaje = getMessage(e.getKeyMessage(), e.getArgs());
			httpServletRequest.setAttribute("msgError", mensaje);
			return "venta/alta";
		}
		if (ok) {
			mensaje = getMessage("venta.confirmar.ok");
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

	// @RequestMapping(params = "detalle", value="/{id}", method =
	// RequestMethod.GET, produces = "text/html")
	// public String detalle(@PathVariable("id") Integer idVenta, Model uiModel)
	// {
	// uiModel.addAttribute("relPedProdList",
	// relVentaProductoService.obtenerByVenta(idVenta,
	// "productoCosto.producto.codigo", "asc"));
	// return "venta/detalle";
	// }
	//
	// @RequestMapping(params = "editar", value="/{id}", method =
	// RequestMethod.GET, produces = "text/html")
	// public String preEdit(@PathVariable("id") Integer idPed, Model uiModel,
	// HttpServletRequest httpServletRequest) {
	// List<Producto> productos =
	// productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion",
	// "asc");
	// List<Estado> estados = estadoService.obtenerEstadosVenta();
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(idPed,
	// "productoCosto.producto.codigo", "asc");
	// Venta venta = relPedProdList.get(0).getVenta();
	// if(venta.getEstado().getId() > new
	// Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)){
	// httpServletRequest.setAttribute("informar",
	// getMessage("venta.editar.estado.error", new
	// Object[]{estados.get(0).getDescripcion().concat(" o
	// ").concat(estados.get(1).getDescripcion())}));
	// return "venta/grilla";
	// }
	//
	// venta.setItems(new ArrayList<ItemDTO>(productos.size()));
	// for (Producto producto : productos) {
	// ItemDTO item = new ItemDTO();
	// for (RelVentaProducto rpp : relPedProdList) {
	// if(rpp.getProductoCosto().getProducto().getId().equals(producto.getId())){
	// item.setCantidad(new
	// BigDecimal(rpp.getCantidad()/producto.getPesoCaja()).setScale(0,
	// RoundingMode.HALF_UP).shortValue());
	// break;
	// }else{
	// item.setCantidad((short)0);
	// }
	// }
	// item.setProducto(producto);
	// venta.getItems().add(item);
	// }
	//
	// List<Estado> estadosPosibles = new ArrayList<Estado>();
	// for (Estado estado : estados) {
	// if(estado.getId() <= new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)){
	// estadosPosibles.add(estado);
	// }
	// }
	//
	// uiModel.addAttribute("ventaForm", venta);
	// uiModel.addAttribute("estadoList", estadosPosibles);
	// return "venta/editar";
	// }
	//
	// @RequestMapping(value = "/editar", method = RequestMethod.POST, produces
	// = "text/html")
	// public String edit(@Valid @ModelAttribute("ventaForm") Venta ventaForm,
	// BindingResult bindingResult, Model uiModel, HttpServletRequest
	// httpServletRequest) {
	// if (bindingResult.hasErrors()) {
	// return "venta/editar";
	// }
	//
	// String mensaje;
	// boolean ok = false;
	// try {
	// ok = ventaService.actualizarVenta(ventaForm);
	// } catch (ProductoSinCostoException e) {
	// mensaje = getMessage(e.getKeyMessage(), e.getArgs());
	// httpServletRequest.setAttribute("msgError", mensaje);
	// return "venta/editar";
	// }
	// if(ok){
	// mensaje = getMessage("venta.editar.ok");
	// }else{
	// mensaje = getMessage("venta.sin.items");
	// httpServletRequest.setAttribute("msgError", mensaje);
	// return "venta/editar";
	// }
	//
	// if(ventaForm.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_CONFIRMADO)) && ventaForm.getEnvioMail()){
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(ventaForm.getId(),
	// "productoCosto.producto.codigo", "asc");
	// ByteArrayOutputStream bytes;
	// try {
	// bytes = reportManager.generarRemito(relPedProdList);
	// Venta venta = relPedProdList.get(0).getVenta();
	// String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
	//
	// sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
	// try {
	// bytes.flush();
	// bytes.close();
	// } catch (IOException e) {}
	// } catch (Exception e) {
	// logger.info(getMessage("venta.editar.remito.error"));
	// logger.error(PrinterStack.getStackTraceAsString(e));
	// mensaje = getMessage("venta.editar.remito.error");
	// }
	// }
	//
	// uiModel.asMap().clear();
	//
	// return
	// "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	// }
	//
	// @RequestMapping(params = "descargar", value="/{id}", method =
	// RequestMethod.GET, produces = "text/html")
	// public String descargar(@PathVariable("id") Integer idPed,
	// HttpServletRequest httpServletRequest, HttpServletResponse response) {
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(idPed,
	// "productoCosto.producto.codigo", "asc");
	//
	// try {
	// ByteArrayOutputStream bytes =
	// reportManager.generarRemito(relPedProdList);
	// Venta venta = relPedProdList.get(0).getVenta();
	// String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
	//
	// response.setHeader("Content-Disposition", "attachment;filename=" +
	// fileDownload + ".pdf");
	// response.setContentType( "application/pdf" );
	// response.setContentLength((int) bytes.size());
	//
	// OutputStream outStream = response.getOutputStream();
	// bytes.writeTo(outStream);
	// outStream.flush();
	// outStream.close();
	// bytes.close();
	// } catch (Exception e) {
	// return
	// "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("venta.generar.remito.error")));
	// }
	// return null;
	// }
	//
	// @RequestMapping(params = "anular", value="/{id}", method =
	// RequestMethod.GET, produces = "text/html")
	// public String preAnular(@PathVariable("id") Integer idPed, Model uiModel,
	// HttpServletRequest httpServletRequest) {
	// Venta venta = ventaService.obtenerVenta(idPed);
	// if(venta.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_ANULADO))
	// || venta.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
	// httpServletRequest.setAttribute("informar",
	// getMessage("venta.anular.estado.error",
	// venta.getEstado().getDescripcion()));
	// return "venta/grilla";
	// }
	// uiModel.addAttribute("ventaForm", venta);
	// return "venta/anular";
	// }
	//
	// @RequestMapping(value = "/anular", method = RequestMethod.POST, produces
	// = "text/html")
	// public String anular(@Valid @ModelAttribute("ventaForm") Venta ventaForm,
	// BindingResult bindingResult, Model uiModel, HttpServletRequest
	// httpServletRequest) {
	// ventaService.anularVenta(ventaForm.getId());
	// String mensaje = getMessage("venta.anular.ok", ventaForm.getId());
	//
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(ventaForm.getId(),
	// "productoCosto.producto.codigo", "asc");
	// ByteArrayOutputStream bytes;
	// try {
	// bytes = reportManager.generarRemito(relPedProdList);
	// Venta venta = relPedProdList.get(0).getVenta();
	// String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
	//
	// sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
	// try {
	// bytes.flush();
	// bytes.close();
	// } catch (IOException e) {}
	// } catch (Exception e) {
	// logger.info(getMessage("venta.anular.remito.error"));
	// logger.error(PrinterStack.getStackTraceAsString(e));
	// mensaje = getMessage("venta.anular.remito.error");
	// }
	// return
	// "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	// }
	//
	// @RequestMapping(params = "cumplir", value="/{id}", method =
	// RequestMethod.GET, produces = "text/html")
	// public String preCumplir(@PathVariable("id") Integer idPed, Model
	// uiModel, HttpServletRequest httpServletRequest) {
	// List<Estado> estados = estadoService.obtenerEstadosVenta();
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(idPed,
	// "productoCosto.producto.codigo", "asc");
	// Venta venta = relPedProdList.get(0).getVenta();
	// if(venta.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_ANULADO))
	// || venta.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
	// httpServletRequest.setAttribute("informar",
	// getMessage("venta.cumplir.estado.error",
	// venta.getEstado().getDescripcion()));
	// return "venta/grilla";
	// }
	//
	// List<ProductoCosto> prodCostoList =
	// productoCostoService.obtenerProductosCosto(Constantes.ESTADO_REL_VIGENTE,
	// venta.getFecha(), "producto.codigo", "asc");
	// venta.setItems(new ArrayList<ItemDTO>(prodCostoList.size()));
	// for (ProductoCosto prodCosto : prodCostoList) {
	// ItemDTO item = new ItemDTO();
	// item.setCantidad((short)0);
	// item.setCostoCumplir(prodCosto.getCosto());
	// for (RelVentaProducto rpp : relPedProdList) {
	// if(rpp.getProductoCosto().getProducto().getId().equals(prodCosto.getProducto().getId())){
	// item.setCantidad(new
	// BigDecimal(rpp.getCantidad()/prodCosto.getProducto().getPesoCaja()).setScale(0,
	// RoundingMode.HALF_UP).shortValue());
	// item.setCostoCumplir(rpp.getCosto());
	// break;
	// }
	// }
	// item.setProducto(prodCosto.getProducto());
	// venta.getItems().add(item);
	// }
	//
	// List<Estado> estadosPosibles = new ArrayList<Estado>();
	// for (Estado estado : estados) {
	// if(estado.getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
	// estadosPosibles.add(estado);
	// }
	// }
	//
	// uiModel.addAttribute("ventaForm", venta);
	// uiModel.addAttribute("estadoList", estadosPosibles);
	// return "venta/cumplir";
	// }
	//
	// @RequestMapping(value = "/cumplir", method = RequestMethod.POST, produces
	// = "text/html")
	// public String cumplir(@Valid @ModelAttribute("ventaForm") Venta
	// ventaForm, BindingResult bindingResult, Model uiModel, HttpServletRequest
	// httpServletRequest) {
	// if (bindingResult.hasErrors()) {
	// return "venta/editar";
	// }
	// //Valido costo no nulo
	// String mensaje;
	// Boolean hayCantidades = Boolean.FALSE;
	// for (ItemDTO item : ventaForm.getItems()) {
	// if(item.getCostoCumplir() == null){
	// mensaje = getMessage("venta.cumplir.sin.costo");
	// httpServletRequest.setAttribute("msgError", mensaje);
	// return "venta/cumplir";
	// }
	// if(item.getCantidad() != 0){
	// hayCantidades = Boolean.TRUE;
	// }
	// }
	// //Valido que exista al menos un item con cantidad
	// if(!hayCantidades){
	// mensaje = getMessage("venta.cumplir.sin.items");
	// httpServletRequest.setAttribute("msgError", mensaje);
	// return "venta/cumplir";
	// }
	//
	// try{
	// ventaService.cumplirVenta(ventaForm);
	// }catch (ProductoSinCostoException e) {
	// mensaje = getMessage(e.getKeyMessage(), e.getArgs());
	// httpServletRequest.setAttribute("msgError", mensaje);
	// return "venta/cumplir";
	// }
	// mensaje = getMessage("venta.cumplir.ok");
	//
	// if(ventaForm.getEstado().getId().equals(new
	// Short(Constantes.ESTADO_PEDIDO_ENTREGADO)) && ventaForm.getEnvioMail()){
	// List<RelVentaProducto> relPedProdList =
	// relVentaProductoService.obtenerByVenta(ventaForm.getId(),
	// "productoCosto.producto.codigo", "asc");
	// ByteArrayOutputStream bytes;
	// try {
	// bytes = reportManager.generarRemito(relPedProdList);
	// Venta venta = relPedProdList.get(0).getVenta();
	// String fileDownload = "Venta_"+Utils.generarNroRemito(venta);
	//
	// sndMailSSL.enviarCorreoVenta(venta, bytes, fileDownload);
	// try {
	// bytes.flush();
	// bytes.close();
	// } catch (IOException e) {}
	// } catch (Exception e) {
	// logger.info(getMessage("venta.cumplir.remito.error"));
	// logger.error(PrinterStack.getStackTraceAsString(e));
	// mensaje = getMessage("venta.cumplir.remito.error");
	// }
	// }
	//
	// uiModel.asMap().clear();
	//
	// return
	// "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
	// }
}