package com.inventario.app.service;

import com.inventario.app.exception.DuplicateResourceException;
import com.inventario.app.exception.InvalidDataException;
import com.inventario.app.exception.ResourceNotFoundException;
import com.inventario.app.model.Producto;
import com.inventario.app.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    public Producto save(Producto producto){

        if(producto == null){
            throw new InvalidDataException("Los datos del producto son obligatorios");
        }

        if(producto.getNombre() == null || producto.getNombre().trim().isEmpty()){
            throw new InvalidDataException("El nombre del producto no puede estar vacío");
        }

        if(producto.getPrecio() == null){
            throw new InvalidDataException("El precio del producto es obligatorio");
        }

        if(producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidDataException("El precio del producto debe ser mayor que cero");
        }

        // Se guarda el nombre sin espacios sobrantes
        producto.setNombre(producto.getNombre().trim());

        if (productoRepository.existsByNombreIgnoreCase(producto.getNombre())){
            throw new DuplicateResourceException("Ya existe un producto con el nombre " + producto.getNombre());
        }

        producto.setId(null);

        return productoRepository.save(producto);
    }

    public List<Producto> findAll(){
        return productoRepository.findAll();
    }

    public void deleteById(Long id){

        if (id == null || id <= 0){
            throw new InvalidDataException("El id del producto debe ser mayor que cero");
        }

        if (!productoRepository.existsById(id)){
            throw new ResourceNotFoundException("No existe un producto con Id " + id);
        }

        productoRepository.deleteById(id);
    }
}
