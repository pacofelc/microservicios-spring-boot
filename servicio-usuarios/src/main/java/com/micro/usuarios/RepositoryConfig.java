package com.micro.usuarios;



import com.micro.commons.usuarios.models.entity.Rol;
import com.micro.commons.usuarios.models.entity.Usuario;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    //Mostrar los ids de las entidades en los métodos de Rest autogenerados
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Usuario.class, Rol.class);
    }
}
