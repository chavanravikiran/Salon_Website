import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';

import { PublicRoutingModule } from './public-routing.module';
import { PublicLayoutComponent } from './layout/public-layout.component';
import { HeaderComponent } from './layout/header.component';
import { FooterComponent } from './layout/footer.component';
import { HomeComponent } from './home/home.component';
import { ServicesComponent } from './services/services.component';
import { StaffComponent } from './staff/staff.component';
import { GalleryComponent } from './gallery/gallery.component';
import { TestimonialsComponent } from './testimonials/testimonials.component';
import { TestimonialCarouselComponent } from './testimonials/testimonial-carousel.component';
import { BookingComponent } from './booking/booking.component';
import { ContactComponent } from './contact/contact.component';
import { ImageUrlPipe } from '../core/pipes/image-url.pipe';
import { RevealDirective } from '../core/directives/reveal.directive';

@NgModule({
  declarations: [
    PublicLayoutComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ServicesComponent,
    StaffComponent,
    GalleryComponent,
    TestimonialsComponent,
    TestimonialCarouselComponent,
    BookingComponent,
    ContactComponent,
    ImageUrlPipe,
    RevealDirective
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    NgxMaterialTimepickerModule
  ]
})
export class PublicModule {}
