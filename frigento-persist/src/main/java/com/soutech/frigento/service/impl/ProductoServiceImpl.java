package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlPedidoVsCostoProducto;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.Utils;

@Service
public class ProductoServiceImpl implements ProductoService {	

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
    ProductoDao productoDao;
	@Autowired
    ProductoCostoDao productoCostoDao;
	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;
	@Autowired
	RelPedidoProductoDao relPedidoProductoDao;
	@Autowired
    ControlStockProducto controlStockProducto;

	@Override
	@Transactional
	public void saveProducto(Producto producto) throws EntityExistException {
		Producto prod = productoDao.findByCodigo(producto.getCodigo());
		if(prod != null){
			throw new EntityExistException("codigo");
		}
		productoDao.save(producto);
		ProductoCosto rpc = new ProductoCosto();
		rpc.setCosto(producto.getCostoActual());
		rpc.setFechaDesde(producto.getFechaAlta());
		rpc.setProducto(producto);
		productoCostoDao.save(rpc);
	}

	@Override
	public Producto obtenerProducto(Integer id) {
		return productoDao.findById(id);
	}

	@Override
	public List<Producto> obtenerProductos() {
		return productoDao.findAll();
	}

	@Override
	public List<Producto> obtenerProductos(String estado, String sortFieldName, String sortOrder) {
		return productoDao.findAll(estado, sortFieldName, sortOrder);
	}

	@Override
	@Transactional
	public void actualizarProducto(Producto producto) throws StockAlteradoException, FechaDesdeException {
		//Chequeo si cambio la fecha de alta del producto. En dicho caso, veo si es aceptable el cambio de fecha
		//Para eso controlo que no se superponga con un rango de fechas existente en productoCosto
		Producto productoActual = productoDao.load(producto.getId());
		String condicion = "";
		Date maxFHastaPosible = null;
		if(productoActual.getFechaAlta().before(producto.getFechaAlta())){
			Date maxFHastaPC = productoCostoDao.getMinFechaHasta(producto.getId());
			Date maxFHastaRPC = relProductoCategoriaDao.findMinDate(productoActual.getId());
			Date maxFHastaPed = relPedidoProductoDao.findMinFechaPedidoNoAnulado(productoActual.getId());
			maxFHastaPosible = Utils.dameFechaMasAnitgua(maxFHastaPC, maxFHastaRPC, maxFHastaPed);
			condicion = "mayor";
		}
		
		if(maxFHastaPosible != null){
			throw new FechaDesdeException("producto.edit.fecha.error", new Object[]{condicion, Utils.formatDate(maxFHastaPosible, Utils.SDF_DDMMYYYY_HHMM)});
		}
		//Primero actualizo productoCosto
		ProductoCosto prodCostoActual = productoCostoDao.findCostoActual(producto.getId());
		prodCostoActual.setCosto(producto.getCostoActual());
		Date maxFechaDesdeAnterior = productoCostoDao.findMaxFechaDesdeAnterior(producto.getId(), prodCostoActual.getFechaDesde());
		if(maxFechaDesdeAnterior == null){
			//No tiene otros productos costo. Igualo las fechas
			prodCostoActual.setFechaDesde(producto.getFechaAlta());
		}
		productoCostoDao.update(prodCostoActual);
		//Esto se hace para asociar al objecto producto a la sessión, ya que sino nos daria el siguiente error:
		//A different object with the same identifier value was already associated with the session
		//Debido a que tenemos otro objecto (productoActual) con el mismo id obtenido durante la session
		producto = productoDao.merge(producto);
		controlStockProducto.actualizarProductoStock(producto);
	}

	@Transactional
	@Override
	public void eliminarProducto(Integer productoId) {
		controlStockProducto.eliminarProductoStock(productoId);
		ProductoCosto prodCosto = productoCostoDao.findCostoActual(productoId);
		if(prodCosto != null){
			logger.info("El producto contiene relaciones con ProductoCosto. Se procede a su baja logica.");
			productoCostoDao.delete(prodCosto);
		}
		List<RelProductoCategoria> relProdCats = relProductoCategoriaDao.findAllByProducto(productoId, Constantes.ESTADO_REL_VIGENTE);
		if(!relProdCats.isEmpty()){
			logger.info("El producto contiene relaciones con RelProductoCategoria. Se procede a su baja logica.");
			}
		for (RelProductoCategoria relProdCategoria : relProdCats) {
			relProductoCategoriaDao.delete(relProdCategoria);
		}
	}

	@Override
	@Transactional
	public void reactivarProducto(Integer productoId) {
		Producto producto = productoDao.findById(productoId);
		ProductoCosto rpc = new ProductoCosto();
		rpc.setCosto(producto.getCostoActual());
		rpc.setFechaDesde(new Date());
		rpc.setProducto(producto);
		productoCostoDao.save(rpc);
		controlStockProducto.reactivarProductoStock(producto.getId());
	}

	@Override
	@Transactional
	public boolean asignarNuevoPrecio(List<RelProductoCategoria> relProdCats, Date fechaDesde, BigDecimal costo, BigDecimal[] incrementos) throws FechaDesdeException {
		boolean huboCambios = false;
		try{
			for (int i = 0; i < relProdCats.size(); i++) {
				RelProductoCategoria relProdCat = relProductoCategoriaDao.findById(relProdCats.get(i).getId());
				Producto producto = productoDao.findById(relProdCat.getProducto().getId());
				if(i == 0){
					ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("prodCosto/").concat(String.valueOf(relProdCat.getProducto().getId())).concat("?estado=A"), "prodCosto.concurrencia.pedido.error");
				}
				ProductoCosto costoActual = productoCostoDao.findCostoActual(relProdCat.getProducto().getId());
				if(Utils.esMenor(fechaDesde, costoActual.getFechaDesde())){
					throw new FechaDesdeException("prodCosto.fecha.desde.error", new Object[]{Utils.formatDate(costoActual.getFechaDesde(), Utils.SDF_DDMMYYYY_HHMM)});
				}
				if(!producto.getCostoActual().equals(costo)){
					//Hubo cambio de costo
					//Primero Controlo la fecha desde
					ProductoCosto prodCostoActual = productoCostoDao.findCostoActual(producto.getId());
					if(fechaDesde.before(prodCostoActual.getFechaDesde())){
						throw new FechaDesdeException("prodCosto.fecha.desde.error", new Object[]{prodCostoActual.getFechaDesde()});
					}
					producto.setCostoActual(costo);
					producto.setStockControlado(Boolean.TRUE);//No necesito control de stock
					productoDao.update(producto);
					//Finalizo la relacion actual y creo una nueva
					prodCostoActual.setFechaHasta(fechaDesde);
					productoCostoDao.update(prodCostoActual);
					
					ProductoCosto rpc = new ProductoCosto();
					rpc.setCosto(producto.getCostoActual());
					rpc.setFechaDesde(fechaDesde);
					rpc.setProducto(producto);
					productoCostoDao.save(rpc);
					huboCambios = true;
				}
				//Ahora me fijo si cambiaron los incrementos de las categorias
				if(!relProdCat.getIncremento().equals(incrementos[i])){
					//Hubo cambios de incremento en categorias
					//Primero Controlo la fecha desde
					if(fechaDesde.before(relProdCat.getFechaDesde())){
						throw new FechaDesdeException("prodCosto.fecha.desde.error", new Object[]{Utils.formatDate(relProdCat.getFechaDesde(), Utils.SDF_DDMMYYYY_HHMM)});
					}
					relProdCat.setFechaHasta(fechaDesde);
					relProductoCategoriaDao.update(relProdCat);
					
					RelProductoCategoria rpc = new RelProductoCategoria();
					rpc.setCategoria(relProdCat.getCategoria());
					rpc.setProducto(relProdCat.getProducto());
					rpc.setIncremento(incrementos[i]);
					rpc.setFechaDesde(fechaDesde);
					relProductoCategoriaDao.save(rpc);
					huboCambios = true;
				}
			}
		}finally{
			//Quito control
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.FALSE);
		}
		return huboCambios;
	}
}