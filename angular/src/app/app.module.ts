import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { catchError, firstValueFrom, of } from 'rxjs';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { WebsiteSettingsService } from './core/services/website-settings.service';

function initWebsiteSettings(settingsService: WebsiteSettingsService): () => Promise<unknown> {
  return () => firstValueFrom(settingsService.load().pipe(catchError(() => of(null))));
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: APP_INITIALIZER, useFactory: initWebsiteSettings, deps: [WebsiteSettingsService], multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
