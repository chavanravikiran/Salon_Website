import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class UploadService {
  constructor(private http: HttpClient) {}

  upload(file: File): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ url: string }>(`${environment.apiUrl}/uploads`, formData);
  }

  resolveUrl(path: string | undefined | null): string {
    if (!path) {
      return '';
    }
    if (path.startsWith('http')) {
      return path;
    }
    return `${environment.apiUrl.replace(/\/api$/, '')}${path}`;
  }
}
