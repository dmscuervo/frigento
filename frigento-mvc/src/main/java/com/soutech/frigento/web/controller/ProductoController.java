package com.soutech.frigento.web.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.service.ProductoCostoService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelPedidoProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;

@Controller
@RequestMapping(value="/producto")
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
			uiModel.asMap().clear();
		} catch (EntityExistException e) {
			String key = "EntityExist.productoForm."+e.getField();
			bindingResult.rejectValue(e.getField(), key);
			logger.info(getMessage(key));
			return "producto/alta";
		}
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
    	Date fechaMinD = new Date();
    	//Inicializo valores en caso de nulos
    	if(fechaHastaMin != null){
    		fechaHastaMin2 = fechaHastaMin2 == null ? fechaHastaMin : fechaHastaMin2;
    		fechaHastaMin3 = fechaHastaMin3 == null ? fechaHastaMin : fechaHastaMin3;
    	}else if(fechaHastaMin2 != null){
    		fechaHastaMin = fechaHastaMin == null ? fechaHastaMin2 : fechaHastaMin;
    		fechaHastaMin3 = fechaHastaMin3 == null ? fechaHastaMin2 : fechaHastaMin3;
    	}else if(fechaHastaMin3 != null){
    		fechaHastaMin = fechaHastaMin == null ? fechaHastaMin3 : fechaHastaMin;
    		fechaHastaMin2 = fechaHastaMin2 == null ? fechaHastaMin3 : fechaHastaMin2;
    	}
    	if(fechaHastaMin != null){
    		fechaMinD = fechaHastaMin.before(fechaHastaMin2) ? fechaHastaMin : fechaHastaMin2;
    		fechaMinD = fechaMinD.before(fechaHastaMin3) ? fechaMinD : fechaHastaMin3;
    	}
    	
    	uiModel.addAttribute("maxDateAlta", fechaMinD.getTime());
    	Date fechaDesdeMin = productoCostoService.obtenerMinFechaDesde(id);
    	Producto prod = productoService.obtenerProducto(id);
    	if(prod.getFechaBaja() != null){
        	httpServletRequest.setAttribute("informar", getMessage("producto.editar.estado.error"));
        	return "pedido/grilla";
        }
    	
    	prod.setFechaAlta(fechaDesdeMin);
    	prod.setStockPrevio(prod.getStock());
    	uiModel.addAttribute("productoForm", prod);
        return "producto/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	formValidator.validate(productoForm, bindingResult);
        if (bindingResult.hasErrors()) {
        	return "producto/editar";
        }
        uiModel.asMap().clear();
        try {
			productoService.actualizarProducto(productoForm);
		} catch (StockAlteradoException e) {
			logger.info("El producto a actualizar habia alterado su stock en pararelo. Se reintenta la edicion.");
			e.getProductoRecargado().setStockPrevio(e.getProductoRecargado().getStock());
	    	uiModel.addAttribute("productoForm", e.getProductoRecargado());
	    	httpServletRequest.setAttribute("stockAlterado", getMessage("producto.editar.error.stock"));
	        return "producto/editar";
		}
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
    	productoService.eliminarProducto(productoForm);
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
    	productoService.reactivarProducto(productoService.obtenerProducto(productoForm.getId()));
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.activar.ok", productoForm.getDescripcion())));
    }
    
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}