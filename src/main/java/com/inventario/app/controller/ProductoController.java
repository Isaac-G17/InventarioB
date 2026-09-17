package com.inventario.app.controller;


import com.inventario.app.model.Producto;
import com.inventario.app.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones para registrar y consultar productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un nuevo producto",description = "Valida y almacena un producto")
    public Producto create(@RequestBody Producto producto){
        return productoService.save(producto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos los productos", description = "Retorna la colección completa de productos registrados")
    public List<Producto> getAll(){
        return productoService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su id")
    public void delete(@PathVariable Long id){ // @PathVariable -> toma el id de la url y lo pasa al metodo
        productoService.deleteById(id);
    }
}
