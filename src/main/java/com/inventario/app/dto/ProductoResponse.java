package com.inventario.app.dto;

import java.math.BigDecimal;

public record ProductoResponse(Long id, String nombre, BigDecimal precio,
                               Long categoriaId, String categoriaNombre) {
}
