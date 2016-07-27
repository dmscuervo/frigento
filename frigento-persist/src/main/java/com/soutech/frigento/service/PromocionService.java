package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Promocion;

public interface PromocionService {

	public List<Promocion> obtenerPromociones();

	public void savePromocion(Promocion promocion);

	public Promocion obtenerPromocion(Integer id);

	public List<Promocion> obtenerPromociones(String sortFieldName, String sortOrder);

	public void actualizarPromocion(Promocion promocion);

	public void eliminarPromocion(Promocion promocion) throws EntityExistException;
}
