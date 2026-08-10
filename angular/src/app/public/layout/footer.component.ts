import { Component } from '@angular/core';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent {
  year = new Date().getFullYear();
  settings$ = this.settingsService.settings$;

  constructor(private settingsService: WebsiteSettingsService) {}
}
