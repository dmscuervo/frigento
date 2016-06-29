package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.web.validator.JSONHandler;

@Controller
@RequestMapping(value="/carrito")
@SessionAttributes(names={"carrito"})
@Secured({"ROLE_USER"})
public class CarritoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    private JSONHandler jSONHandler;
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/agregar", method = RequestMethod.POST, produces = "text/html")
    public String agregar(@RequestParam(value = "idProd", required = true) Integer idProducto, @RequestParam(value = "cantidad", required = true) BigDecimal cantidad, Model uiModel) {
		Map<Integer, BigDecimal> carrito = (Map<Integer, BigDecimal>) uiModel.asMap().get("carrito");
		if(carrito == null){
			carrito = new HashMap<Integer, BigDecimal>();
			uiModel.addAttribute("carrito", carrito);
			carrito.put(idProducto, cantidad);
		}else{
			if(carrito.containsKey(idProducto)){
				carrito.put(idProducto, carrito.get(idProducto).add(cantidad).setScale(2, RoundingMode.HALF_UP));
			}else{
				carrito.put(idProducto, cantidad.setScale(2, RoundingMode.HALF_UP));
			}
		}
		String json = jSONHandler.getMensajeGenericoJSON(String.valueOf(carrito.size()));
		uiModel.addAttribute("messageAjax", json);
		return "ajax/value";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/vaciar", method = RequestMethod.GET, produces = "text/html")
    public String vaciar(Model uiModel) {
		Map<Integer, BigDecimal> carrito = (Map<Integer, BigDecimal>) uiModel.asMap().get("carrito");
		String cantProd = "0";
		if(carrito != null){
			carrito = new HashMap<Integer, BigDecimal>();
		}
		String json = jSONHandler.getMensajeGenericoJSON(cantProd);
		uiModel.addAttribute("messageAjax", json);
		return "ajax/value";
    }
}