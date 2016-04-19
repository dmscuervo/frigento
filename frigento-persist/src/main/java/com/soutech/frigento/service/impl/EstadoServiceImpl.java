package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.EstadoDao;
import com.soutech.frigento.enums.TipoEstadoEnum;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.service.EstadoService;

@Service
public class EstadoServiceImpl implements EstadoService {

	@Autowired
    EstadoDao estadoDao;

	@Override
	public List<Estado> obtenerEstadosPedido() {
		return estadoDao.findAllByTipo(new TipoEstadoEnum[]{TipoEstadoEnum.A, TipoEstadoEnum.P});
	}
	
	@Override
	public List<Estado> obtenerEstadosVenta() {
		return estadoDao.findAllByTipo(new TipoEstadoEnum[]{TipoEstadoEnum.A, TipoEstadoEnum.V});
	}

}