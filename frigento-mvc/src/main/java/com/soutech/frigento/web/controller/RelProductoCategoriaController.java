package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Constantes;

@Controller
@RequestMapping(value="/relProdCat")
@SessionAttributes(names={"productosCategoria", "codProductosMap", "codCostoJson"})
public class RelProductoCategoriaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
    
    @Autowired
    public ProductoService productoService;

    @RequestMapping(params = "alta", produces = "text/html", method = RequestMethod.GET)
    public String preAlta(Model uiModel) {
    	uiModel.addAttribute("relProdCatForm", new RelProductoCategoria());
        return "relProdCat/alta";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@ModelAttribute("relProdCatForm") RelProductoCategoria relProdCatForm, Model uiModel, HttpServletRequest httpServletRequest) {
        List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
        lista.add(relProdCatForm);
        return "relProdCat/contenido";
    }
 
    @RequestMapping(params = "listar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String listar(@PathVariable("id") Short idCat, Model uiModel) {
        uiModel.addAttribute("productosCategoria", relProductoCategoriaService.obtenerProductosCategoria(idCat));
        List<Producto> productos = productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion", "asc");
        Map<String, String> codDescripcionMap = new HashMap<String, String>();
        for (Producto producto : productos) {
			codDescripcionMap.put(producto.getCodigo(), producto.getCodigo().concat(" - ").concat(producto.getDescripcion()));
		}
        Map<String, BigDecimal> codCostoMap = new HashMap<String, BigDecimal>();
        for (Producto producto : productos) {
        	codCostoMap.put(producto.getCodigo(), producto.getCostoActual());
		}
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
			json = mapper.writeValueAsString(codCostoMap);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al convertir el mapa codigo-costo de productos.");
		}
        uiModel.addAttribute("codProductosMap", codDescripcionMap);
        uiModel.addAttribute("codCostoJson", json);
        return "relProdCat/grilla";
    }
}