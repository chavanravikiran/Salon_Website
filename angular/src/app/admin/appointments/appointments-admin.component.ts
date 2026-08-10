import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Appointment, AppointmentStatus } from 'src/app/core/models/appointment.model';
import { AppointmentApiService } from 'src/app/core/services/appointment-api.service';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';
import { StaffApiService } from 'src/app/core/services/staff-api.service';
import { FilterFieldConfig } from 'src/app/shared/components/filter-bar/filter-field.model';

@Component({
  selector: 'app-appointments-admin',
  templateUrl: './appointments-admin.component.html'
})
export class AppointmentsAdminComponent implements OnInit {
  appointments: Appointment[] = [];
  statuses: AppointmentStatus[] = ['PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED'];

  filters: Record<string, unknown> = {};
  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  filterFields: FilterFieldConfig[] = [
    { key: 'customer', label: 'Customer', type: 'text' },
    {
      key: 'status', label: 'Status', type: 'select',
      options: this.statuses.map(s => ({ label: s, value: s }))
    },
    { key: 'serviceId', label: 'Service', type: 'select', options: [] },
    { key: 'staffId', label: 'Stylist', type: 'select', options: [] },
    { key: 'from', label: 'From', type: 'date' },
    { key: 'to', label: 'To', type: 'date' }
  ];

  constructor(
    private appointmentApi: AppointmentApiService,
    private serviceApi: SalonServiceApiService,
    private staffApi: StaffApiService
  ) {}

  ngOnInit(): void {
    this.load();
    this.serviceApi.getAll().subscribe(services => {
      const serviceField = this.filterFields.find(f => f.key === 'serviceId');
      if (serviceField) {
        serviceField.options = services.map(s => ({ label: s.name, value: s.id as number }));
      }
    });
    this.staffApi.getAll().subscribe(staff => {
      const staffField = this.filterFields.find(f => f.key === 'staffId');
      if (staffField) {
        staffField.options = staff.map(s => ({ label: s.name, value: s.id as number }));
      }
    });
  }

  load(): void {
    this.appointmentApi
      .search(this.filters, { page: this.pageIndex, size: this.pageSize, sortBy: 'appointmentDate', sortDir: 'asc' })
      .subscribe(result => {
        this.appointments = result.content;
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

  badgeClass(status?: AppointmentStatus): string {
    switch (status) {
      case 'CONFIRMED': return 'badge-confirmed';
      case 'CANCELLED': return 'badge-cancelled';
      case 'COMPLETED': return 'badge-completed';
      default: return 'badge-pending';
    }
  }

  updateStatus(appointment: Appointment, status: AppointmentStatus): void {
    if (!appointment.id) {
      return;
    }
    this.appointmentApi.updateStatus(appointment.id, status).subscribe(() => this.load());
  }

  remove(appointment: Appointment): void {
    if (!appointment.id || !confirm(`Delete appointment for "${appointment.customerName}"?`)) {
      return;
    }
    this.appointmentApi.delete(appointment.id).subscribe(() => this.load());
  }
}
