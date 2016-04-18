package com.soutech.frigento.web.controller;

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

import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.UsuarioService;
import com.soutech.frigento.util.Encriptador;

@Controller
@RequestMapping(value="/usuario")
@Secured({"ROLE_ADMIN"})
public class UsuarioController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    public UsuarioService usuarioService;
    @Autowired
    public CategoriaService categoriaService;

    
    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	Usuario usuario = new Usuario();
    	usuario.setHabilitado(Boolean.FALSE);
		usuario.setUsername("");
		usuario.setPassword(Encriptador.encriptarPassword("S1nCl4v3"));
		usuario.setEsAdmin(Boolean.FALSE);
    	uiModel.addAttribute("usuarioForm", usuario);
    	
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	return "usuario/alta";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "usuario/alta";
        }
        uiModel.asMap().clear();
        usuarioService.saveUsuario(usuarioForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.alta.ok", usuarioForm.getUsername())));
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("usuarioForm", usuarioService.obtenerUsuario(id));
        uiModel.addAttribute("itemId", id);
        return "usuario/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "estado", required = false) Boolean estado, @RequestParam(value = "sortFieldName", required = false) String[] sortFieldName, @RequestParam(value = "sortOrder", required = false) String[] sortOrder, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
    	uiModel.addAttribute("usuarios", usuarioService.obtenerUsuarios(estado, sortFieldName, sortOrder));
    	uiModel.addAttribute("estadoSel", estado);
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        return "usuario/grilla";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("usuarioForm", usuarioService.obtenerUsuario(id));
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	return "usuario/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "usuario/editar";
        }
        uiModel.asMap().clear();
        usuarioService.actualizarUsuario(usuarioForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.editar.ok", usuarioForm.getUsername())));
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Integer id, Model uiModel) {
    	Usuario usuario = usuarioService.obtenerUsuario(id);
    	if(!usuario.getHabilitado()){
    		uiModel.addAttribute("informar", getMessage("usuario.borrar.estado.error"));
        	return "pedido/grilla";
        }
    	uiModel.addAttribute("usuarioForm", usuario);
        return "usuario/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@ModelAttribute("usuarioForm") Usuario usuarioForm, HttpServletRequest httpServletRequest) {
        Usuario usuario = usuarioService.eliminarUsuario(usuarioForm.getId());
		return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.borrar.ok", usuario.getUsername())));
    }
    
    @RequestMapping(params = "activar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preActivar(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    	Usuario usuario = usuarioService.obtenerUsuario(id);
    	if(usuario.getHabilitado()){
        	httpServletRequest.setAttribute("informar", getMessage("usuario.activar.estado.error"));
        	return "pedido/grilla";
        }
    	uiModel.addAttribute("usuarioForm", usuario);
        return "usuario/activar";
    }
    
    @RequestMapping(value = "/activar", method = RequestMethod.POST, produces = "text/html")
    public String activar(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	Usuario usuario = usuarioService.reactivarUsuario(usuarioForm.getId());
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.activar.ok", usuario.getUsername())));
    }
    
    @RequestMapping(params = "detalle", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String detalle(@PathVariable("id") Integer idUsuario, Model uiModel) {
        uiModel.addAttribute("usuario", usuarioService.obtenerUsuario(idUsuario));
        return "usuario/detalle";
    }
    
    
}