import { Component } from '@angular/core';
import { Tarea } from './models/tarea';
import { TareaService } from './services/tarea.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  tareas: Tarea[] = [];
  tareaSeleccionada!: Tarea;
  tareaNueva: Tarea = new Tarea();

  modal: boolean = false;

  mensajeError: string = '';

  constructor(private tareaService: TareaService) {
    this.tareaService.obtenerTareas()
        .subscribe(response => {
          this.tareas = response.tareas;
          this.tareas.forEach(t=> t.inputActivado = false)
        })
  }

  activarEdicion(tarea: Tarea){
    tarea.inputActivado = true;
  }

  crearTarea() {
    this.tareaService.crearTarea(this.tareaNueva)
        .subscribe(response => {
          this.tareas.push(response.tarea);
          this.tareaNueva = new Tarea();
        }, err => {
          this.mensajeError = err.error.errors[0];
        })
  }

  actualizarTarea(tarea: Tarea){
    this.tareaService.actualizarTarea(tarea)
        .subscribe(response => {
          tarea.nombreTarea = response.tarea.nombreTarea;
          tarea.inputActivado = false;
        })
  }

  actualizarEstado(tarea: Tarea) {
    tarea.estado = !tarea.estado;
    this.tareaService.actualizarTarea(tarea)
        .subscribe()
  }

  abrirModal(tarea: Tarea){
    this.tareaSeleccionada = tarea;
    this.modal = true;
  }

  cerrarModal(){
    this.modal = false;
  }

  eliminarTarea() {
    
      this.tareaService.eliminarTarea(this.tareaSeleccionada.idTarea)
          .subscribe(response => {
            this.modal = false;
            this.tareas = this.tareas.filter(t => t.idTarea != this.tareaSeleccionada.idTarea)
          });
  }
}
