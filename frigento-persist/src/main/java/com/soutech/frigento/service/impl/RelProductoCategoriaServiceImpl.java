package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.util.Constantes;
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
    VentaDao ventaDao;
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

	@Autowired
	@Override
	@Transactional
	public void asignarProductos(Categoria categoria, List<RelProductoCategoria> relaciones) throws FechaDesdeException {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("relProdCat/"+categoria.getId()+"?listar"), "relProdCat.concurrencia.venta.error");
		try{
			//Primero chequeo si algun producto fue dado de baja y no tiene un alta nueva
			List<RelProductoCategoria> relacionesActual = relProductoCategoriaDao.findAllByCategoria(categoria.getId(), Constantes.ESTADO_REL_VIGENTE, null, null);
			List<RelProductoCategoria> relacionesEliminadas = new ArrayList<RelProductoCategoria>();
			
			List<Date[]> fechas;
			for (RelProductoCategoria relProdCatActual : relacionesActual) {
				//Control de eliminado
				Boolean prodCatEncontrado = Boolean.FALSE;
				for (RelProductoCategoria relProdCatNuevo : relaciones) {
					if(relProdCatNuevo.getId() != null && relProdCatNuevo.getId().equals(relProdCatActual.getId())){
						prodCatEncontrado = Boolean.TRUE;
						break;
					}
				}
				if(!prodCatEncontrado){
					//Se dio de baja. Aplico un baja logica
					//relProductoCategoriaDao.delete(relProdCatActual);
					relacionesEliminadas.add(relProdCatActual);
				}
			}
			
			//Deberia chequear si no hay ninguna venta para el rango de fechas dado de baja. Pero tambien podria ser que una nueva relProdCat con id = null cubra todo o parte del rango original que fue borrado
			//Lo que voy a hacer es llenar 3 listas con las relaciones a crear, modificar y eliminar. Pero antes de persistir, armo una lista con los rangos de fechas excluidos y luego reviso que no exista una venta para dichos rangos
			
//			for (RelProductoCategoria rpcBorrar : relacionesBorrar) {
//				List<Date[]> duplaFechas = mapaControl.get(new Object[]{rpcBorrar.getCategoria().getId(), rpcBorrar.getProducto().getId()});
//				for (Date[] duplaFecha : duplaFechas) {
//					Date fechaDesdeActual = duplaFecha[0];
//					Date fechaHastaActual = duplaFecha[1];
//					//Me fijo si esta dentro del rango
//					if(Utils.estaDentroDeRelacion(rpcBorrar.getFechaDesde(), fechaDesdeActual, fechaHastaActual)){
//						
//					}
//				}
//				
//				
//			}
			
			List<RelProductoCategoria> relacionesNuevas = new ArrayList<RelProductoCategoria>();
			List<RelProductoCategoria> relacionesModificadas = new ArrayList<RelProductoCategoria>();
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
						relacionesModificadas.add(rpc);
					}
					relProdCat.setCategoria(categoriaDao.findById(relProdCat.getCategoria().getId()));
					relProdCat.setProducto(producto);
					relacionesNuevas.add(relProdCat);
				}else{
					if(relProdCat.getEsEditable() && relProdCat.getFechaHasta() == null){
						relacionesModificadas.add(relProdCat);
					}
				}
			}
			//Me aseguro el ordern correcto (por codigo y fechaDesde)
			Collections.sort(relaciones);
			
			/* Logico para el for siguiente
				1) Si hay fechaDesde y fechaHasta. Son dos registros
					1.1) Si ya registre una dupla antes
						a) Pongo fechaDesde en la dupla anterior {fechaHastaAnterior, fechaDesde}
						b) Nueva dupla {fechaHasta, null}
					1.2) Si no hay duplas anteriores
						a) Nueva dupla {null, fechaDesde}
						b) Nueva dupla {fechaHasta, null}
				
				2) Si solo hay fechaDesde. Es un registro
					1.1) Si ya registre una dupla antes
						a) Pongo fechaDesde en la dupla anterior {fechaHastaAnterior, fechaDesde}
					1.2) Si no hay duplas anteriores
						a) Nueva dupla {null, fechaDesde}
			 */
			
			Map<Integer, List<Date[]>> rangosExcluidos = new HashMap<Integer, List<Date[]>>();
			for (RelProductoCategoria relProdCat : relaciones) {
				Date[] dupla;
				//Genero mapa de control
				if(!rangosExcluidos.containsKey(relProdCat.getProducto().getId())){
					fechas = new ArrayList<Date[]>();
					rangosExcluidos.put(relProdCat.getProducto().getId(), fechas);
					//No tengo dupla anteriores
					if(relProdCat.getFechaHasta() == null){
						dupla = new Date[]{null, relProdCat.getFechaDesde()};
						rangosExcluidos.get(relProdCat.getProducto().getId()).add(dupla);
					}else{
						dupla = new Date[]{null, relProdCat.getFechaDesde()};
						rangosExcluidos.get(relProdCat.getProducto().getId()).add(dupla);
						dupla = new Date[]{relProdCat.getFechaHasta(), null};
						rangosExcluidos.get(relProdCat.getProducto().getId()).add(dupla);
					}
				}else{
					//Tengo dupla anteriores
					if(relProdCat.getFechaHasta() == null){
						List<Date[]> duplas = rangosExcluidos.get(relProdCat.getProducto().getId());
						duplas.get(duplas.size()-1)[1] = relProdCat.getFechaDesde();
					}else{
						List<Date[]> duplas = rangosExcluidos.get(relProdCat.getProducto().getId());
						duplas.get(duplas.size()-1)[1] = relProdCat.getFechaDesde();
						dupla = new Date[]{relProdCat.getFechaHasta(), null};
						rangosExcluidos.get(relProdCat.getProducto().getId()).add(dupla);
					}
				}
			}
			//Verifico que no existan ventas para los ranglos excluidos
			Set<Integer> productoIds = rangosExcluidos.keySet();
			for (Integer prodId : productoIds) {
				List<Date[]> duplas = rangosExcluidos.get(prodId);
				for (Date[] dupla : duplas) {
					List<RelVentaProducto> ventas = relVentaProductoDao.findAll(prodId, categoria.getId(), dupla[0], dupla[1]);
					
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
	
}
