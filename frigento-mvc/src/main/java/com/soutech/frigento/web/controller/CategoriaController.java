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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.service.CategoriaService;

@Controller
@RequestMapping(value="/categoria")
public class CategoriaController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    public CategoriaService categoriaService;

    @RequestMapping(params = "alta", produces = "text/html")
    public String createForm(Model uiModel) {
    	uiModel.addAttribute("categoriaForm", new Categoria());
        return "categoria/alta";
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid @ModelAttribute("categoriaForm") Categoria categoriaForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	uiModel.addAttribute("categoriaForm", new Categoria());
            return "categoria/alta";
        }
        uiModel.asMap().clear();
        categoriaService.saveCategoria(categoriaForm);
        httpServletRequest.setAttribute("msg", "Categoria dada de alta.");
        return "generic/mensaje";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Short id, Model uiModel) {
        uiModel.addAttribute("categoriaForm", categoriaService.obtenerCategoria(id));
        uiModel.addAttribute("itemId", id);
        return "categoria/grilla";
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