package com.inventario.app.service;

import com.inventario.app.dto.ProductoRequest;
import com.inventario.app.dto.ProductoResponse;
import com.inventario.app.exception.DuplicateResourceException;
import com.inventario.app.exception.ResourceNotFoundException;
import com.inventario.app.model.Categoria;
import com.inventario.app.model.Producto;
import com.inventario.app.repository.CategoriaRepository;
import com.inventario.app.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void guardaProductoSinCategoria() {

        ProductoRequest request = new ProductoRequest("  Cable  ", new BigDecimal("5000"), null);

        when(productoRepository.existsByNombreIgnoreCase("Cable")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocacion -> {
            Producto producto = invocacion.getArgument(0);
            producto.setId(1L);
            return producto;
        });

        ProductoResponse response = productoService.save(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nombre()).isEqualTo("Cable");   // sin espacios
        assertThat(response.categoriaId()).isNull();
    }

    @Test
    void rechazaNombreDuplicado() {

        ProductoRequest request = new ProductoRequest("Mouse", new BigDecimal("25000"), null);

        when(productoRepository.existsByNombreIgnoreCase("Mouse")).thenReturn(true);

        assertThatThrownBy(() -> productoService.save(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Ya existe un producto");

        verify(productoRepository, never()).save(any());
    }

    @Test
    void rechazaCategoriaInexistente() {

        ProductoRequest request = new ProductoRequest("Mouse", new BigDecimal("25000"), 99L);

        when(productoRepository.existsByNombreIgnoreCase("Mouse")).thenReturn(false);
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.save(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void asignaCategoriaExistente() {

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Periféricos");

        ProductoRequest request = new ProductoRequest("Mouse", new BigDecimal("25000"), 1L);

        when(productoRepository.existsByNombreIgnoreCase("Mouse")).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        ProductoResponse response = productoService.save(request);

        assertThat(response.categoriaNombre()).isEqualTo("Periféricos");
    }
}
