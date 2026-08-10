import { Component, OnInit } from '@angular/core';
import { SalonService } from 'src/app/core/models/salon-service.model';
import { Testimonial } from 'src/app/core/models/testimonial.model';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';
import { TestimonialApiService } from 'src/app/core/services/testimonial-api.service';
import { WebsiteSettingsService } from 'src/app/core/services/website-settings.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  featuredServices: SalonService[] = [];
  testimonials: Testimonial[] = [];
  settings$ = this.settingsService.settings$;

  constructor(
    private serviceApi: SalonServiceApiService,
    private testimonialApi: TestimonialApiService,
    private settingsService: WebsiteSettingsService
  ) {}

  ngOnInit(): void {
    this.serviceApi.getAllActive().subscribe(services => {
      this.featuredServices = services.slice(0, 3);
    });
    this.testimonialApi.getApproved().subscribe(testimonials => {
      this.testimonials = testimonials.slice(0, 3);
    });
  }
}
