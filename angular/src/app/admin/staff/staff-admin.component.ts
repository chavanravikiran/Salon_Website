import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { Staff } from 'src/app/core/models/staff.model';
import { StaffApiService } from 'src/app/core/services/staff-api.service';
import { UploadService } from 'src/app/core/services/upload.service';
import { FilterFieldConfig } from 'src/app/shared/components/filter-bar/filter-field.model';

@Component({
  selector: 'app-staff-admin',
  templateUrl: './staff-admin.component.html'
})
export class StaffAdminComponent implements OnInit {
  staff: Staff[] = [];
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
    { key: 'specialty', label: 'Specialty', type: 'text' },
    { key: 'active', label: 'Active', type: 'boolean' }
  ];

  constructor(
    private staffApi: StaffApiService,
    private uploadService: UploadService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      specialty: [''],
      bio: [''],
      photoUrl: [''],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.staffApi
      .search(this.filters, { page: this.pageIndex, size: this.pageSize, sortBy: 'id', sortDir: 'desc' })
      .subscribe(result => {
        this.staff = result.content;
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
    return this.uploadService.resolveUrl(url) || 'https://placehold.co/80x80?text=No+Photo';
  }

  newMember(): void {
    this.editingId = null;
    this.form.reset({ active: true });
    this.showForm = true;
  }

  edit(member: Staff): void {
    this.editingId = member.id ?? null;
    this.form.reset(member);
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
        this.form.patchValue({ photoUrl: res.url });
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
      ? this.staffApi.update(this.editingId, payload)
      : this.staffApi.create(payload);

    request.subscribe({
      next: () => {
        this.showForm = false;
        this.load();
      },
      error: () => (this.errorMessage = 'Could not save this staff member.')
    });
  }

  remove(member: Staff): void {
    if (!member.id || !confirm(`Delete staff member "${member.name}"?`)) {
      return;
    }
    this.staffApi.delete(member.id).subscribe(() => this.load());
  }
}
