package com.soutech.frigento.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.soutech.frigento.model.Producto;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.web.validator.FormValidator;

@Controller
@RequestMapping(value="/producto")
public class ProductoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    private final String BUSQUEDA_DEFAULT = "producto?sortFieldName=descripcion&sortOrder=asc";
    
    @Autowired
    public ProductoService productoService;

    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	uiModel.addAttribute("productoForm", new Producto());
        return "producto/alta";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "producto/alta";
        }
        FormValidator fv = new FormValidator();
        fv.validate(productoForm, bindingResult);
        uiModel.asMap().clear();
        productoService.saveProducto(productoForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.alta.ok", productoForm.getDescripcion())));
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("productoForm", productoService.obtenerProducto(id));
        uiModel.addAttribute("itemId", id);
        return "producto/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
        uiModel.addAttribute("productos", productoService.obtenerProductos(sortFieldName, sortOrder));
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        return "producto/grilla";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("productoForm", productoService.obtenerProducto(id));
        return "producto/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "producto/editar";
        }
        uiModel.asMap().clear();
        productoService.actualizarProducto(productoForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.editar.ok", productoForm.getDescripcion())));
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("productoForm", productoService.obtenerProducto(id));
        return "producto/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@Valid @ModelAttribute("productoForm") Producto productoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "producto/borrar";
        }
        uiModel.asMap().clear();
        productoService.eliminarProducto(productoForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("producto.borrar.ok", productoForm.getDescripcion())));
    }
    
//    @RequestMapping(params = "borrar", produces = "text/html")
//    public String delete(@RequestParam(value = "id", required = true) Integer id, HttpServletRequest httpServletRequest) {
//        productoService.eliminarProducto(id);
//        httpServletRequest.setAttribute("msgTitle", getMessage("producto.borrar.title"));
//        httpServletRequest.setAttribute("msgResult", getMessage("producto.borrar.ok"));
//        httpServletRequest.setAttribute("urlOk", "producto/grilla");
//        return "generic/mensaje";
//    }
    
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