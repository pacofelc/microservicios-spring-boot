package com.micro.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
// OJO ESTO ANULA EL ENTITY SCAN @ComponentScan({"com.micro.commons.usuarios"}) // OJO ESTO ANULA EL ENTITY SCAN
@SpringBootApplication
@EntityScan(basePackages = {"com.micro.commons.usuarios.models.entity","com.micro.usuarios.models.entity"})
public class ServicioUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioUsuariosApplication.class, args);
    }

}
