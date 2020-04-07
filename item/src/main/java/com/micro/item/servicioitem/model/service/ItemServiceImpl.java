package com.micro.item.servicioitem.model.service;

import com.micro.commons.models.entity.Producto;
import com.micro.item.servicioitem.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación de cliente Rest con RestTemplate
 */
@Service("ItemServiceRestTemplate")
public class ItemServiceImpl implements ItemService {
    @Autowired
    private RestTemplate clienteRest;

    @Override
    public List<Item> findAll() {
        List<Producto> productos =
                Arrays.asList(
                        clienteRest.getForObject("http://servicio-productos/listar",
                                Producto[].class));
        return productos.stream().map( p -> new Item(p, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Map<String,String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id",id.toString());
        Producto producto = clienteRest
                .getForObject("http://servicio-productos/detalle/{id}",Producto.class,pathVariables);
        return new Item(producto,cantidad);

    }

    @Override
    public Producto save(Producto producto) {
        HttpEntity body = new HttpEntity<Producto>(producto);

        // Consumimos un servicio Rest
        // Parámetros - ruta, tipo de método, cuerpo de la petición y TIPO de respuesta
        ResponseEntity<Producto> response=clienteRest.exchange
                ("http://servicio-productos/crear", HttpMethod.POST, body, Producto.class);
        return response.getBody();
    }

    @Override
    public Producto update(Producto producto, Long id) {
        HttpEntity body = new HttpEntity<Producto>(producto);

        Map<String,String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id",id.toString());

        // Consumimos un servicio Rest
        // Parámetros - ruta, tipo de método, cuerpo de la petición, TIPO de respuesta y parámetros de la url
        ResponseEntity<Producto> response=clienteRest.exchange
                ("http://servicio-productos/editar/{id}", HttpMethod.PUT, body, Producto.class,pathVariables);
        return response.getBody();

    }

    @Override
    public void delete(Long id) {
        Map<String,String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id",id.toString());

        clienteRest.delete("http://servicio-productos/eliminar/{id}",pathVariables);
    }
}