package com.micro.producto.servicioproducto.models.service;

import com.micro.commons.models.entity.Producto;

import java.util.List;

public interface IProductoService {

    public List<Producto> findAll();
    public Producto findById(Long id);

    public Producto save( Producto producto);
    public void deleteById(Long id);


}
