package com.myerp.rh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myerp.rh.repository.UsuarioRepository;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    private final UsuarioRepository repository;

    public FuncionarioController(UsuarioRepository repository) {
        this.repository = repository;
    }


    



}
