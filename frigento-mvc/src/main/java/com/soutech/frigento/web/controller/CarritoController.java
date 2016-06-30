package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.Promocion;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.validator.JSONHandler;

@Controller
@RequestMapping(value="/carrito")
@SessionAttributes(names={"carrito"})
@Secured({"ROLE_USER"})
public class CarritoController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
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
			uiModel.addAttribute("carrito", carrito);
		}
		String json = jSONHandler.getMensajeGenericoJSON(cantProd);
		uiModel.addAttribute("messageAjax", json);
		return "ajax/value";
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/ver", method = RequestMethod.GET, produces = "text/html")
    public String ver(Model uiModel) {
		Map<Integer, BigDecimal> carrito = (Map<Integer, BigDecimal>) uiModel.asMap().get("carrito");
		if(carrito == null || carrito.size() == 0){
			String json = jSONHandler.getMensajeGenericoJSON(getMessage("online.error.carrito.vacio"));
			uiModel.addAttribute("messageAjax", json);
			return "ajax/value";
		}
		
		Short idCat;
        try{
        	idCat = new Short(Parametros.getValor(Parametros.CATEGORIA_ID_VENTA_ONLINE));
        }catch(Exception e){
        	throw new RuntimeException(getMessage("mensaje.error.parametro", Parametros.CATEGORIA_ID_VENTA_ONLINE));
        }
		List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(new Date(), idCat);
		
		Venta venta = new Venta();
		venta.setFecha(new Date());
		venta.setVersion((short) 0);
		venta.setItems(new ArrayList<ItemVentaDTO>(carrito.keySet().size()));
		// Lo inicializo para que no falle la validación
		venta.setImporte(BigDecimal.ZERO);
		venta.setIncrementoIva(BigDecimal.ZERO);
		for (RelProductoCategoria rpc : rpcList) {
			if(carrito.containsKey(rpc.getProducto().getId())){
				ItemVentaDTO item;
				Producto producto = rpc.getProducto();
				Boolean esPromo = Boolean.FALSE;
				for (Promocion promo : producto.getPromociones()) {
					if(Utils.estaDentroDeRelacion(venta.getFecha(), promo.getFechaDesde(), promo.getFechaHasta())){
						item = new ItemVentaDTO();
						item.setProducto(producto);
						item.setCantidad(carrito.get(rpc.getProducto().getId()).floatValue());
						item.setRelProductoCategoriaId(rpc.getId());
						BigDecimal descuento = rpc.getProducto().getImporteVenta().multiply(promo.getDescuento()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
						item.setImporteVenta(rpc.getProducto().getImporteVenta().subtract(descuento));
						item.setPromocion(promo);
						venta.getItems().add(item);
						esPromo = Boolean.TRUE;
						break;
					}
				}
				if(!esPromo){
					item = new ItemVentaDTO();
					item.setProducto(producto);
					item.setCantidad(carrito.get(rpc.getProducto().getId()).floatValue());
					item.setRelProductoCategoriaId(rpc.getId());
					item.setImporteVenta(rpc.getProducto().getImporteVenta());
					venta.getItems().add(item);
				}
			}
		}

		uiModel.addAttribute("ventaForm", venta);
		return "carrito/alta";
    }
}