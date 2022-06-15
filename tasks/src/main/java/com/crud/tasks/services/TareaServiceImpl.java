package com.crud.tasks.services;

import com.crud.tasks.models.Tarea;
import com.crud.tasks.repos.TareaRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepo tareaRepo;

    public TareaServiceImpl(TareaRepo tareaRepo) {
        this.tareaRepo = tareaRepo;
    }

    @Override
    public List<Tarea> obtenerTareas() {
        return (List<Tarea>) tareaRepo.findAll();
    }

    @Override
    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepo.findById(id);
    }

    @Override
    public Tarea guardarTarea(Tarea tarea) {
        if(tarea.getIdTarea() == null) {
            tarea.setEstado(false);
        }
        return tareaRepo.save(tarea);
    }

    @Override
    public void eliminarTarea(Long id) {
        tareaRepo.deleteById(id);
    }
}
