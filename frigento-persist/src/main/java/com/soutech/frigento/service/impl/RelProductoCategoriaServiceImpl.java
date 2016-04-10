package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.RelProductoCategoriaService;

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

	@Override
	public List<RelProductoCategoria> obtenerProductosCategoria(Short idCat) {
		return relProductoCategoriaDao.findAllByCategoria(idCat);
	}

	@Override
	@Transactional
	public void asignarProductos(Categoria categoria, List<RelProductoCategoria> relaciones) {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("relProdCat/"+categoria.getId()+"?listar"), "relProdCat.concurrencia.error");
		try{
			
			for (RelProductoCategoria relProdCat : relaciones) {
				//Si el id no es nulo, entonces es una nueva relacion existente. No hago nada (recordar que no pueden modificarse)
				//Si el id es nulo, entonces es una nueva relacion. Tengo que verificar si una relacion con mismo prod-cat fue dada de baja
				if(relProdCat.getId() == null){
					RelProductoCategoria rpc = relProductoCategoriaDao.findActualByDupla(relProdCat.getCategoria().getId(), relProdCat.getProducto().getId());
					if(rpc != null){
						//Bajo relacion actual
						rpc.setFechaHasta(relProdCat.getFechaDesde());
						relProductoCategoriaDao.update(rpc);
					}
					relProdCat.setCategoria(categoriaDao.findById(relProdCat.getCategoria().getId()));
					relProdCat.setProducto(productoDao.findByCodigo(relProdCat.getProducto().getCodigo()));
					relProductoCategoriaDao.save(relProdCat);
				}
			}
			
//			List<RelProductoCategoria> relacionActual = relProductoCategoriaDao.findAllByCategoria((short)1);
//			//Primero actualizo relaciones existentes
//			for (RelProductoCategoria relProdCatActual : relacionActual) {
//				Short idCat = relProdCatActual.getCategoria().getId();
//				Integer idProd = relProdCatActual.getProducto().getId();
//				for (RelProductoCategoria relProdCatNuevo : relaciones) {
//					Short idCatNuevo = relProdCatNuevo.getCategoria().getId();
//					Integer idProdNuevo = relProdCatNuevo.getProducto().getId();
//					if(idCat.equals(idCatNuevo) && idProd.equals(idProdNuevo)){
//						//Relacion existente. (solo puede cambiar el incremento)
//						if(!relProdCatActual.getIncremento().equals(relProdCatNuevo.getIncremento())){
//							//Sufrio cambios. Se finaliza la actual y se ingresa nuevo registro
//							//Finalizo relacion
//							relProdCatActual.setFechaHasta(relProdCatNuevo.getFechaDesde());
//							relProductoCategoriaDao.update(relProdCatActual);
//							//Nuevo productoCosto
//							ProductoCosto pcActual = productoCostoDao.findActualByProducto(idProdNuevo);
//							//Baja logica
//							
//							ProductoCosto pc = new ProductoCosto();
//							pc.setCosto(relProdCatNuevo.getPrecioCalculado());
//							pc.setProducto(relProdCatActual.getProducto());
//							
//							pc.setFechaDesde(relProdCatNuevo.getFechaDesde());
//							relProductoCategoriaDao.save(relProdCatNuevo);
//							
//						}
//						
//					}else{
//						//Finalizo relacion
//					}
//				}
//			}
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
	}
	
}
