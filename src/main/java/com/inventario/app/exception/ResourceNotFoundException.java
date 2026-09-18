package com.inventario.app.exception;

public class ResourceNotFoundException extends RuntimeException {
    private ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException producto(Long id) {
        return new ResourceNotFoundException("No existe un producto con id " + id);
    }

    public static ResourceNotFoundException categoria(Long id) {
        return new ResourceNotFoundException("No existe una categoría con id " + id);
    }
}
