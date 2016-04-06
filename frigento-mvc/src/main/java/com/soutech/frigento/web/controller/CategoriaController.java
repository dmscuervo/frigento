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

import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.service.CategoriaService;

@Controller
@RequestMapping(value="/categoria")
public class CategoriaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    public CategoriaService categoriaService;

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
        httpServletRequest.setAttribute("msgTitle", getMessage("categoria.alta.title"));
        httpServletRequest.setAttribute("msgResult", getMessage("categoria.alta.ok", categoriaForm.getDescripcion()));
        httpServletRequest.setAttribute("urlOk", "categoria/grilla");
        return "generic/mensaje";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Short id, Model uiModel) {
        uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        uiModel.addAttribute("itemId", id);
        return "categoria/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        uiModel.addAttribute("categorias", categoriaService.obtenerCategorias(sortFieldName, sortOrder));
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
        httpServletRequest.setAttribute("msgTitle", getMessage("categoria.editar.title"));
        httpServletRequest.setAttribute("msgResult", getMessage("categoria.editar.ok", categoriaForm.getDescripcion()));
        httpServletRequest.setAttribute("urlOk", "categoria/grilla");
        return "generic/mensaje";
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Short id, Model uiModel) {
    	uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        return "categoria/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@Valid @ModelAttribute("categoriaForm") Categoria categoriaForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "categoria/borrar";
        }
        uiModel.asMap().clear();
        categoriaService.eliminarCategoria(categoriaForm);
        httpServletRequest.setAttribute("msgTitle", getMessage("categoria.borrar.title"));
        httpServletRequest.setAttribute("msgResult", getMessage("categoria.borrar.ok", categoriaForm.getDescripcion()));
        httpServletRequest.setAttribute("urlOk", "categoria/grilla");
        return "generic/mensaje";
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