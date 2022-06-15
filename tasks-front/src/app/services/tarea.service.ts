import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tarea } from '../models/tarea';

@Injectable({
  providedIn: 'root'
})
export class TareaService {

  private urlBackend: string = 'http://localhost:8080/api/v0/tareas';

  constructor(private http: HttpClient) { }

  obtenerTareas(): Observable<any> {
    return this.http.get<any>(`${this.urlBackend}/listar`);
  }

  actualizarTarea(tarea: Tarea): Observable<any> {
    return this.http.put<any>(`${this.urlBackend}/actualizar/${tarea.idTarea}`, tarea);
  }

  eliminarTarea(tareaId: number): Observable<any> {
    return this.http.delete<any>(`${this.urlBackend}/eliminar/${tareaId}`);
  }

  crearTarea(tarea: Tarea): Observable<any> {
    return this.http.post<any>(`${this.urlBackend}/crear`, tarea);
  }
}
