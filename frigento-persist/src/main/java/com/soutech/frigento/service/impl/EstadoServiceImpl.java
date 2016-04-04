package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.EstadoDao;
import com.soutech.frigento.service.EstadoService;

@Service
@Transactional
public class EstadoServiceImpl implements EstadoService {

	@Autowired
    EstadoDao estadoDao;

}