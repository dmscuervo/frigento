package com.soutech.frigento.web.controller;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoCostoService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelPedidoProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;

@Controller
@RequestMapping(value="/producto")
@Secured({"ROLE_ADMIN"})
public class ProductoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "producto?estado=A&sortFieldName=descripcion&sortOrder=asc";
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new FormatoDateTruncateValidator(new SimpleDateFormat("dd/MM/yyyy HH:mm"), false));   
    }
    
    @Autowired
    public ProductoService productoService;
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
    
    @Autowired
    public ProductoCostoService productoCostoService;
    
    @Autowired
    public RelPedidoProductoService relPedidoProductoService;

    
    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	Producto producto = new Producto();
    	producto.setFechaAlta(new Date());
		uiModel.addAttribute("productoForm", producto );
        return "producto/alta";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	formValidator.validate(productoForm, bindingResult);
        if (bindingResult.hasErrors()) {
        	return "producto/alta";
        }
        try {
			productoService.saveProducto(productoForm);
		} catch (EntityExistException e) {
			String key = "EntityExist.productoForm."+e.getField();
			bindingResult.rejectValue(e.getField(), key);
			logger.info(getMessage(key));
			return "producto/alta";
		}
        uiModel.asMap().clear();
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.alta.ok", productoForm.getDescripcion())));
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("productoForm", productoService.obtenerProducto(id));
        uiModel.addAttribute("itemId", id);
        return "producto/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "estado", required = true) String estado, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, HttpServletRequest httpServletRequest) {
    	httpServletRequest.setAttribute("productos", productoService.obtenerProductos(estado, sortFieldName, sortOrder));
    	httpServletRequest.setAttribute("estadoSel", estado);
        if(informar != null){
        	httpServletRequest.setAttribute("informar", informar);
        }
        return "producto/grilla";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    	Date fechaHastaMin = relProductoCategoriaService.obtenerMinFechaDesde(id);
    	Date fechaHastaMin2 = productoCostoService.obtenerMinFechaHasta(id);
    	Date fechaHastaMin3 = relPedidoProductoService.obtenerMinFechaPedido(id);
    	//Me quedo con la mas vieja
    	//Inicializo valores en caso de nulos
    	Date fechaMinD = Utils.dameFechaMasAnitgua(fechaHastaMin, fechaHastaMin2, fechaHastaMin3);
    	if(fechaMinD == null){
    		fechaMinD = new Date();
    	}
    	
    	uiModel.addAttribute("maxDateAlta", fechaMinD.getTime());
    	Producto prod = productoService.obtenerProducto(id);
    	if(prod.getFechaBaja() != null){
        	httpServletRequest.setAttribute("informar", getMessage("producto.editar.estado.error"));
        	return "pedido/grilla";
        }
    	
    	prod.setStockPrevio(prod.getStock());
    	prod.setCostoPrevio(prod.getCostoActual());
    	logger.info("La fechaAlta del producto a editar es: " + Utils.formatDate(prod.getFechaAlta(), Utils.SDF_DDMMYYYY_HHMM));
    	uiModel.addAttribute("productoForm", prod);
        return "producto/editar";
    }
    
    @RequestMapping(value = "/validarEditar", method = RequestMethod.POST, produces = "text/html")
    public String validarEditar(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	formValidator.validate(productoForm, bindingResult);
        if (bindingResult.hasErrors()) {
        	return "producto/editar";
        }
        //Si cambio el costo, me fijo si tiene categorias para advertir
    	if(productoForm.getCostoPrevio() != null && !productoForm.getCostoPrevio().equals(productoForm.getCostoActual())){
    		//Obtengo la relacion prod-costo que se modificara
    		ProductoCosto prodCosto = productoCostoService.obtenerActual(productoForm.getId());
    		//Busco si tiene relaciones con categorias para la fecha de prod-costo
    		List<RelProductoCategoria> relaciones = relProductoCategoriaService.obtenerRelacionesAPartirDe(productoForm.getId(), prodCosto.getFechaDesde());
    		if(!relaciones.isEmpty()){
    			uiModel.addAttribute("productoForm", productoForm);
    	    	return "producto/editConfirmar";
    		}
    	}
    	return edit(productoForm, bindingResult, uiModel, httpServletRequest);
    }

	@RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		formValidator.validate(productoForm, bindingResult);
        if (bindingResult.hasErrors()) {
        	return "producto/editar";
        }
    	try {
			productoService.actualizarProducto(productoForm);
		} catch (StockAlteradoException e) {
			logger.info("El producto a actualizar habia alterado su stock en pararelo. Se reintenta la edicion.");
			e.getProductoRecargado().setStockPrevio(e.getProductoRecargado().getStock());
	    	uiModel.addAttribute("productoForm", e.getProductoRecargado());
	    	httpServletRequest.setAttribute("msgError", getMessage("producto.editar.error.stock"));
	        return "producto/editar";
		} catch (FechaDesdeException e) {
			logger.info("Se intento cambiar la fecha de alta pero la misma se superponia con otro rango de fechas en ProductoCosto.");
			httpServletRequest.setAttribute("msgError", getMessage(e.getKeyMessage(), e.getArgs()));
	        return "producto/editar";
		}
        uiModel.asMap().clear();
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.editar.ok", productoForm.getDescripcion())));
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    	Producto prod = productoService.obtenerProducto(id);
    	if(prod.getFechaBaja() != null){
        	httpServletRequest.setAttribute("informar", getMessage("producto.borrar.estado.error"));
        	return "pedido/grilla";
        }
    	uiModel.addAttribute("productoForm", prod);
    	return "producto/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	productoService.eliminarProducto(productoForm.getId());
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.borrar.ok", productoForm.getDescripcion())));
    }
    
    @RequestMapping(params = "activar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preActivar(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    	Producto prod = productoService.obtenerProducto(id);
    	if(prod.getFechaBaja() == null){
        	httpServletRequest.setAttribute("informar", getMessage("producto.activar.estado.error"));
        	return "pedido/grilla";
        }
    	uiModel.addAttribute("productoForm", prod);
        return "producto/activar";
    }
    
    @RequestMapping(value = "/activar", method = RequestMethod.POST, produces = "text/html")
    public String activar(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	productoService.reactivarProducto(productoForm.getId());
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.activar.ok", productoForm.getDescripcion())));
    }
    
}