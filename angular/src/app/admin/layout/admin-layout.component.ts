import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent {
  settings$ = this.settingsService.settings$;

  constructor(
    private authService: AuthService,
    private router: Router,
    private settingsService: WebsiteSettingsService
  ) {}

  get username(): string | null {
    return this.authService.getUsername();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/admin/login']);
  }
}
