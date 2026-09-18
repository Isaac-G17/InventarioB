package com.inventario.app.controller;


import com.inventario.app.dto.CategoriaRequest;
import com.inventario.app.dto.CategoriaResponse;
import com.inventario.app.model.Categoria;
import com.inventario.app.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Operaciones para registrar y consultar categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una nueva categoría", description = "Valida y almacena una categoría")
    public CategoriaResponse create(@Valid @RequestBody CategoriaRequest request) {
        return categoriaService.save(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todas las categorías", description = "Retorna la colección completa de categorías registradas")
    public List<CategoriaResponse> getAll() {
        return categoriaService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría por su id")
    public void delete(@PathVariable Long id) { // @PathVariable -> toma el id de la url y lo pasa al metodo
        categoriaService.deleteById(id);
    }
}
