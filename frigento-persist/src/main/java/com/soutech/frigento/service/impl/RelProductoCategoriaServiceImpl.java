package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.exception.ProductoInexistenteException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Utils;

@Service
public class RelProductoCategoriaServiceImpl implements RelProductoCategoriaService {

	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;
	
	@Autowired
    ProductoCostoDao productoCostoDao;
	
	@Autowired
    ProductoDao productoDao;
	
	@Autowired
    CategoriaDao categoriaDao;

	@Autowired
    RelVentaProductoDao relVentaProductoDao;
	
	@Override
	public List<RelProductoCategoria> obtenerProductosCategoria(Short idCat, String estado, String[] sortFieldName, String[] sortOrder) {
		return relProductoCategoriaDao.findAllByCategoria(idCat, estado, sortFieldName, sortOrder);
	}
	
	@Override
	public List<RelProductoCategoria> obtenerProductosCategoriaParaVenta(Date fecha, Short idCat, String estado) {
		List<RelProductoCategoria> relProdCatList = relProductoCategoriaDao.findAllByCategoria(fecha, idCat);
		for (RelProductoCategoria rpc : relProdCatList) {
			ProductoCosto prodCosto = productoCostoDao.findByProductoFecha(rpc.getProducto().getId(), fecha);
			BigDecimal factor = rpc.getIncremento().divide(new BigDecimal(100)).add(BigDecimal.ONE);
			rpc.getProducto().setImporteVenta(prodCosto.getCosto().multiply(factor).setScale(2, RoundingMode.HALF_UP));
			rpc.getProducto().setCostoVenta(prodCosto.getCosto());
		}
		return relProdCatList;
	}
	
	@Override
	public Date obtenerMinFechaDesde(Integer idProd) {
		return relProductoCategoriaDao.findMinDate(idProd);
	}

	@Override
	@Transactional
	public void asignarProductos(Categoria categoria, List<RelProductoCategoria> relaciones, String estadoRelVisualizadas) throws FechaDesdeException, ProductoInexistenteException {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("relProdCat/"+categoria.getId()+"?listar"), "relProdCat.concurrencia.venta.error");
		try{
			//Primero chequeo si algun producto fue dado de baja y no tiene un alta nueva
			List<RelProductoCategoria> relacionesActual = relProductoCategoriaDao.findAllByCategoria(categoria.getId(), estadoRelVisualizadas, null, null);
			for (RelProductoCategoria relProdCatActual : relacionesActual) {
				Integer idRpcActual = relProdCatActual.getId();
				Boolean relacionMantenida = Boolean.FALSE;
				for (RelProductoCategoria relProdCatNuevo : relaciones) {
					Integer idRpcNuevo = relProdCatNuevo.getId();
					if(idRpcNuevo != null && idRpcNuevo.equals(idRpcActual)){
						relacionMantenida = Boolean.TRUE;
						break;
					}
				}
				if(!relacionMantenida){
					//Controlo fechas de ventas
					Date primerFechaVenta = relVentaProductoDao.obtenerFechaPrimerVentaNoAnulada(relProdCatActual.getProducto().getId(), relProdCatActual.getCategoria().getId(), relProdCatActual.getFechaDesde(), relProdCatActual.getFechaHasta());
					if(primerFechaVenta != null){
						Object[] args = new Object[]{relProdCatActual.getProducto().getCodigo(), Utils.formatDate(primerFechaVenta, Utils.SDF_DDMMYYYY_HHMM)};
						throw new FechaDesdeException("relProdCat.borrar.venta.existente", args);
					}
					//Se dio de baja. Aplico un baja fisica
					relProductoCategoriaDao.delete(relProdCatActual);
					
				}
			}
			
			for (RelProductoCategoria relProdCat : relaciones) {
				//Si el id no es nulo, entonces es una relacion existente. No hago nada (recordar que no pueden modificarse)
				//Si el id es nulo, entonces es una nueva relacion. Tengo que verificar si una relacion con mismo prod-cat fue dada de baja
				Producto producto = productoDao.findByCodigo(relProdCat.getProducto().getCodigo());
				if(relProdCat.getId() == null){
					//Controlo fechas con ProductoCosto
					Date fechaDesdeMin = productoCostoDao.getMinFechaDesde(producto.getId());
					if(fechaDesdeMin != null && relProdCat.getFechaDesde().before(fechaDesdeMin)){
						Object[] args = new Object[]{relProdCat.getProducto().getCodigo(), Utils.formatDate(fechaDesdeMin, Utils.SDF_DDMMYYYY_HHMM)};
						throw new FechaDesdeException("relProdCat.fecha.desde.min.producto.costo", args);
					}
					//Controlo fechas de ventas
					Date maxFechaVenta = relVentaProductoDao.findMaxFechaNoAnulada(producto.getId());
					if(maxFechaVenta != null && relProdCat.getFechaDesde().before(maxFechaVenta)){
						Object[] args = new Object[]{relProdCat.getProducto().getCodigo(), Utils.formatDate(fechaDesdeMin, Utils.SDF_DDMMYYYY_HHMM)};
						throw new FechaDesdeException("relProdCat.fecha.desde.min.venta", args);
					}
					//Controlo fechas con alta del producto
					if(!Utils.esMayorIgual(relProdCat.getFechaDesde(), producto.getFechaAlta())){
						
						throw new ProductoInexistenteException("relProdCat.producto.inexistente.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaDesde()});
						
					}else if(!Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaDesde())){
						
						throw new ProductoInexistenteException("relProdCat.producto.baja.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaDesde()});
						
					}
					
					RelProductoCategoria rpc = relProductoCategoriaDao.findActualByDupla(relProdCat.getCategoria().getId(), producto.getId());
					if(rpc != null){
						//Bajo relacion actual
						rpc.setFechaHasta(relProdCat.getFechaDesde());
						relProductoCategoriaDao.update(rpc);
					}
					relProdCat.setCategoria(categoriaDao.findById(relProdCat.getCategoria().getId()));
					relProdCat.setProducto(producto);
					relProductoCategoriaDao.save(relProdCat);
				}else{
					//Actualizo cambios
					//Controlo fechas con alta del producto
					if(!Utils.esMayorIgual(relProdCat.getFechaDesde(), producto.getFechaAlta())){
						
						throw new ProductoInexistenteException("relProdCat.producto.inexistente.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaDesde()});
						
					}else if(!Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaDesde())
							|| (relProdCat.getFechaHasta() != null && !Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaHasta()))){
						
						throw new ProductoInexistenteException("relProdCat.producto.baja.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaHasta()});
						
					}
					//Esto se hace para asociar al objecto relProdCat a la sessión, ya que sino nos daria el siguiente error:
					//A different object with the same identifier value was already associated with the session
					relProdCat = relProductoCategoriaDao.merge(relProdCat);
					relProductoCategoriaDao.update(relProdCat);
				}
			}
			
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
	}

	@Override
	public List<RelProductoCategoria> obtenerCategoriasProducto(Integer idProd, String estado) {
		return relProductoCategoriaDao.findAllByProducto(idProd, estado);
	}

	@Override
	public List<RelProductoCategoria> obtenerRelaciones(Integer idProd, Short idCat) {
		return relProductoCategoriaDao.findAllByDupla(idCat, idProd);
	}

	@Override
	public RelProductoCategoria obtenerById(Integer idProdCat) {
		return relProductoCategoriaDao.findById(idProdCat);
	}
	
}
