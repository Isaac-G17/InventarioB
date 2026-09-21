package com.inventario.app.repository;

import com.inventario.app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);
}
