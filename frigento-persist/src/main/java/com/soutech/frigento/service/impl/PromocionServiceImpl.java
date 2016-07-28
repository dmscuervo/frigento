package com.soutech.frigento.service.impl;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void savePromocion(Promocion promocion) {
		promocionDao.save(promocion);
	}

	@Override
	public Promocion obtenerPromocion(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promocion> obtenerPromociones(String sortFieldName, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizarPromocion(Promocion promocion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminarPromocion(Promocion promocion) throws EntityExistException {
		// TODO Auto-generated method stub

	}

}
