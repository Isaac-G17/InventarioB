package com.inventario.app.exception;

public class DuplicateResourceException extends RuntimeException {
    private DuplicateResourceException(String message) {
        super(message);
    }

    public static DuplicateResourceException producto(String nombre) {
        return new DuplicateResourceException("Ya existe un producto con el nombre " + nombre);
    }

    public static DuplicateResourceException categoria(String nombre) {
        return new DuplicateResourceException("Ya existe una categoría con el nombre " + nombre);
    }
}
