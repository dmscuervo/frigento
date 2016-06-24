package com.soutech.frigento.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Constantes;

@Controller
public class LoginController extends GenericController {

	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
    private ServletContext servletContext;
	@Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
	@Autowired
    public ProductoService productoService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home?logout";
    }
	
	@RequestMapping(value="/home")
    public String handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("inicio - Login controller");
        servletContext.setAttribute(Parametros.VERSION_APP, Constantes.VERSION_APP);
        Short idCat;
        try{
        	idCat = new Short(Parametros.getValor(Parametros.CATEGORIA_ID_VENTA_ONLINE));
        }catch(Exception e){
        	throw new RuntimeException(getMessage("mensaje.error.parametro", Parametros.CATEGORIA_ID_VENTA_ONLINE));
        }
		List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoria(idCat , Constantes.ESTADO_REL_VIGENTE, new String[]{"incremento * r.producto.costoActual"}, new String[]{"asc"});
		request.setAttribute("rpcListOnline", rpcList);
        logger.debug("fin - Login controller");
        return "main";
    }
	
	@RequestMapping(value = "/autenticationFailure", method = RequestMethod.GET)
    public String autenticationFailure(ModelMap model) {
        model.addAttribute("msg", getMessage("login.error.user.password", null));
        return "redirect:/login?error";
    }
	
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(HttpServletRequest request) {
        logger.info("Acceso denegado: URL="+request.getRequestURL());
        
        request.setAttribute("msgTitle", "Acceso Denegado");
        request.setAttribute("msgResult", "No dispone de permisos para utilizar esta función.<br>");
        
        return "generic/mensajeException";
    }
	
	@SuppressWarnings("unused")
	private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
	
	@RequestMapping(params = "imagen", value="/{id}", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody byte[] getImagen(@PathVariable("id") Integer id, Model uiModel, HttpServletResponse httpServletResponse) {
		byte[] imageBytes = productoService.obtenerProducto(id).getImagen();
		return imageBytes;
	}
}
