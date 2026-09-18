package com.inventario.app.service;

import com.inventario.app.dto.ProductoRequest;
import com.inventario.app.dto.ProductoResponse;
import com.inventario.app.exception.DuplicateResourceException;
import com.inventario.app.exception.InvalidDataException;
import com.inventario.app.exception.ResourceNotFoundException;
import com.inventario.app.model.Categoria;
import com.inventario.app.model.Producto;
import com.inventario.app.repository.CategoriaRepository;
import com.inventario.app.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProductoResponse save(ProductoRequest request) {

        String nombre = request.nombre().trim();

        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw DuplicateResourceException.producto(nombre);
        }

        Categoria categoria = buscarCategoria(request.categoriaId());

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(request.precio());
        producto.setCategoria(categoria);

        return toResponse(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findAll() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {

        validarId(id);

        if (!productoRepository.existsById(id)) {
            throw ResourceNotFoundException.producto(id);
        }

        productoRepository.deleteById(id);
    }

    private Categoria buscarCategoria(Long categoriaId) {

        if (categoriaId == null) {
            return null;
        }

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> ResourceNotFoundException.categoria(categoriaId));
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("El id debe ser mayor que cero");
        }
    }

    private ProductoResponse toResponse(Producto producto) {

        Categoria categoria = producto.getCategoria();

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNombre() : null);
    }
}
