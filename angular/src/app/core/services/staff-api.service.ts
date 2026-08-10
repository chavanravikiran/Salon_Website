import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Staff } from '../models/staff.model';
import { PagedResponse, PageQuery } from '../models/page.model';
import { buildPageParams } from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class StaffApiService {
  private baseUrl = `${environment.apiUrl}/staff`;

  constructor(private http: HttpClient) {}

  search(filters: Record<string, unknown>, query: PageQuery): Observable<PagedResponse<Staff>> {
    return this.http.get<PagedResponse<Staff>>(`${this.baseUrl}/search`, {
      params: buildPageParams(query, filters)
    });
  }

  getAllActive(): Observable<Staff[]> {
    return this.http.get<Staff[]>(this.baseUrl);
  }

  getAll(): Observable<Staff[]> {
    return this.http.get<Staff[]>(this.baseUrl, { params: { all: 'true' } });
  }

  getById(id: number): Observable<Staff> {
    return this.http.get<Staff>(`${this.baseUrl}/${id}`);
  }

  create(staff: Staff): Observable<Staff> {
    return this.http.post<Staff>(this.baseUrl, staff);
  }

  update(id: number, staff: Staff): Observable<Staff> {
    return this.http.put<Staff>(`${this.baseUrl}/${id}`, staff);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
