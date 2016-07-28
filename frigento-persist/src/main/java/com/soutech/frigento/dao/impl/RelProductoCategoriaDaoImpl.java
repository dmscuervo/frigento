package com.soutech.frigento.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.util.Constantes;

@Repository
@Transactional(readOnly=true)
public class RelProductoCategoriaDaoImpl extends AbstractSpringDao<RelProductoCategoria, Integer> implements RelProductoCategoriaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllByCategoria(Date fecha, Short idCat, String[] sortFieldName, String[] sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r left outer join fetch r.promociones p ");
		hql.append(" where r.categoria.id = :catId");
		if(fecha != null){
			hql.append(" and r.fechaDesde <= :fecha");
			hql.append(" and (r.fechaHasta is null or r.fechaHasta > :fecha)");
		}
		if(sortFieldName != null){
			hql.append(" order by r.");
			for (int i = 0; i < sortFieldName.length; i++) {
				String sfn = sortFieldName[i];
				String so;
				try {
					so = sortOrder[i];
				} catch (Exception e) {
					so = "asc";
				}
				hql.append(sfn);
				hql.append(" ");
				hql.append(so);
				hql.append(", r.");
			}
			hql.replace(hql.length()-4, hql.length(), "");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		if(fecha != null){
			query.setParameter("fecha", fecha);
		}
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllByCategoria(Short idCat, String estado, String[] sortFieldName, String[] sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.categoria.id = :catId");
		if(estado != null && estado.equals(Constantes.ESTADO_REL_VIGENTE)){
			hql.append(" and r.fechaHasta is null");
		}else if(estado != null && estado.equals(Constantes.ESTADO_REL_NO_VIGENTE)){
			hql.append(" and r.fechaHasta is not null");
		}
		if(sortFieldName != null){
			hql.append(" order by r.");
			for (int i = 0; i < sortFieldName.length; i++) {
				String sfn = sortFieldName[i];
				String so;
				try {
					so = sortOrder[i];
				} catch (Exception e) {
					so = "asc";
				}
				hql.append(sfn);
				hql.append(" ");
				hql.append(so);
				hql.append(", r.");
			}
			hql.replace(hql.length()-4, hql.length(), "");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllByProducto(Integer idProd, String estado) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" rpc where rpc.producto.id = :idProd ");
		if(estado != null && estado.equals(Constantes.ESTADO_REL_VIGENTE)){
			hql.append("and rpc.fechaHasta is null");
		}else if(estado != null && estado.equals(Constantes.ESTADO_REL_NO_VIGENTE)){
			hql.append("and rpc.fechaHasta is not null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return query.list();
	}

	@Transactional
	public void deleteAll(Collection<RelProductoCategoria> relProdCategorias) {
		for (RelProductoCategoria relProdCategoria : relProdCategorias)
			//La baja de productoCosto es logica
			delete(relProdCategoria);
	}

	@Override
	public RelProductoCategoria findActualByDupla(Short idCat, Integer idProd) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.categoria.id = :catId ");
		hql.append("and r.producto.id = :prodId ");
		hql.append("and r.fechaHasta is null ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		query.setParameter("prodId", idProd);
		return (RelProductoCategoria) query.uniqueResult();
	}

	@Override
	public Date findMinDate(Integer idProd) {
		StringBuilder hql = new StringBuilder("select min(rpc.fechaDesde) from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" rpc where rpc.producto.id = :idProd ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (Date) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllByDupla(Short idCat, Integer idProd) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.categoria.id = :catId ");
		hql.append("and r.producto.id = :prodId ");
		hql.append("order by r.categoria.id, r.producto.id, r.fechaDesde ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		query.setParameter("prodId", idProd);
		return query.list();
	}

	@Override
	public RelProductoCategoria findByDupla(Short idCat, Integer idProd, Date fecha) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.categoria.id = :catId ");
		hql.append("and r.producto.id = :prodId ");
		hql.append(" and r.fechaDesde <= :fecha");
		hql.append(" and (r.fechaHasta is null or r.fechaHasta > :fecha)");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		query.setParameter("prodId", idProd);
		query.setParameter("fecha", fecha);
		return (RelProductoCategoria) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllActuales() {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.fechaHasta is null ");
		Query query = getSession().createQuery(hql.toString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findBy(Integer idProd, Date fecha) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.producto.id = :prodId ");
		hql.append(" and r.fechaDesde <= :fecha");
		hql.append(" and (r.fechaHasta is null or r.fechaHasta > :fecha)");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", idProd);
		query.setParameter("fecha", fecha);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findPosterioresA(Integer idProd, Date fechaDesde) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.producto.id = :prodId ");
		hql.append(" and r.fechaDesde >= :fecha");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", idProd);
		query.setParameter("fecha", fechaDesde);
		return query.list();
	}
	
}
