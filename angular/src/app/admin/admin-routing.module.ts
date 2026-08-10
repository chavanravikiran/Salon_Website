import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth.guard';
import { AdminLoginComponent } from './login/admin-login.component';
import { AdminLayoutComponent } from './layout/admin-layout.component';
import { AdminDashboardComponent } from './dashboard/admin-dashboard.component';
import { ServicesAdminComponent } from './services/services-admin.component';
import { StaffAdminComponent } from './staff/staff-admin.component';
import { GalleryAdminComponent } from './gallery/gallery-admin.component';
import { TestimonialsAdminComponent } from './testimonials/testimonials-admin.component';
import { AppointmentsAdminComponent } from './appointments/appointments-admin.component';
import { SettingsAdminComponent } from './settings/settings-admin.component';

const routes: Routes = [
  { path: 'login', component: AdminLoginComponent },
  {
    path: '',
    component: AdminLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'services', component: ServicesAdminComponent },
      { path: 'staff', component: StaffAdminComponent },
      { path: 'gallery', component: GalleryAdminComponent },
      { path: 'testimonials', component: TestimonialsAdminComponent },
      { path: 'appointments', component: AppointmentsAdminComponent },
      { path: 'settings', component: SettingsAdminComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
