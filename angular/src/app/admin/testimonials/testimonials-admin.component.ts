import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Testimonial } from 'src/app/core/models/testimonial.model';
import { TestimonialApiService } from 'src/app/core/services/testimonial-api.service';
import { FilterFieldConfig } from 'src/app/shared/components/filter-bar/filter-field.model';

@Component({
  selector: 'app-testimonials-admin',
  templateUrl: './testimonials-admin.component.html'
})
export class TestimonialsAdminComponent implements OnInit {
  testimonials: Testimonial[] = [];

  filters: Record<string, unknown> = {};
  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  filterFields: FilterFieldConfig[] = [
    { key: 'customerName', label: 'Customer', type: 'text' },
    { key: 'approved', label: 'Approved', type: 'boolean' },
    {
      key: 'minRating', label: 'Min Rating', type: 'select',
      options: [1, 2, 3, 4, 5].map(n => ({ label: `${n}+`, value: n }))
    },
    { key: 'from', label: 'From', type: 'date' },
    { key: 'to', label: 'To', type: 'date' }
  ];

  constructor(private testimonialApi: TestimonialApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.testimonialApi
      .search(this.filters, { page: this.pageIndex, size: this.pageSize, sortBy: 'createdAt', sortDir: 'desc' })
      .subscribe(result => {
        this.testimonials = result.content;
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

  stars(rating: number): string {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  }

  toggleApproval(testimonial: Testimonial): void {
    if (!testimonial.id) {
      return;
    }
    this.testimonialApi.setApproved(testimonial.id, !testimonial.approved).subscribe(() => this.load());
  }

  remove(testimonial: Testimonial): void {
    if (!testimonial.id || !confirm(`Delete review from "${testimonial.customerName}"?`)) {
      return;
    }
    this.testimonialApi.delete(testimonial.id).subscribe(() => this.load());
  }
}
