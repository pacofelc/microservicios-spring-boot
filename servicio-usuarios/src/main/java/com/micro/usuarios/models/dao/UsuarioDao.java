package com.micro.usuarios.models.dao;


import com.micro.commons.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

// Generamos de forma automática el controlador REST para el repositorio
@RepositoryRestResource(path="usuarios")
public interface UsuarioDao extends PagingAndSortingRepository<Usuario,Long> {

    // localhost:8090/api/usuarios/usuarios/search/buscar-username?username=admin
    @RestResource(path="buscar-username")
    public Usuario findByUserName( @Param ("username") String username);

    // Ejemplo de query personalizada NATIVA
    // @Query(value="select * from Usuario u where u.userName=?1",nativeQuery = true)
    // ==============================================================================

    // Ejemplo de query personalizada JPA
    @Query("select u from Usuario u where u.userName=?1")
    public Usuario consultarPorUsername(String username);
}
