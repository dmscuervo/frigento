package com.soutech.frigento.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.PromocionDao;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Promocion;
import com.soutech.frigento.service.PromocionService;

@Service
public class PromocionServiceImpl implements PromocionService {
	
	@Autowired
	private PromocionDao promocionDao;

	@Override
	public List<Promocion> obtenerPromociones() {
		return promocionDao.findAll();
	}

	@Override
	public void savePromocion(Promocion promocion) {
		promocionDao.save(promocion);
	}

	@Override
	public Promocion obtenerPromocion(Integer id) {
		return promocionDao.findById(id);
	}

	@Override
	public List<Promocion> obtenerPromociones(Boolean vigente, String sortFieldName, String sortOrder) {
		return promocionDao.obtenerPromociones(vigente, sortFieldName, sortOrder);
	}

	@Override
	public void actualizarPromocion(Promocion promocion) {
		promocionDao.update(promocion);
	}

	@Override
	public void eliminarPromocion(Promocion promocion) throws EntityExistException {
		Promocion promoActual = promocionDao.findById(promocion.getId());
		promoActual.setFechaHasta(new Date());
		promocionDao.update(promoActual);
	}

}