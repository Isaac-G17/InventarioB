package com.inventario.app.service;

import com.inventario.app.dto.CategoriaRequest;
import com.inventario.app.dto.CategoriaResponse;
import com.inventario.app.exception.DuplicateResourceException;
import com.inventario.app.exception.InvalidDataException;
import com.inventario.app.exception.ResourceNotFoundException;
import com.inventario.app.model.Categoria;
import com.inventario.app.model.Producto;
import com.inventario.app.repository.CategoriaRepository;
import com.inventario.app.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public CategoriaResponse save(CategoriaRequest request) {

        String nombre = request.nombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw DuplicateResourceException.categoria(nombre);
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> findAll() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {

        validarId(id);

        if (!categoriaRepository.existsById(id)) {
            throw ResourceNotFoundException.categoria(id);
        }

        desasignarProductos(id);

        categoriaRepository.deleteById(id);
    }

    private void desasignarProductos(Long categoriaId) {

        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);

        productos.forEach(producto -> producto.setCategoria(null));

        productoRepository.saveAll(productos);
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("El id debe ser mayor que cero");
        }
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre());
    }
}
