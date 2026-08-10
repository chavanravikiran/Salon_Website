import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Appointment, AppointmentStatus } from '../models/appointment.model';
import { PagedResponse, PageQuery } from '../models/page.model';
import { buildPageParams } from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class AppointmentApiService {
  private baseUrl = `${environment.apiUrl}/appointments`;

  constructor(private http: HttpClient) {}

  search(filters: Record<string, unknown>, query: PageQuery): Observable<PagedResponse<Appointment>> {
    return this.http.get<PagedResponse<Appointment>>(`${this.baseUrl}/search`, {
      params: buildPageParams(query, filters)
    });
  }

  getAll(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.baseUrl);
  }

  create(appointment: Appointment): Observable<Appointment> {
    return this.http.post<Appointment>(this.baseUrl, appointment);
  }

  updateStatus(id: number, status: AppointmentStatus): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.baseUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
