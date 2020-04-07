package com.micro.producto.servicioproducto.controller;

import com.micro.commons.models.entity.Producto;
import com.micro.producto.servicioproducto.models.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    //@Autowired
    //private Environment environment;

    // Recuperamos del properties el valor del puerto
    @Value("${server.port}")
    private Integer port;

    @GetMapping("/listar")
    public List<Producto> listar (){
        List<Producto> lista =productoService.findAll();
        return lista.stream().map(
                producto -> { producto.setPort(port);
                return producto; }
        ).collect(Collectors.toList());
    }
    @GetMapping("/detalle/{id}")
    public Producto detalle( @PathVariable Long id) throws Exception {

        Producto producto= productoService.findById(id);
        //producto.setPort( Integer.parseInt(
        //        environment.getProperty("local.server.port")));
        producto.setPort(port);
//      Forzar error de ejecución
//        boolean ok =false;
//        if (ok == false)
//            throw new Exception("No se pudo cargar el producto");

//      Forzar error de timeout
//        try {
//            Thread.sleep(2000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return producto;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear( @RequestBody Producto producto){
        return productoService.save(producto);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar( @RequestBody Producto producto, @PathVariable Long id){
        Producto productoDb = productoService.findById(id);

        productoDb.setNombre(producto.getNombre());
        productoDb.setPrecio(producto.getPrecio());

        return productoService.save(productoDb);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar (@PathVariable Long id) {
        productoService.deleteById(id);
    }
}
