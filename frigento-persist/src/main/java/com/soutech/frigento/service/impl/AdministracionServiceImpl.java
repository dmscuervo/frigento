package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.dto.ConsultaGananciaDTO;
import com.soutech.frigento.service.AdministracionService;

@Service
public class AdministracionServiceImpl implements AdministracionService {

	@Autowired
	private RelVentaProductoDao relVentaProductoDao;

	@Override
	public List<ConsultaGananciaDTO> obtenerGanancia(Short tipo, String periodo, Short agrupamiento) {
		return relVentaProductoDao.obtenerGanancia(tipo, periodo, agrupamiento);
	}
	
}
