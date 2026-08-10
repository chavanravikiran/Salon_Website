import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Testimonial } from '../models/testimonial.model';
import { PagedResponse, PageQuery } from '../models/page.model';
import { buildPageParams } from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class TestimonialApiService {
  private baseUrl = `${environment.apiUrl}/testimonials`;

  constructor(private http: HttpClient) {}

  search(filters: Record<string, unknown>, query: PageQuery): Observable<PagedResponse<Testimonial>> {
    return this.http.get<PagedResponse<Testimonial>>(`${this.baseUrl}/search`, {
      params: buildPageParams(query, filters)
    });
  }

  getApproved(): Observable<Testimonial[]> {
    return this.http.get<Testimonial[]>(`${this.baseUrl}/public`);
  }

  getAll(): Observable<Testimonial[]> {
    return this.http.get<Testimonial[]>(this.baseUrl);
  }

  submit(testimonial: Testimonial): Observable<Testimonial> {
    return this.http.post<Testimonial>(this.baseUrl, testimonial);
  }

  setApproved(id: number, approved: boolean): Observable<Testimonial> {
    return this.http.patch<Testimonial>(`${this.baseUrl}/${id}/approve`, null, { params: { approved: String(approved) } });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
