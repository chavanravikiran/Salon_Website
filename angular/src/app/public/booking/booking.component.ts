import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SalonService } from 'src/app/core/models/salon-service.model';
import { Staff } from 'src/app/core/models/staff.model';
import { AppointmentApiService } from 'src/app/core/services/appointment-api.service';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';
import { StaffApiService } from 'src/app/core/services/staff-api.service';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html'
})
export class BookingComponent implements OnInit {
  services: SalonService[] = [];
  staff: Staff[] = [];
  form: FormGroup;
  submitted = false;
  submitting = false;
  errorMessage = '';
  minDate = new Date().toISOString().split('T')[0];

  constructor(
    private serviceApi: SalonServiceApiService,
    private staffApi: StaffApiService,
    private appointmentApi: AppointmentApiService,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      customerName: ['', Validators.required],
      customerEmail: ['', [Validators.required, Validators.email]],
      customerPhone: ['', Validators.required],
      serviceId: [null, Validators.required],
      staffId: [null],
      appointmentDate: ['', Validators.required],
      appointmentTime: ['', Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.serviceApi.getAllActive().subscribe(services => (this.services = services));
    this.staffApi.getAllActive().subscribe(staff => (this.staff = staff));

    const preselected = this.route.snapshot.queryParamMap.get('serviceId');
    if (preselected) {
      this.form.patchValue({ serviceId: Number(preselected) });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.errorMessage = '';
    this.appointmentApi.create(this.form.value).subscribe({
      next: () => {
        this.submitted = true;
        this.submitting = false;
      },
      error: () => {
        this.errorMessage = 'Could not submit your booking. Please check the details and try again.';
        this.submitting = false;
      }
    });
  }
}
