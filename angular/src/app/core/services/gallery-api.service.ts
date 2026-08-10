import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { GalleryImage } from '../models/gallery-image.model';
import { PagedResponse, PageQuery } from '../models/page.model';
import { buildPageParams } from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class GalleryApiService {
  private baseUrl = `${environment.apiUrl}/gallery`;

  constructor(private http: HttpClient) {}

  search(filters: Record<string, unknown>, query: PageQuery): Observable<PagedResponse<GalleryImage>> {
    return this.http.get<PagedResponse<GalleryImage>>(`${this.baseUrl}/search`, {
      params: buildPageParams(query, filters)
    });
  }

  getAll(): Observable<GalleryImage[]> {
    return this.http.get<GalleryImage[]>(this.baseUrl);
  }

  create(image: GalleryImage): Observable<GalleryImage> {
    return this.http.post<GalleryImage>(this.baseUrl, image);
  }

  update(id: number, image: GalleryImage): Observable<GalleryImage> {
    return this.http.put<GalleryImage>(`${this.baseUrl}/${id}`, image);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
