package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.list.TreeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.ProductoCostoService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.validator.ErrorJSONHandler;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;
import com.soutech.frigento.web.validator.obj.RelProdCatErroresView;

@Controller
@RequestMapping(value="/relProdCat")
@SessionAttributes(names={"categoria", "productosCategoria", "codProductosMap", "codProductosMasterMap", "codCostoJson"})
@Secured({"ROLE_ADMIN"})
public class RelProductoCategoriaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    private final SimpleDateFormat sdf_desde_hasta = new SimpleDateFormat(Constantes.FORMATO_FECHA_DESDE_HASTA); 
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new FormatoDateTruncateValidator(new SimpleDateFormat("dd/MM/yyyy HH:mm"), false));   
    }
    
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    
    @Autowired
    private ProductoCostoService productoCostoService;
    
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
    	//En caso de haber realizado un alta nuevo, vuelvo a dejar la misma fecha elegida
    	rpc.setFechaDesde(new Date());
    	if(lista != null && !lista.isEmpty()){
    		for (RelProductoCategoria relProdCat : lista) {
    			if(relProdCat.getId() == null){
    				rpc.setFechaDesde(relProdCat.getFechaDesde());
    			}
    		}
    	}
    	uiModel.addAttribute("relProdCatForm", rpc);
        return "relProdCat/alta";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/alta", method = RequestMethod.POST, produces = "text/html")
    public String alta(@Valid @ModelAttribute("relProdCatForm") RelProductoCategoria relProdCatForm, BindingResult bindingResult, Model uiModel) {
    	if (bindingResult.hasErrors()) {
    		RelProdCatErroresView errorView = new RelProdCatErroresView();
    		ProductoCosto pc = null;
    		if(relProdCatForm.getProducto().getId() != null){
    			pc = productoCostoService.obtenerActual(relProdCatForm.getProducto().getId());
    		}
    		if(relProdCatForm.getFechaDesde() != null && pc != null && relProdCatForm.getFechaDesde().before(pc.getFechaDesde())){
    			errorView.setFechaDesde(getMessage("relProdCatForm.fechaDesde.anterior", sdf_desde_hasta.format(pc.getFechaDesde())));
    		}
    		String json = errorJSONHandler.getJSON(errorView, bindingResult);
    		uiModel.addAttribute("messageAjax", json);
        	return "ajax/value";
        }
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	relProdCatForm.getProducto().setDescripcion(codDescripcionMap.get(relProdCatForm.getProducto().getCodigo()));;
        List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
        //Control de fecha y de relaciones actuales
        Date fechaDesdeMinPosible = null;
        RelProductoCategoria relActual = null;
        for (RelProductoCategoria rpc : lista) {
			if(rpc.getProducto().getCodigo().equals(relProdCatForm.getProducto().getCodigo())){
				//Control de fecha
				if(rpc.getFechaHasta() == null){//Registro actual vigente. La nueva fecha no puede ser menor que la fechaDesde del actual
					if(fechaDesdeMinPosible == null || rpc.getFechaDesde().equals(fechaDesdeMinPosible) || rpc.getFechaDesde().after(fechaDesdeMinPosible)){
						fechaDesdeMinPosible = rpc.getFechaDesde();
						relActual = rpc;
					}
				}else{
					if(fechaDesdeMinPosible == null || rpc.getFechaHasta().after(fechaDesdeMinPosible)){
						fechaDesdeMinPosible = rpc.getFechaHasta();
					}
				}
			}
		}
        if(fechaDesdeMinPosible != null && relProdCatForm.getFechaDesde().before(fechaDesdeMinPosible)){
        	uiModel.addAttribute("msgRespuesta", getMessage("relProdCat.fecha.desde.min.venta", new Object[]{relProdCatForm.getProducto().getCodigo(), Utils.formatDate(fechaDesdeMinPosible, Utils.SDF_DDMMYYYY_HHMM)}));
			return "relProdCat/grilla";
        }
        //Habia una relacion vigente. La cierro
        if(relActual != null){
        	relActual.setFechaHasta(relProdCatForm.getFechaDesde());
        }
        //Fin Control de fecha
        lista.add(relProdCatForm);
        controlEditable(lista);
        codDescripcionMap.remove(relProdCatForm.getProducto().getCodigo());
        return "relProdCat/grilla";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "editar", value="/{idx}", produces = "text/html", method = RequestMethod.GET)
    public String preEdit(@PathVariable("idx") Integer index, Model uiModel) {
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	RelProductoCategoria rpc = lista.get(index.intValue());
    	rpc.setIndiceLista(index);
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
        String codProdAnt = lista.get(relProdCatForm.getIndiceLista()).getProducto().getCodigo();
        String codProd = relProdCatForm.getProducto().getCodigo();
        if(!codProd.equals(codProdAnt)){
        	//Actualizo
        	codDescripcionMap = new HashMap<String, String>(codProductosMasterMap);
        	for (RelProductoCategoria rpc : lista) {
				codDescripcionMap.remove(rpc.getProducto().getCodigo());
			}
        	uiModel.addAttribute("codProductosMap", codDescripcionMap);
        }
        //Control de fecha y de relaciones actuales
        Date fechaDesdeMinPosible = null;
        RelProductoCategoria relActual = null;
        for (RelProductoCategoria rpc : lista) {
			if(rpc.getProducto().getCodigo().equals(relProdCatForm.getProducto().getCodigo())){
				//Control de fecha
				if(rpc.getFechaHasta() == null){//Registro actual vigente. La nueva fecha no puede ser menor que la fechaDesde del actual
					if(fechaDesdeMinPosible == null || rpc.getFechaDesde().equals(fechaDesdeMinPosible) || rpc.getFechaDesde().after(fechaDesdeMinPosible)){
						fechaDesdeMinPosible = rpc.getFechaDesde();
						relActual = rpc;
					}
				}else{
					if(fechaDesdeMinPosible == null || rpc.getFechaHasta().after(fechaDesdeMinPosible)){
						fechaDesdeMinPosible = rpc.getFechaHasta();
					}
				}
			}
		}
        if(fechaDesdeMinPosible != null && relProdCatForm.getFechaDesde().before(fechaDesdeMinPosible)){
        	uiModel.addAttribute("msgRespuesta", getMessage("relProdCat.fecha.desde.min.venta", new Object[]{relProdCatForm.getProducto().getCodigo(), Utils.formatDate(fechaDesdeMinPosible, Utils.SDF_DDMMYYYY_HHMM)}));
			return "relProdCat/grilla";
        }
        //Habia una relacion vigente. La cierro
        if(relActual != null){
        	relActual.setFechaHasta(relProdCatForm.getFechaDesde());
        }
        //Fin Control de fecha
        lista.set(relProdCatForm.getIndiceLista(), relProdCatForm);
        return "relProdCat/grilla";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "borrar", value="/{idx}", produces = "text/html", method = RequestMethod.POST)
    public String borrar(@PathVariable("idx") Integer index, Model uiModel) {
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	RelProductoCategoria rpc = lista.get(index.intValue());
    	//Vuelvo a incorporarlo como posible producto a seleccionar
    	Map<String, String> codDescripcionMap = (Map<String, String>) uiModel.asMap().get("codProductosMap");
    	codDescripcionMap.put(rpc.getProducto().getCodigo(), rpc.getProducto().getDescripcion());
    	lista.remove(index.intValue());
    	controlEditable(lista);
        return "relProdCat/grilla";
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(params = "confirmar", produces = "text/html", method = RequestMethod.GET)
    public String confirmar(Model uiModel, HttpServletRequest httpServletRequest) {
    	List<RelProductoCategoria> lista = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	Categoria categoria = (Categoria)uiModel.asMap().get("categoria");
    	try{
			relProductoCategoriaService.asignarProductos(categoria, lista);
    	} catch (FechaDesdeException e) {
			String key = e.getKeyMessage();
			logger.info(getMessage(key, e.getArgs()));
			httpServletRequest.setAttribute("msgRespuesta", getMessage(key, e.getArgs()));
			return "relProdCat/grilla";
		}
    	httpServletRequest.setAttribute("msgRespuesta", getMessage("relProdCat.confirmar.ok"));
    	return "relProdCat/grilla";
    }
 
    @SuppressWarnings("unchecked")
	@RequestMapping(params = "listar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String listar(@PathVariable("id") Short idCat, @RequestParam(value = "estado", required = true) String estado, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
    	Categoria categoria = categoriaService.obtenerCategoria(idCat);
    	String estadoBusqueda = null;
    	if(!estado.equals("")){
    		estadoBusqueda = estado;
    	}
        List<RelProductoCategoria> relProdCats = new TreeList(relProductoCategoriaService.obtenerProductosCategoria(idCat, estadoBusqueda, new String[]{"producto.id", "fechaDesde"}, new String[]{"asc", "asc"}));
		uiModel.addAttribute("productosCategoria", relProdCats);
        List<Producto> productos = productoService.obtenerProductos(Constantes.ESTADO_ACTIVO, "descripcion", "asc");
        Map<String, String> codDescripcionMap = new TreeMap<String, String>();
        List<Integer> productosYaRelacionados = new ArrayList<Integer>();
        Integer prodIdTemp = null;
        for (int i = 0; i < relProdCats.size(); i++) {
        	RelProductoCategoria relProdCat = relProdCats.get(i);
			productosYaRelacionados.add(relProdCat.getProducto().getId());
			//Calculo precio
        	//BigDecimal costoActual = relProdCat.getProducto().getCostoActual();
        	ProductoCosto prodCosto = productoCostoService.obtenerProductoCosto(relProdCat.getProducto().getId(), relProdCat.getFechaDesde());
        	BigDecimal costoEnLaFecha = prodCosto.getCosto();
        	BigDecimal factor = relProdCat.getIncremento().divide(new BigDecimal(100)).add(BigDecimal.ONE);
        	BigDecimal precioCalc = costoEnLaFecha.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        	relProdCat.setPrecioCalculado(precioCalc);
        	
        	if(!relProdCat.getProducto().getId().equals(prodIdTemp)){
        		if(i > 0){
        			RelProductoCategoria rpcAnterior = relProdCats.get(i-1);
        			if(rpcAnterior.getFechaHasta() != null){
        				relProdCat.setEsEditable(Boolean.TRUE);
        			}
        		}
        		prodIdTemp = relProdCat.getProducto().getId();
        	}
        }
        for (Producto producto : productos) {
        	//if(!productosYaRelacionados.contains(producto.getId())){
        		codDescripcionMap.put(producto.getCodigo(), producto.getCodigo().concat(" - ").concat(producto.getDescripcion()));
        	//}
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
        uiModel.addAttribute("estadoSel", estado);
        if(informar != null){
        	uiModel.addAttribute("informar", informar);
        }
        return "relProdCat/grilla";
    }
    
    /**
     * Establece que un item sera editable.
     * Si el ultimo item de un producto tiene fechaHasta no nula, entonces es editable
     * Se requiere que relProdCats este ordenada por producto y fechaDesde
     * @param relProdCats
     */
    private void controlEditable(List<RelProductoCategoria> relProdCats){
    	Collections.sort(relProdCats); 
    	String prodCodigoTemp = null;
        for (int i = 0; i < relProdCats.size(); i++) {
        	RelProductoCategoria relProdCat = relProdCats.get(i);
        	relProdCat.setEsEditable(Boolean.FALSE);
			if(!relProdCat.getProducto().getCodigo().equals(prodCodigoTemp)){
        		if(i > 0){
        			RelProductoCategoria rpcAnterior = relProdCats.get(i-1);
        			if(rpcAnterior.getFechaHasta() != null){
        				rpcAnterior.setEsEditable(Boolean.TRUE);
        			}
        		}
        		prodCodigoTemp = relProdCat.getProducto().getCodigo();
        	}
        }
        RelProductoCategoria rpcAnterior = relProdCats.get(relProdCats.size()-1);
		if(rpcAnterior.getFechaHasta() != null){
			rpcAnterior.setEsEditable(Boolean.TRUE);
		}
    }
}