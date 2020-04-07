package com.micro.commons.usuarios;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ServicioUsuariosCommonsApplication {
/*Como es una librería no lo necesitamos
    public static void main(String[] args) {
        SpringApplication.run(ServicioUsuariosCommonsApplication.class, args);
    }
*/
}
