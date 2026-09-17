package com.inventario.app.service;

import com.inventario.app.exception.DuplicateResourceException;
import com.inventario.app.exception.InvalidDataException;
import com.inventario.app.exception.ResourceNotFoundException;
import com.inventario.app.model.Categoria;
import com.inventario.app.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria save(Categoria categoria){

        if(categoria == null){
            throw new InvalidDataException("Los datos de la categoría son obligatorios");
        }

        if(categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()){
            throw new InvalidDataException("El nombre de la categoría no puede estar vacío");
        }

        // Se guarda el nombre sin espacios sobrantes
        categoria.setNombre(categoria.getNombre().trim());

        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())){
            throw new DuplicateResourceException("Ya existe una categoría con el nombre " + categoria.getNombre());
        }

        categoria.setId(null);

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    public void deleteById(Long id){

        if (id == null || id <= 0){
            throw new InvalidDataException("El id de la categoría debe ser mayor que cero");
        }

        if (!categoriaRepository.existsById(id)){
            throw new ResourceNotFoundException("No existe una categoría con Id " + id);
        }

        categoriaRepository.deleteById(id);
    }
}
