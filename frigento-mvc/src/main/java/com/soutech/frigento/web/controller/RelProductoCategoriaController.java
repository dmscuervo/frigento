package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soutech.frigento.model.Categoria;
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
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm"), false));   
    }
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
    
    @Autowired
    public ProductoService productoService;

    @SuppressWarnings("unchecked")
	@RequestMapping(params = "alta", value="/{id}", produces = "text/html", method = RequestMethod.GET)
    public String preAlta(@PathVariable("id") Short idCat, Model uiModel) {
    	RelProductoCategoria rpc = new RelProductoCategoria();
    	Categoria cat = new Categoria();
    	cat.setId(idCat);
    	rpc.setCategoria(cat);
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	if(lista != null && !lista.isEmpty()){
    		rpc.setFechaDesde(lista.get(0).getFechaDesde());
    	}else{
    		rpc.setFechaDesde(new Date());
    	}
    	uiModel.addAttribute("relProdCatForm", rpc);
        return "relProdCat/alta";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("relProdCatForm") RelProductoCategoria relProdCatForm, BindingResult bindingResult, Model uiModel) {
    	if (bindingResult.hasErrors()) {
    		uiModel.addAttribute("messageAjax", "ERROR");
        	return "ajax/value";
        }
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	relProdCatForm.getProducto().setDescripcion(codDescripcionMap.get(relProdCatForm.getProducto().getCodigo()));;
        List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
        lista.add(relProdCatForm);
        uiModel.addAttribute("idCat", relProdCatForm.getCategoria().getId());
        codDescripcionMap.remove(relProdCatForm.getProducto().getCodigo());
        return "relProdCat/grilla";
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
        uiModel.addAttribute("idCat", idCat);
        return "relProdCat/grilla";
    }
}