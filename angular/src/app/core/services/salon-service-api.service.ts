import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SalonService } from '../models/salon-service.model';
import { PagedResponse, PageQuery } from '../models/page.model';
import { buildPageParams } from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class SalonServiceApiService {
  private baseUrl = `${environment.apiUrl}/services`;

  constructor(private http: HttpClient) {}

  search(filters: Record<string, unknown>, query: PageQuery): Observable<PagedResponse<SalonService>> {
    return this.http.get<PagedResponse<SalonService>>(`${this.baseUrl}/search`, {
      params: buildPageParams(query, filters)
    });
  }

  getAllActive(): Observable<SalonService[]> {
    return this.http.get<SalonService[]>(this.baseUrl);
  }

  getAll(): Observable<SalonService[]> {
    return this.http.get<SalonService[]>(this.baseUrl, { params: { all: 'true' } });
  }

  getById(id: number): Observable<SalonService> {
    return this.http.get<SalonService>(`${this.baseUrl}/${id}`);
  }

  create(service: SalonService): Observable<SalonService> {
    return this.http.post<SalonService>(this.baseUrl, service);
  }

  update(id: number, service: SalonService): Observable<SalonService> {
    return this.http.put<SalonService>(`${this.baseUrl}/${id}`, service);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
