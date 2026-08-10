import { Component } from '@angular/core';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  menuOpen = false;
  settings$ = this.settingsService.settings$;

  constructor(private settingsService: WebsiteSettingsService) {}

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }
}
