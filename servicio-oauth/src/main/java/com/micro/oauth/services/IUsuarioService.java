package com.micro.oauth.services;

import com.micro.commons.usuarios.models.entity.Usuario;


public interface IUsuarioService {
    public Usuario findByUsername(String username);

    public Usuario update(Usuario usuario,Long id);

}
