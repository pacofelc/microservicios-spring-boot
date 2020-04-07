package com.micro.item.servicioitem.controller;

import com.micro.commons.models.entity.Producto;
import com.micro.item.servicioitem.model.Item;
import com.micro.item.servicioitem.model.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Permite cambiar la configuración dinámicamente(@Value) con Spring actuator
@RefreshScope
@RestController
public class ItemController {
    @Autowired
    @Qualifier("ItemServiceFeign")
    //@Qualifier("ItemServiceRestTemplate")
    private ItemService itemService;

    // Consultamos un valor del fichero application.properties
    // O en el caso de Spring Cloud Config, del servidor de configuración
    @Value("${configuracion.texto}")
    private String textoConfig;

    @Autowired
    private Environment env;

    @GetMapping("/listar")
    public List<Item> listar(){
        return itemService.findAll();
    }

    @HystrixCommand (fallbackMethod = "metodoAlternativo")
    // http://localhost:8002/detalle/2/cantidad/4
    @GetMapping("/detalle/{id}/cantidad/{cantidad}")
    public Item detalle (@PathVariable Long id, @PathVariable Integer cantidad){
        return  itemService.findById(id,cantidad);
    }

    // Esta función se ejecutará si falla la función de la anotación HystrixCommand
    public Item metodoAlternativo (Long id, Integer cantidad){
        Item item=  new Item();
        item.setCantidad(cantidad);
        Producto producto=new Producto();
        producto.setId(id);
        producto.setNombre("Defecto por error");
        producto.setPrecio(0.0);
        item.setProducto(producto);
        return item;

    }

    /**
     * Permite Consultar de la configuración actual del servicio
     * @param puertoConfig
     * @return
     */
    @GetMapping("/obtener-config")
    public ResponseEntity<?> obtenerConfig(
            @Value("${server.port}") String puertoConfig
    ) {
        Map<String,String> json = new HashMap<>();
        json.put("texto", textoConfig);
        json.put("puerto",puertoConfig);

        if ( env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }
        return new ResponseEntity<Map<String,String>>(json, HttpStatus.OK);
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return itemService.save(producto);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto, @PathVariable Long id) {
        return itemService.update(producto,id);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        itemService.delete(id);
    }



}
