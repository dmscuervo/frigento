package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.dto.ConsultaGananciaDTO;

public interface AdministracionService {

	public List<ConsultaGananciaDTO> obtenerGanancia(Short tipo, String periodo, Short agrupamiento);
}
