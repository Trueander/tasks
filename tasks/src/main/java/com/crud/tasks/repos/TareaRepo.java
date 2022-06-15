package com.crud.tasks.repos;

import com.crud.tasks.models.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TareaRepo extends CrudRepository<Tarea, Long> {
}
