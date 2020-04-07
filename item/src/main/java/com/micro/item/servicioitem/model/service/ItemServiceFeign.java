package com.micro.item.servicioitem.model.service;

import com.micro.commons.models.entity.Producto;
import com.micro.item.servicioitem.cliente.ProductoClienteRest;
import com.micro.item.servicioitem.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.stream.Collectors;

/**
 * Implementación de cliente Rest con Feign
 */

@Service("ItemServiceFeign")
// @Primary: Para que sea el servicio por defecto al inyectarse.
@Primary
public class ItemServiceFeign implements ItemService {
    @Autowired
    private ProductoClienteRest clienteFeign;

    @Override
    public List<Item> findAll() {
        return clienteFeign.listar().stream().map(p -> new Item(p, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(clienteFeign.detalle(id), cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return clienteFeign.crear(producto);
    }

    @Override
    public Producto update(Producto producto, Long id) {
        return clienteFeign.modificar(producto,id);
    }

    @Override
    public void delete(Long id) {
        clienteFeign.borrar(id);
    }
}
