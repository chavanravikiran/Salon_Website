import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SharedModule } from '../shared/shared.module';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminLoginComponent } from './login/admin-login.component';
import { AdminLayoutComponent } from './layout/admin-layout.component';
import { AdminDashboardComponent } from './dashboard/admin-dashboard.component';
import { ServicesAdminComponent } from './services/services-admin.component';
import { StaffAdminComponent } from './staff/staff-admin.component';
import { GalleryAdminComponent } from './gallery/gallery-admin.component';
import { TestimonialsAdminComponent } from './testimonials/testimonials-admin.component';
import { AppointmentsAdminComponent } from './appointments/appointments-admin.component';
import { SettingsAdminComponent } from './settings/settings-admin.component';

@NgModule({
  declarations: [
    AdminLoginComponent,
    AdminLayoutComponent,
    AdminDashboardComponent,
    ServicesAdminComponent,
    StaffAdminComponent,
    GalleryAdminComponent,
    TestimonialsAdminComponent,
    AppointmentsAdminComponent,
    SettingsAdminComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    SharedModule
  ]
})
export class AdminModule {}
