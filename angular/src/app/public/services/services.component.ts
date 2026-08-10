import { Component, OnInit } from '@angular/core';
import { SalonService } from 'src/app/core/models/salon-service.model';
import { SalonServiceApiService } from 'src/app/core/services/salon-service-api.service';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html'
})
export class ServicesComponent implements OnInit {
  services: SalonService[] = [];

  constructor(private serviceApi: SalonServiceApiService) {}

  ngOnInit(): void {
    this.serviceApi.getAllActive().subscribe(services => (this.services = services));
  }
}
