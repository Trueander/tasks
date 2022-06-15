package com.crud.tasks.services;

import com.crud.tasks.models.Tarea;

import java.util.List;
import java.util.Optional;

public interface TareaService {
    List<Tarea> obtenerTareas();
    Optional<Tarea> obtenerPorId(Long id);
    Tarea guardarTarea(Tarea tarea);
    void eliminarTarea(Long id);
}
