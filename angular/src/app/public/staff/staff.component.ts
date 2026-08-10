import { Component, OnInit } from '@angular/core';
import { Staff } from 'src/app/core/models/staff.model';
import { StaffApiService } from 'src/app/core/services/staff-api.service';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html'
})
export class StaffComponent implements OnInit {
  staff: Staff[] = [];

  constructor(private staffApi: StaffApiService) {}

  ngOnInit(): void {
    this.staffApi.getAllActive().subscribe(staff => (this.staff = staff));
  }
}
