import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { WebsiteSettings, WebsiteSettingsRequest } from '../models/website-settings.model';
import { UploadService } from './upload.service';

@Injectable({ providedIn: 'root' })
export class WebsiteSettingsService {
  private baseUrl = `${environment.apiUrl}/settings`;
  private settingsSubject = new BehaviorSubject<WebsiteSettings | null>(null);
  settings$ = this.settingsSubject.asObservable();

  constructor(
    private http: HttpClient,
    private titleService: Title,
    private uploadService: UploadService
  ) {}

  get current(): WebsiteSettings | null {
    return this.settingsSubject.value;
  }

  load(): Observable<WebsiteSettings> {
    return this.http.get<WebsiteSettings>(this.baseUrl).pipe(tap(settings => this.apply(settings)));
  }

  update(request: WebsiteSettingsRequest): Observable<WebsiteSettings> {
    return this.http.put<WebsiteSettings>(this.baseUrl, request).pipe(tap(settings => this.apply(settings)));
  }

  uploadLogo(file: File): Observable<WebsiteSettings> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<WebsiteSettings>(`${this.baseUrl}/logo`, formData).pipe(tap(settings => this.apply(settings)));
  }

  deleteLogo(): Observable<WebsiteSettings> {
    return this.http.delete<WebsiteSettings>(`${this.baseUrl}/logo`).pipe(tap(settings => this.apply(settings)));
  }

  uploadFavicon(file: File): Observable<WebsiteSettings> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<WebsiteSettings>(`${this.baseUrl}/favicon`, formData).pipe(tap(settings => this.apply(settings)));
  }

  deleteFavicon(): Observable<WebsiteSettings> {
    return this.http.delete<WebsiteSettings>(`${this.baseUrl}/favicon`).pipe(tap(settings => this.apply(settings)));
  }

  uploadHeroMedia(file: File): Observable<WebsiteSettings> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<WebsiteSettings>(`${this.baseUrl}/hero-media`, formData).pipe(tap(settings => this.apply(settings)));
  }

  deleteHeroMedia(): Observable<WebsiteSettings> {
    return this.http.delete<WebsiteSettings>(`${this.baseUrl}/hero-media`).pipe(tap(settings => this.apply(settings)));
  }

  private apply(settings: WebsiteSettings): void {
    this.settingsSubject.next(settings);
    this.titleService.setTitle(settings.websiteName);
    if (settings.faviconImage) {
      this.setFavicon(this.uploadService.resolveUrl(settings.faviconImage));
    }
  }

  /**
   * Some browsers (notably Safari, and Chrome in some cases) don't refresh the tab icon
   * when an existing <link rel="icon"> element's href is mutated in place - removing and
   * re-inserting the element is the reliable cross-browser way to force a refetch.
   */
  private setFavicon(url: string): void {
    const existing = document.getElementById('app-favicon');
    if (existing) {
      existing.remove();
    }
    const link = document.createElement('link');
    link.id = 'app-favicon';
    link.rel = 'icon';
    link.href = url;
    document.head.appendChild(link);
  }
}
