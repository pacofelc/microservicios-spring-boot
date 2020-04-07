package com.micro.producto.servicioproducto.models.dao;

import com.micro.commons.models.entity.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoDao extends CrudRepository<Producto, Long> {
}
