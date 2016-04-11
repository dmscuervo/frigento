package com.soutech.frigento.service.impl;

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
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Producto;
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
	public List<RelProductoCategoria> obtenerProductosCategoria(Short idCat) {
		return relProductoCategoriaDao.findAllByCategoria(idCat);
	}
	
	@Override
	public Date obtenerMinFechaDesde(Integer idProd) {
		return relProductoCategoriaDao.findMinDate(idProd);
	}

	@Override
	@Transactional
	public void asignarProductos(Categoria categoria, List<RelProductoCategoria> relaciones) throws FechaDesdeException {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("relProdCat/"+categoria.getId()+"?listar"), "relProdCat.concurrencia.error");
		try{
			//Primero chequeo si algun producto fue dado de baja y no tiene un alta nueva
			List<RelProductoCategoria> relacionesActual = relProductoCategoriaDao.findAllByCategoria(categoria.getId());
			for (RelProductoCategoria relProdCatActual : relacionesActual) {
				Short idCat = relProdCatActual.getCategoria().getId();
				Integer idProd = relProdCatActual.getProducto().getId();
				Boolean prodCatEncontrado = Boolean.FALSE;
				for (RelProductoCategoria relProdCatNuevo : relaciones) {
					Producto producto = productoDao.findByCodigo(relProdCatActual.getProducto().getCodigo());
					Short idCatNuevo = relProdCatNuevo.getCategoria().getId();
					Integer idProdNuevo = producto.getId();
					if(idCat.equals(idCatNuevo) && idProd.equals(idProdNuevo)){
						prodCatEncontrado = Boolean.TRUE;
						break;
					}
				}
				if(!prodCatEncontrado){
					//Se dio de baja. Aplico un baja logica
					relProductoCategoriaDao.delete(relProdCatActual);
					
				}
			}
			
			for (RelProductoCategoria relProdCat : relaciones) {
				//Si el id no es nulo, entonces es una relacion existente. No hago nada (recordar que no pueden modificarse)
				//Si el id es nulo, entonces es una nueva relacion. Tengo que verificar si una relacion con mismo prod-cat fue dada de baja
				if(relProdCat.getId() == null){
					Producto producto = productoDao.findByCodigo(relProdCat.getProducto().getCodigo());
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
					
					RelProductoCategoria rpc = relProductoCategoriaDao.findActualByDupla(relProdCat.getCategoria().getId(), producto.getId());
					if(rpc != null){
						//Bajo relacion actual
						rpc.setFechaHasta(relProdCat.getFechaDesde());
						relProductoCategoriaDao.update(rpc);
					}
					relProdCat.setCategoria(categoriaDao.findById(relProdCat.getCategoria().getId()));
					relProdCat.setProducto(producto);
					relProductoCategoriaDao.save(relProdCat);
				}
			}
			
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
	}
	
}
