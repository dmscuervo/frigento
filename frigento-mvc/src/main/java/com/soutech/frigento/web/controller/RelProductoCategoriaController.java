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
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.web.validator.ErrorJSONHandler;
import com.soutech.frigento.web.validator.obj.RelProdCatErroresView;

@Controller
@RequestMapping(value="/relProdCat")
@SessionAttributes(names={"categoria", "productosCategoria", "codProductosMap", "codProductosMasterMap", "codCostoJson"})
public class RelProductoCategoriaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm"), false));   
    }
    
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    
    @Autowired
    private ErrorJSONHandler errorJSONHandler;
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;
    
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "alta", produces = "text/html", method = RequestMethod.GET)
    public String preAlta(Model uiModel) {
    	RelProductoCategoria rpc = new RelProductoCategoria();
    	Categoria cat = (Categoria)uiModel.asMap().get("categoria");
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
    		RelProdCatErroresView errorView = new RelProdCatErroresView();
    		String json = errorJSONHandler.getJSON(errorView, bindingResult);
    		uiModel.addAttribute("messageAjax", json);
        	return "ajax/value";
        }
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	relProdCatForm.getProducto().setDescripcion(codDescripcionMap.get(relProdCatForm.getProducto().getCodigo()));;
        List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
        lista.add(relProdCatForm);
        codDescripcionMap.remove(relProdCatForm.getProducto().getCodigo());
        return "relProdCat/grilla";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "editar", value="/{idx}", produces = "text/html", method = RequestMethod.GET)
    public String preEdit(@PathVariable("idx") Integer index, Model uiModel) {
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	RelProductoCategoria rpc = lista.get(index.intValue());
    	//Por comodidad uso el id para guardar el indice. Cuando tengo que persistir lo tengo que eliminar
    	rpc.setId(index);
    	uiModel.addAttribute("relProdCatForm", lista.get(index.intValue()));
        return "relProdCat/editar";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/editar", method = RequestMethod.POST, produces = "text/html")
    public String edit(@Valid @ModelAttribute("relProdCatForm") RelProductoCategoria relProdCatForm, BindingResult bindingResult, Model uiModel) {
    	if (bindingResult.hasErrors()) {
    		RelProdCatErroresView errorView = new RelProdCatErroresView();
    		String json = errorJSONHandler.getJSON(errorView, bindingResult);
    		uiModel.addAttribute("messageAjax", json);
        	return "ajax/value";
        }
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	Map<String, String> codProductosMasterMap = (Map<String, String>) uiModel.asMap().get("codProductosMasterMap");
    	relProdCatForm.getProducto().setDescripcion(codProductosMasterMap.get(relProdCatForm.getProducto().getCodigo()));;
        List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
        //Cambio el producto
        String codProdAnt = lista.get(relProdCatForm.getId()).getProducto().getCodigo();
        String codProd = relProdCatForm.getProducto().getCodigo();
        if(!codProd.equals(codProdAnt)){
        	//Actualizo
        	codDescripcionMap = new HashMap<String, String>(codProductosMasterMap);
        	for (RelProductoCategoria rpc : lista) {
				codDescripcionMap.remove(rpc.getProducto().getCodigo());
			}
        	uiModel.addAttribute("codProductosMap", codDescripcionMap);
        }
        lista.set(relProdCatForm.getId(), relProdCatForm);
        return "relProdCat/grilla";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "borrar", value="/{idx}", produces = "text/html", method = RequestMethod.GET)
    public String borrar(@PathVariable("idx") Integer index, Model uiModel) {
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	RelProductoCategoria rpc = lista.get(index.intValue());
    	//Vuelvo a incorporarlo como posible producto a seleccionar
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	codDescripcionMap.put(rpc.getProducto().getCodigo(), rpc.getProducto().getDescripcion());
    	lista.remove(index.intValue());
        return "relProdCat/grilla";
    }
 
    @RequestMapping(params = "listar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String listar(@PathVariable("id") Short idCat, Model uiModel) {
    	Categoria categoria = categoriaService.obtenerCategoria(idCat);
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
        uiModel.addAttribute("codProductosMasterMap", codDescripcionMap);
        uiModel.addAttribute("codCostoJson", json);
        uiModel.addAttribute("categoria", categoria);
        return "relProdCat/grilla";
    }
    
}