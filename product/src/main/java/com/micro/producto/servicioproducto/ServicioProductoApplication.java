package com.micro.producto.servicioproducto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
// Para que reconozca las clases de la librería común
//@ComponentScan(basePackages = {"com.micro.commons"}) // CUIDADO DE DESCOMENTAR ESTO PORQUE YA NO ME EJECUTABA EL IMPORT DE LA BBDD
// Para que reconozca las entidades de la librería común
@EntityScan(basePackages = {"com.micro.commons.models.entity"})

public class ServicioProductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioProductoApplication.class, args);
    }

}
