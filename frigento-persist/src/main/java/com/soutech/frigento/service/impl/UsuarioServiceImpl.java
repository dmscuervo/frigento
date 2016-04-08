package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
    UsuarioDao usuarioDao;
}
