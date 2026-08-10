import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { SalonService } from 'src/app/core/models/salon-service.model';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';
import { UploadService } from 'src/app/core/services/upload.service';
import { FilterFieldConfig } from 'src/app/shared/components/filter-bar/filter-field.model';

@Component({
  selector: 'app-services-admin',
  templateUrl: './services-admin.component.html'
})
export class ServicesAdminComponent implements OnInit {
  services: SalonService[] = [];
  form: FormGroup;
  editingId: number | null = null;
  showForm = false;
  uploading = false;
  errorMessage = '';

  filters: Record<string, unknown> = {};
  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  filterFields: FilterFieldConfig[] = [
    { key: 'name', label: 'Name', type: 'text' },
    { key: 'category', label: 'Category', type: 'text' },
    { key: 'active', label: 'Active', type: 'boolean' },
    { key: 'minPrice', label: 'Min Price', type: 'number' },
    { key: 'maxPrice', label: 'Max Price', type: 'number' }
  ];

  constructor(
    private serviceApi: SalonServiceApiService,
    private uploadService: UploadService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(0)]],
      durationMinutes: [30, [Validators.required, Validators.min(1)]],
      category: [''],
      imageUrl: [''],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.serviceApi
      .search(this.filters, { page: this.pageIndex, size: this.pageSize, sortBy: 'id', sortDir: 'desc' })
      .subscribe(result => {
        this.services = result.content;
        this.totalElements = result.totalElements;
      });
  }

  onFilterChange(filters: Record<string, unknown>): void {
    this.filters = filters;
    this.pageIndex = 0;
    this.load();
  }

  onPage(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load();
  }

  resolveImage(url: string | undefined): string {
    return this.uploadService.resolveUrl(url) || 'https://placehold.co/80x80?text=No+Image';
  }

  newService(): void {
    this.editingId = null;
    this.form.reset({ price: 0, durationMinutes: 30, active: true });
    this.showForm = true;
  }

  edit(service: SalonService): void {
    this.editingId = service.id ?? null;
    this.form.reset(service);
    this.showForm = true;
  }

  cancel(): void {
    this.showForm = false;
    this.errorMessage = '';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }
    this.uploading = true;
    this.uploadService.upload(input.files[0]).subscribe({
      next: res => {
        this.form.patchValue({ imageUrl: res.url });
        this.uploading = false;
      },
      error: () => {
        this.errorMessage = 'Image upload failed.';
        this.uploading = false;
      }
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.value;
    const request = this.editingId
      ? this.serviceApi.update(this.editingId, payload)
      : this.serviceApi.create(payload);

    request.subscribe({
      next: () => {
        this.showForm = false;
        this.load();
      },
      error: () => (this.errorMessage = 'Could not save this service.')
    });
  }

  remove(service: SalonService): void {
    if (!service.id || !confirm(`Delete service "${service.name}"?`)) {
      return;
    }
    this.serviceApi.delete(service.id).subscribe(() => this.load());
  }
}
