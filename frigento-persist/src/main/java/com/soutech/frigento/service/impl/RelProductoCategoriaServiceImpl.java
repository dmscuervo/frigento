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
	public void asignarProductos(Categoria categoria, List<RelProductoCategoria> listaAgregados, List<RelProductoCategoria> listaModificados, List<RelProductoCategoria> listaEliminados) throws FechaDesdeException, ProductoInexistenteException {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("relProdCat/"+categoria.getId()+"?listar"), "relProdCat.concurrencia.venta.error");
		try{
			Producto producto;
			//Primero las nuevas relaciones
			for (RelProductoCategoria rpcAdd : listaAgregados) {
				producto = productoDao.findByCodigo(rpcAdd.getProducto().getCodigo());
				//Controlo fechas con ProductoCosto
				Date fechaDesdeMin = productoCostoDao.getMinFechaDesde(producto.getId());
				if(fechaDesdeMin != null && rpcAdd.getFechaDesde().before(fechaDesdeMin)){
					Object[] args = new Object[]{producto.getCodigo(), Utils.formatDate(fechaDesdeMin, Utils.SDF_DDMMYYYY_HHMM)};
					throw new FechaDesdeException("relProdCat.fecha.desde.min.producto.costo", args);
				}
				//Controlo fechas de ventas
				Date maxFechaVenta = relVentaProductoDao.findMaxFechaNoAnulada(producto.getId());
				if(maxFechaVenta != null && rpcAdd.getFechaDesde().before(maxFechaVenta)){
					Object[] args = new Object[]{producto.getCodigo(), Utils.formatDate(fechaDesdeMin, Utils.SDF_DDMMYYYY_HHMM)};
					throw new FechaDesdeException("relProdCat.fecha.desde.min.venta", args);
				}
				//Controlo fechas con alta del producto
				if(!Utils.esMayorIgual(rpcAdd.getFechaDesde(), producto.getFechaAlta())){
					
					throw new ProductoInexistenteException("relProdCat.producto.inexistente.fecha", new Object[]{producto.getCodigo(), rpcAdd.getFechaDesde()});
					
				}
//				else if(!Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaDesde())){
//					
//					throw new ProductoInexistenteException("relProdCat.producto.baja.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaDesde()});
//					
//				}
				
				rpcAdd.setCategoria(categoriaDao.findById(categoria.getId()));
				rpcAdd.setProducto(producto);
				relProductoCategoriaDao.save(rpcAdd);
			}
			
			//Continuo con las modificaciones sobre registros ya existentes
			for (RelProductoCategoria rpcUpd : listaModificados) {
				producto = productoDao.findByCodigo(rpcUpd.getProducto().getCodigo());
				//Controlo fechas con alta del producto
				if(!Utils.esMayorIgual(rpcUpd.getFechaDesde(), producto.getFechaAlta())){
					
					throw new ProductoInexistenteException("relProdCat.producto.inexistente.fecha", new Object[]{producto.getCodigo(), rpcUpd.getFechaDesde()});
					
				}
//				else if(!Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaDesde())
//						|| (relProdCat.getFechaHasta() != null && !Utils.esMenor(producto.getFechaAlta(), relProdCat.getFechaHasta()))){
//					
//					throw new ProductoInexistenteException("relProdCat.producto.baja.fecha", new Object[]{relProdCat.getProducto().getCodigo(), relProdCat.getFechaHasta()});
//					
//				}
				relProductoCategoriaDao.update(rpcUpd);
			}
			
			//Por ultimo elimino los registros existentes en BD
			for (RelProductoCategoria rpcDel : listaEliminados) {
				producto = productoDao.findByCodigo(rpcDel.getProducto().getCodigo());
				//Controlo fechas de ventas
				Date primerFechaVenta = relVentaProductoDao.obtenerFechaPrimerVentaNoAnulada(producto.getId(), categoria.getId(), rpcDel.getFechaDesde(), rpcDel.getFechaHasta());
				if(primerFechaVenta != null){
					Object[] args = new Object[]{producto.getCodigo(), Utils.formatDate(primerFechaVenta, Utils.SDF_DDMMYYYY_HHMM)};
					throw new FechaDesdeException("relProdCat.borrar.venta.existente", args);
				}
				//Se dio de baja. Aplico un baja fisica
				relProductoCategoriaDao.delete(rpcDel);
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
