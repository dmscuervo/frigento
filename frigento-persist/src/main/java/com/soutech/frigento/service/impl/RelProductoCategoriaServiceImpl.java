package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.service.RelProductoCategoriaService;

@Service
public class RelProductoCategoriaServiceImpl implements RelProductoCategoriaService {

	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;

}
