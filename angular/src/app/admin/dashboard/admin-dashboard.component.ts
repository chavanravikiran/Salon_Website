import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AppointmentApiService } from 'src/app/core/services/appointment-api.service';
import { GalleryApiService } from 'src/app/core/services/gallery-api.service';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';
import { StaffApiService } from 'src/app/core/services/staff-api.service';
import { TestimonialApiService } from 'src/app/core/services/testimonial-api.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit {
  serviceCount = 0;
  staffCount = 0;
  galleryCount = 0;
  pendingAppointments = 0;
  pendingTestimonials = 0;

  constructor(
    private serviceApi: SalonServiceApiService,
    private staffApi: StaffApiService,
    private galleryApi: GalleryApiService,
    private appointmentApi: AppointmentApiService,
    private testimonialApi: TestimonialApiService
  ) {}

  ngOnInit(): void {
    forkJoin({
      services: this.serviceApi.getAll(),
      staff: this.staffApi.getAll(),
      gallery: this.galleryApi.getAll(),
      appointments: this.appointmentApi.getAll(),
      testimonials: this.testimonialApi.getAll()
    }).subscribe(({ services, staff, gallery, appointments, testimonials }) => {
      this.serviceCount = services.length;
      this.staffCount = staff.length;
      this.galleryCount = gallery.length;
      this.pendingAppointments = appointments.filter(a => a.status === 'PENDING').length;
      this.pendingTestimonials = testimonials.filter(t => !t.approved).length;
    });
  }
}
