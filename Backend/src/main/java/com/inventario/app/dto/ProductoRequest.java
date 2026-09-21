package com.inventario.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El nombre del producto no puede estar vacío")
        @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
        String nombre,

        @NotNull(message = "El precio del producto es obligatorio")
        @Positive(message = "El precio del producto debe ser mayor que cero")
        BigDecimal precio,

        @Positive(message = "El id de la categoría debe ser mayor que cero")
        Long categoriaId) {
}
