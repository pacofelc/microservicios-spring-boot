package com.micro.item.servicioitem.model.service;

import com.micro.commons.models.entity.Producto;
import com.micro.item.servicioitem.model.Item;

import java.util.List;

public interface ItemService {
    public List<Item> findAll();
    public Item findById( Long id, Integer cantidad);
    public Producto save (Producto producto);
    public Producto update (Producto producto, Long id);
    public void delete(Long id);
}
