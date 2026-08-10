import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { WebsiteSettings } from 'src/app/core/models/website-settings.model';
import { UploadService } from 'src/app/core/services/upload.service';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

@Component({
  selector: 'app-settings-admin',
  templateUrl: './settings-admin.component.html'
})
export class SettingsAdminComponent implements OnInit {
  form: FormGroup;
  settings: WebsiteSettings | null = null;
  uploadingLogo = false;
  uploadingFavicon = false;
  uploadingHeroMedia = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private settingsService: WebsiteSettingsService,
    private uploadService: UploadService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      websiteName: ['', Validators.required],
      phone: [''],
      email: [''],
      address: [''],
      description: [''],
      businessHours: [''],
      facebookUrl: [''],
      instagramUrl: [''],
      twitterUrl: [''],
      youtubeUrl: [''],
      googleMapEmbedUrl: ['']
    });
  }

  ngOnInit(): void {
    this.settingsService.load().subscribe(settings => this.applySettings(settings));
  }

  resolveImage(url: string | null | undefined): string {
    return this.uploadService.resolveUrl(url) || '';
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.errorMessage = '';
    this.successMessage = '';
    this.settingsService.update(this.form.value).subscribe({
      next: settings => {
        this.applySettings(settings);
        this.successMessage = 'Settings updated.';
      },
      error: (err: HttpErrorResponse) => (this.errorMessage = this.describeError(err, 'Could not save settings.'))
    });
  }

  onLogoSelected(event: Event): void {
    const file = this.getFile(event);
    if (!file) {
      return;
    }
    this.errorMessage = '';
    this.uploadingLogo = true;
    this.settingsService.uploadLogo(file).subscribe({
      next: settings => {
        this.applySettings(settings);
        this.uploadingLogo = false;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = this.describeError(err, 'Logo upload failed.');
        this.uploadingLogo = false;
      }
    });
  }

  deleteLogo(): void {
    if (!confirm('Remove the current logo?')) {
      return;
    }
    this.settingsService.deleteLogo().subscribe(settings => this.applySettings(settings));
  }

  onFaviconSelected(event: Event): void {
    const file = this.getFile(event);
    if (!file) {
      return;
    }
    this.errorMessage = '';
    this.uploadingFavicon = true;
    this.settingsService.uploadFavicon(file).subscribe({
      next: settings => {
        this.applySettings(settings);
        this.uploadingFavicon = false;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = this.describeError(err, 'Favicon upload failed.');
        this.uploadingFavicon = false;
      }
    });
  }

  deleteFavicon(): void {
    if (!confirm('Remove the current favicon?')) {
      return;
    }
    this.settingsService.deleteFavicon().subscribe(settings => this.applySettings(settings));
  }

  onHeroMediaSelected(event: Event): void {
    const file = this.getFile(event);
    if (!file) {
      return;
    }
    this.errorMessage = '';
    this.uploadingHeroMedia = true;
    this.settingsService.uploadHeroMedia(file).subscribe({
      next: settings => {
        this.applySettings(settings);
        this.uploadingHeroMedia = false;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = this.describeError(err, 'Home background upload failed.');
        this.uploadingHeroMedia = false;
      }
    });
  }

  deleteHeroMedia(): void {
    if (!confirm('Remove the current home page background?')) {
      return;
    }
    this.settingsService.deleteHeroMedia().subscribe(settings => this.applySettings(settings));
  }

  private describeError(err: HttpErrorResponse, fallback: string): string {
    return err.error?.message || fallback;
  }

  private applySettings(settings: WebsiteSettings): void {
    this.settings = settings;
    this.form.patchValue(settings);
  }

  private getFile(event: Event): File | null {
    const input = event.target as HTMLInputElement;
    return input.files && input.files.length ? input.files[0] : null;
  }
}
