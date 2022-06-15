package com.crud.tasks.controller;

import com.crud.tasks.models.Tarea;
import com.crud.tasks.services.TareaService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v0/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/listar")
    public ResponseEntity<?> obtenerTareas() {
        Map<String, Object> response = new HashMap<>();
        List<Tarea> tareas;
        try {
            tareas = tareaService.obtenerTareas();

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al obtener las tareas.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Tareas obtenidas con éxito.");
        response.put("tareas", tareas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{idTarea}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long idTarea) {
        Map<String, Object> response = new HashMap<>();
        Optional<Tarea> tarea;
        try {
            tarea = tareaService.obtenerPorId(idTarea);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al obtener la tarea.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!tarea.isPresent()) {
            response.put("mensaje", "No se encontró tarea con ese ID.");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        response.put("mensaje", "Tarea obtenida con éxito.");
        response.put("tarea", tarea.get());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearTarea(@Valid @RequestBody Tarea tarea, BindingResult result) {
        Tarea nuevaTarea;
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            nuevaTarea = tareaService.guardarTarea(tarea);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar la tarea.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Tarea regitrada con éxito");
        response.put("tarea", nuevaTarea);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{idTarea}")
    public ResponseEntity<?> actualizarTarea(@RequestBody Tarea tarea, @PathVariable Long idTarea) {

        Optional<Tarea> tareaEncontradaOpt;
        Tarea tareaEncontrada;

        Map<String, Object> response = new HashMap<>();

        try {
            tareaEncontradaOpt = tareaService.obtenerPorId(idTarea);

            if(!tareaEncontradaOpt.isPresent()) {
                response.put("mensaje", "No existe tarea con ese ID");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            tareaEncontrada = tareaEncontradaOpt.get();
            tareaEncontrada.setNombreTarea(tarea.getNombreTarea());
            tareaEncontrada.setEstado(tarea.getEstado());

            tareaEncontrada = tareaService.guardarTarea(tareaEncontrada);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar la tarea.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Tarea actualizada con éxito");
        response.put("tarea", tareaEncontrada);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{idTarea}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long idTarea){
        Optional<Tarea> tareaEncontradaOpt;

        Map<String, Object> response = new HashMap<>();

        try {
            tareaEncontradaOpt = tareaService.obtenerPorId(idTarea);

            if(!tareaEncontradaOpt.isPresent()) {
                response.put("mensaje", "No existe tarea con ese ID");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            tareaService.eliminarTarea(idTarea);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar la tarea.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Se eliminó correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
