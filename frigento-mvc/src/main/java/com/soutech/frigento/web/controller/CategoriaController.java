package com.soutech.frigento.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.RelProductoCategoriaService;

@Controller
@RequestMapping(value="/categoria")
@Secured({"ROLE_ADMIN"})
public class CategoriaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "categoria?sortFieldName=descripcion&sortOrder=asc";
    
    @Autowired
    public CategoriaService categoriaService;
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;

    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	uiModel.addAttribute("categoriaForm", new Categoria());
        return "categoria/alta";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("categoriaForm") Categoria categoriaForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "categoria/alta";
        }
        uiModel.asMap().clear();
        categoriaService.saveCategoria(categoriaForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("categoria.alta.ok", categoriaForm.getDescripcion())));
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Short id, Model uiModel) {
        uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        uiModel.addAttribute("itemId", id);
        return "categoria/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
        uiModel.addAttribute("categorias", categoriaService.obtenerCategorias(sortFieldName, sortOrder));
        uiModel.addAttribute("mapaCant", relProductoCategoriaService.obtenerCantProductoVigentesXCat());
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        return "categoria/grilla";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Short id, Model uiModel) {
    	uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        return "categoria/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("categoriaForm") Categoria categoriaForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "categoria/editar";
        }
        uiModel.asMap().clear();
        categoriaService.actualizarCategoria(categoriaForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("categoria.editar.ok", categoriaForm.getDescripcion())));
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Short id, Model uiModel) {
    	uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        return "categoria/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@ModelAttribute("categoriaForm") Categoria categoriaForm, HttpServletRequest httpServletRequest) {
        try {
			categoriaService.eliminarCategoria(categoriaForm);
		} catch (EntityExistException e) {
			String key = "EntityExist.categoriaForm."+e.getField();
			httpServletRequest.setAttribute("msgError", getMessage(key, e.getArgs()));
			logger.info(getMessage(key, e.getArgs()));
			return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage(key, e.getArgs())));
		}
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("categoria.borrar.ok", categoriaForm.getDescripcion())));
    }
    
//    @RequestMapping(params = "borrar", produces = "text/html")
//    public String delete(@RequestParam(value = "id", required = true) Short id, HttpServletRequest httpServletRequest) {
//        categoriaService.eliminarCategoria(id);
//        httpServletRequest.setAttribute("msgTitle", getMessage("categoria.borrar.title"));
//        httpServletRequest.setAttribute("msgResult", getMessage("categoria.borrar.ok"));
//        httpServletRequest.setAttribute("urlOk", "categoria/grilla");
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