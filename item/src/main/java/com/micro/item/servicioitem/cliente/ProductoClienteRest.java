package com.micro.item.servicioitem.cliente;

import com.micro.commons.models.entity.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name="servicio-productos",url="localhost:8001")
@FeignClient(name="servicio-productos")
public interface ProductoClienteRest {


    @GetMapping("/listar")
    public List<Producto> listar();

    @GetMapping("/detalle/{id}")
    public Producto detalle (@PathVariable  Long id );

    @PostMapping("/crear")
    public Producto crear (@RequestBody Producto producto);

    @PutMapping("/editar/{id}")
    public Producto modificar(@RequestBody Producto producto, @PathVariable Long id);

    @DeleteMapping("/eliminar/{id}")
    public void borrar(@PathVariable Long id);

}
