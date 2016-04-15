package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.dto.PrecioDTO;
import com.soutech.frigento.web.validator.FormatoDateTruncateValidator;

@Controller
@RequestMapping(value="/prodCosto")
@SessionAttributes(names={"productosCategoria"})
@Secured({"ROLE_ADMIN"})
public class ProductoCostoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    public static String BUSQUEDA_DEFAULT(Integer idProd){
		return "prodCosto/"+idProd+"?estado=A";
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(Date.class, new FormatoDateTruncateValidator(new SimpleDateFormat("dd/MM/yyyy HH:mm"), false));   
    }
    
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    private ProductoService productoService;
    

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/confirmar", method = RequestMethod.POST, produces = "text/html")
    public String alta(@ModelAttribute("prodCostoForm") PrecioDTO prodCostoForm, Model uiModel, HttpServletRequest httpServletRequest) {
    	List<RelProductoCategoria> relProdCats = (List<RelProductoCategoria>) uiModel.asMap().get("productosCategoria");
    	boolean huboCambios;
    	try{
    		huboCambios = productoService.asignarNuevoPrecio(relProdCats, prodCostoForm.getFechaDesde(), prodCostoForm.getCosto(), prodCostoForm.getIncrementos());
    	} catch (FechaDesdeException e) {
			String key = e.getKeyMessage();
			logger.info(getMessage(key, e.getArgs()));
			httpServletRequest.setAttribute("msgRespuesta", getMessage(key, e.getArgs()));
			return "prodCosto/grilla";
		}
    	String mensaje;
    	if(huboCambios){
    		mensaje = getMessage("prodCosto.confirmar.ok");
    	}else{
    		mensaje = getMessage("prodCosto.confirmar.sin.cambios");
    	}
    	return "redirect:/".concat(ProductoController.BUSQUEDA_DEFAULT).concat("&informar=".concat(mensaje));
    }
 
    @RequestMapping(params = "listar", value="/{id}", method = RequestMethod.GET, produces = "text/html")
    public String listar(@PathVariable("id") Integer idProd, @RequestParam(value = "estado", required = false) String estado, @RequestParam(value = "informar", required = false) String informar, Model uiModel) {
    	List<RelProductoCategoria> relProdCats = relProductoCategoriaService.obtenerCategoriasProducto(idProd, estado);
    	Producto producto = productoService.obtenerProducto(idProd);
    	uiModel.addAttribute("productosCategoria", relProdCats);
    	uiModel.addAttribute("producto", producto);
    	if(informar != null){
    		uiModel.addAttribute("informar", informar);
        }
    	
    	PrecioDTO precio = new PrecioDTO();
    	precio.setProdId(idProd);
    	precio.setFechaDesde(new Date());
    	precio.setCosto(producto.getCostoActual());
    	precio.setIncrementos(new BigDecimal[relProdCats.size()]);
    	precio.setPrecioCalculado(new BigDecimal[relProdCats.size()]);
    	//precio.setIncrementos(new ArrayList<BigDecimal>());;
    	for (int i = 0; i < relProdCats.size(); i++) {
    		RelProductoCategoria relProdCat = relProdCats.get(i); 
			precio.getIncrementos()[i] = relProdCat.getIncremento();
			BigDecimal factor = relProdCat.getIncremento().divide(new BigDecimal(100)).add(BigDecimal.ONE);
    		precio.getPrecioCalculado()[i] = producto.getCostoActual().multiply(factor).setScale(2, RoundingMode.HALF_UP);
		}
		uiModel.addAttribute("prodCostoForm", precio );
        return "prodCosto/grilla";
    }
    
}