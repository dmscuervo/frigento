package com.soutech.frigento.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Promocion;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.PromocionService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.validator.JSONHandler;

@Controller
@RequestMapping(value="/promocion")
@SessionAttributes(names={"estadoSel"})
@Secured({"ROLE_ADMIN"})
public class PromocionController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static String BUSQUEDA_DEFAULT(String vigente){
    	return "promocion?vigente="+vigente+"&sortFieldName=fechaDesde&sortOrder=desc";
    }
    //public static final String BUSQUEDA_DEFAULT = "promocion?vigente=true&sortFieldName=fechaDesde&sortOrder=desc";
    
    @Autowired
    public PromocionService promocionService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    private JSONHandler jSONHandler;
    
    @RequestMapping(params = "alta", produces = "text/html")
    public String preAlta(HttpServletRequest httpServletRequest) {
    	List<Categoria> categorias = categoriaService.obtenerCategorias("descripcion", "asc");
    	httpServletRequest.setAttribute("categoriaList", categorias);
        return "promocion/alta";
    }
    
    @RequestMapping(value = "/preAltaContenido", produces = "text/html")
    public String preAltaContenido(@RequestParam(value = "idCat", required = false) Short idCategoria, Model uiModel) {
    	Promocion promo = new Promocion();
    	promo.setFechaDesde(new Date());
    	List<RelProductoCategoria> relProdCatList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(promo.getFechaDesde(), idCategoria);
    	if(relProdCatList.isEmpty()){
    		String json = jSONHandler.getMensajeGenericoJSON(getMessage("login.error.user.password"));
    		uiModel.addAttribute("messageAjax", json);
    		return "ajax/value";
    	}
    	
    	Map<Integer, BigDecimal> mapa = new HashMap<Integer, BigDecimal>();
    	for (RelProductoCategoria rpc : relProdCatList) {
    		mapa.put(rpc.getId(), rpc.getProducto().getImporteVenta());
		}
    	
    	String json;
    	ObjectMapper mapper = new ObjectMapper();
        try {
			json = mapper.writeValueAsString(mapa);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al convertir el mapa codigo-costo de productos.");
		}
    	
    	uiModel.addAttribute("idRpcPrecioVtaJson", json);
    	uiModel.addAttribute("relProdCatList", relProdCatList);
    	uiModel.addAttribute("promoForm", promo);
    	return "promocion/altaContenido";
    }
    
    @RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("promoForm") Promocion promoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        uiModel.asMap().clear();
        promocionService.savePromocion(promoForm);
        String estadoVisualizado = (String) uiModel.asMap().get("estadoSel");
        if(estadoVisualizado == null){
        	estadoVisualizado = "true";
        }
        return "redirect:/".concat(BUSQUEDA_DEFAULT(estadoVisualizado)).concat("&informar=".concat(getMessage("promocion.alta.ok", promoForm.getId())));
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String get(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("promoForm", promocionService.obtenerPromocion(id));
        uiModel.addAttribute("itemId", id);
        return "promocion/grilla";
    }
    
    @RequestMapping(produces = "text/html")
    public String listar(@RequestParam(value = "vigente", required = false) String estado, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
    	Boolean vigente = Boolean.FALSE;
    	if(estado == null){
    		estado = "true";
    	}
    	if(estado.equals("")){
    		vigente = null;
    	}else if(estado.toLowerCase().equals("true")){
    		vigente = Boolean.TRUE;
    	}
        uiModel.addAttribute("promociones", promocionService.obtenerPromociones(vigente, sortFieldName, sortOrder));
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        uiModel.addAttribute("estadoSel", estado);
        return "promocion/grilla";
    }
    
    @RequestMapping(params = "editar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preEdit(@PathVariable("id") Integer id, Model uiModel) {
    	Promocion promo = promocionService.obtenerPromocion(id);
    	List<RelProductoCategoria> relProdCatList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(promo.getFechaDesde(), promo.getRelProdCat().getCategoria().getId());
    	if(relProdCatList.isEmpty()){
    		String json = jSONHandler.getMensajeGenericoJSON(getMessage("login.error.user.password"));
    		uiModel.addAttribute("messageAjax", json);
    		return "ajax/value";
    	}
    	
    	Map<Integer, BigDecimal> mapa = new HashMap<Integer, BigDecimal>();
    	for (RelProductoCategoria rpc : relProdCatList) {
    		mapa.put(rpc.getId(), rpc.getProducto().getImporteVenta());
		}
    	String json;
    	ObjectMapper mapper = new ObjectMapper();
        try {
			json = mapper.writeValueAsString(mapa);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al convertir el mapa codigo-costo de productos.");
		}
    	
    	uiModel.addAttribute("idRpcPrecioVtaJson", json);
    	uiModel.addAttribute("promoForm", promo);
        return "promocion/editar";
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("promoForm") Promocion promoForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	return "promocion/editar";
        }
        uiModel.asMap().clear();
        promocionService.actualizarPromocion(promoForm);
        String estadoVisualizado = (String) uiModel.asMap().get("estadoSel");
        return "redirect:/".concat(BUSQUEDA_DEFAULT(estadoVisualizado)).concat("&informar=".concat(getMessage("promocion.editar.ok", promoForm.getId())));
    }
    
    @RequestMapping(params = "borrar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String preDelete(@PathVariable("id") Integer id, Model uiModel) {
    	uiModel.addAttribute("promoForm", promocionService.obtenerPromocion(id));
        return "promocion/borrar";
    }
    
    @RequestMapping(value = "/borrar", method = RequestMethod.POST, produces = "text/html")
    public String delete(@ModelAttribute("promoForm") Promocion promoForm, HttpServletRequest httpServletRequest) {
    	String estadoVisualizado = (String) httpServletRequest.getSession().getAttribute("estadoSel");
        try {
			promocionService.eliminarPromocion(promoForm);
		} catch (EntityExistException e) {
			String key = "EntityExist.promoForm."+e.getField();
			httpServletRequest.setAttribute("msgError", getMessage(key, e.getArgs()));
			logger.info(getMessage(key, e.getArgs()));
			return "redirect:/".concat(BUSQUEDA_DEFAULT(estadoVisualizado)).concat("&informar=".concat(getMessage(key, e.getArgs())));
		}
        return "redirect:/".concat(BUSQUEDA_DEFAULT(estadoVisualizado)).concat("&informar=".concat(getMessage("promocion.borrar.ok", promoForm.getId())));
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