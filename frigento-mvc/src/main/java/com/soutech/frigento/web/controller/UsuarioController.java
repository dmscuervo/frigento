package com.soutech.frigento.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.captcha.botdetect.web.servlet.Captcha;
import com.soutech.frigento.dto.LocalidadesDTO;
import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.exception.ConfirmacionRegistracionException;
import com.soutech.frigento.exception.EmailExistenteException;
import com.soutech.frigento.exception.UserNameExistenteException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.UsuarioService;
import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.util.SendMailSSL;
import com.soutech.frigento.web.google.GoogleDistance;
import com.soutech.frigento.web.google.GoogleGeocode;
import com.soutech.frigento.web.google.GoogleServicesHandler;
import com.soutech.frigento.web.validator.PasswordValidator;

@Controller
@RequestMapping(value="/usuario")
public class UsuarioController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    public UsuarioService usuarioService;
    @Autowired
    public CategoriaService categoriaService;
    @Autowired
    private GoogleServicesHandler googleServicesHandler;
    @Autowired
    private SendMailSSL sendMailSSL;

    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.addValidators(new PasswordValidator());
         binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @Secured({"ROLE_ADMIN"})    
    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(Model uiModel) {
    	Usuario usuario = new Usuario();
    	usuario.setHabilitado(Boolean.FALSE);
    	usuario.setEsAdmin(Boolean.FALSE);
		usuario.setUsername("prueba");
		usuario.setPassword("S1nCl4v3");
		usuario.setPasswordReingresada("S1nCl4v3");
		usuario.setLocalidad(LocalidadesDTO.getLocalidadDefault());
		usuario.setDistancia(0);
    	uiModel.addAttribute("usuarioForm", usuario);
    	
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	return "usuario/alta";
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
        	return "usuario/alta";
        }
        if(usuarioForm.getApellido() == null){
        	usuarioForm.setApellido("");
        }
        try {
			usuarioService.saveUsuario(usuarioForm);
		} catch (EmailExistenteException e) {
			//Esto esta mal
			BeanPropertyBindingResult error = new BeanPropertyBindingResult(usuarioForm, "usuarioForm");
			ObjectError oe = new ObjectError("email", "El correo ya existes");
			error.addError(oe );
			return "usuario/alta";
		}
        uiModel.asMap().clear();
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.alta.ok", usuarioForm.getUsername())));
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("usuarioForm", usuarioService.obtenerUsuario(id));
        uiModel.addAttribute("itemId", id);
        return "usuario/grilla";
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "estado", required = false) Boolean estado, @RequestParam(value = "sortFieldName", required = false) String[] sortFieldName, @RequestParam(value = "sortOrder", required = false) String[] sortOrder, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
    	uiModel.addAttribute("usuarios", usuarioService.obtenerUsuarios(estado, sortFieldName, sortOrder));
    	uiModel.addAttribute("estadoSel", estado);
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        return "usuario/grilla";
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("usuarioForm", usuarioService.obtenerUsuario(id));
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	return "usuario/editar";
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "usuario/editar";
        }
        uiModel.asMap().clear();
        usuarioService.actualizarUsuario(usuarioForm);
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.editar.ok", usuarioForm.getUsername())));
    }
    
    @Secured({"ROLE_ADMIN"})
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
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@ModelAttribute("usuarioForm") Usuario usuarioForm, HttpServletRequest httpServletRequest) {
        Usuario usuario = usuarioService.eliminarUsuario(usuarioForm.getId());
		return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.borrar.ok", usuario.getUsername())));
    }
    
    @Secured({"ROLE_ADMIN"})
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
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/activar", method = RequestMethod.POST, produces = "text/html")
    public String activar(@Valid @ModelAttribute("usuarioForm") Usuario usuarioForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	Usuario usuario = usuarioService.reactivarUsuario(usuarioForm.getId());
        return "redirect:/".concat(BUSQUEDA_DEFAULT).concat("&informar=".concat(getMessage("usuario.activar.ok", usuario.getUsername())));
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(params = "detalle", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String detalle(@PathVariable("id") Integer idUsuario, Model uiModel) {
        uiModel.addAttribute("usuario", usuarioService.obtenerUsuario(idUsuario));
        return "usuario/detalle";
    }
    
    @RequestMapping(params = "registrar", produces = "text/html")
    public String preRegistrar(Model uiModel) {
    	Usuario usuario = new Usuario();
    	usuario.setHabilitado(Boolean.FALSE);
		usuario.setEsAdmin(Boolean.FALSE);
		usuario.setLocalidad(LocalidadesDTO.getLocalidadDefault());
		usuario.setDistancia(0);
		Categoria categoria = new Categoria();
		categoria.setId(new Short(Parametros.getValor(Parametros.CATEGORIA_ID_VENTA_ONLINE)));
		usuario.setCategoriaProducto(categoria);
    	uiModel.addAttribute("registracionForm", usuario);
    	
    	return "usuario/registrar";
    }
    
    @RequestMapping(value = "/registrar", method = RequestMethod.POST, produces = "text/html")
    public String registrar(@Valid @ModelAttribute("registracionForm") Usuario registracionForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.debug("Comienza registración");
        if (bindingResult.hasErrors()) {
        	return "usuario/registrarForm";
        }
        
        Captcha captcha = Captcha.load(httpServletRequest, "exampleCaptcha");
        boolean isHuman = captcha.validate(httpServletRequest.getParameter("captchaCode"));
        if(!isHuman){
        	//bindingResult.rejectValue("captchaCode", "registracion.error.captchaCode");
        	httpServletRequest.setAttribute("captchaCodeError", getMessage("registracion.error.captchaCode"));
        	return "usuario/registrar";
        }
        
        Boolean usarGoogle = Boolean.parseBoolean(Parametros.getValor(Parametros.USAR_SERVICIOS_GOOGLE));
        if(usarGoogle){
	        logger.info("Comienzo geocodificacion");
	        //Obtengo geolocation
	        String url = googleServicesHandler.urlGeocode.replace("{0}", registracionForm.getCalle().replaceAll(" ", "+")).replace("{1}", registracionForm.getAltura().toString());
	        GoogleGeocode geocode = null;
			try {
				geocode = googleServicesHandler.invocarURL(url, GoogleGeocode.class);
			} catch (Exception e) {
				logger.info("Error al invocar el servicio google geocode.");
				logger.error(PrinterStack.getStackTraceAsString(e));
				bindingResult.rejectValue("calle", "registracion.error.localidad");
	        	return "usuario/registrarForm";
			}
			
			logger.info("Chequeo si el domicilio es unico dentro de CABA");
			Boolean esUnica = googleServicesHandler.esDirecciónUnica(geocode);
			if(!esUnica){
				bindingResult.rejectValue("calle", "registracion.error.localidad.ambigua");
	        	return "usuario/registrarForm";
			}
			
			logger.info("Chequeo si el domicilio corresponde a CABA");
	        Boolean esCABA = googleServicesHandler.esCABA(geocode);
	        if(!esCABA){
	        	bindingResult.rejectValue("calle", "registracion.error.localidad");
	        	return "usuario/registrarForm";
	        }
	        String localidad = googleServicesHandler.getLocalidad(geocode);
	        String urlDistance = googleServicesHandler.urlDistance.replace("{0}", registracionForm.getCalle().replaceAll(" ", "+")).replace("{1}", registracionForm.getAltura().toString());
	        GoogleDistance distance = null;
			try {
				logger.info("Obtengo distancia en metros");
				distance = googleServicesHandler.invocarURL(urlDistance, GoogleDistance.class);
			} catch (Exception e) {
				logger.info("Error al invocar el servicio google geocode.");
				logger.error(PrinterStack.getStackTraceAsString(e));
				bindingResult.rejectValue("calle", "registracion.error.localidad");
	        	return "usuario/registrarForm";
			}
	        Integer metros = googleServicesHandler.getDistancia(distance);
	        
	        registracionForm.setLocalidad(LocalidadesDTO.getLocalidad(localidad));
	        registracionForm.setDistancia(metros);
        }else{
        	logger.info("Geocodificacion desactivada");
        	registracionForm.setLocalidad(LocalidadesDTO.getLocalidadDefault());
	        registracionForm.setDistancia(0);
        }
        logger.info("Persisto usuario");
        try {
			usuarioService.registrarUsuario(registracionForm);
		} catch (UserNameExistenteException e) {
			bindingResult.rejectValue("username", e.getKeyMessage());
        	return "usuario/registrarForm";
		} catch (EmailExistenteException e) {
			bindingResult.rejectValue("email", e.getKeyMessage());
        	return "usuario/registrarForm";
		}
        
        try {
        	//http://localhost:8081/frigento-mvc/home
			sendMailSSL.enviarCorreoRegistracion(registracionForm, "http", "localhost", "8081", "frigento-mvc");
		} catch (Exception e) {
			String mensaje = "No pudo enviarse el correo de registración a " + registracionForm.getEmail();
			logger.error(mensaje);
			sendMailSSL.enviarCorreoProblema(mensaje, e);
		}
        
        uiModel.asMap().clear();
        logger.debug("Fin de registración");
        uiModel.addAttribute("informar", getMessage("usuario.registracion.ok"));
        return "usuario/registrarResultado";
    }
    
    @RequestMapping(params = "confirmarReg", value="/{username}/{valid}", method = RequestMethod.GET, produces = "text/html")
    public String confirmarRegistracion(@PathVariable("username") String username, @PathVariable("valid") String validator, HttpServletRequest httpServletRequest) {
    	
    	String mensaje;
    	try {
			Usuario usuario = usuarioService.confirmarRegistracion(username, validator);
			mensaje = getMessage("usuario.registracion.confirmacion.ok", usuario.getNombre());
		} catch (ConfirmacionRegistracionException e) {
			logger.error(getMessage(e.getMessage()));
			mensaje = getMessage(e.getMessage());
		}
    	httpServletRequest.setAttribute("informar", mensaje);
        return "usuario/registrarConfirmacion";
    }
}