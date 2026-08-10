import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Testimonial } from 'src/app/core/models/testimonial.model';
import { TestimonialApiService } from 'src/app/core/services/testimonial-api.service';

@Component({
  selector: 'app-testimonials',
  templateUrl: './testimonials.component.html'
})
export class TestimonialsComponent implements OnInit {
  testimonials: Testimonial[] = [];
  form: FormGroup;
  submitted = false;
  submitting = false;
  errorMessage = '';

  ratingOptions = [
    { value: 1, emoji: '😞', label: 'Poor' },
    { value: 2, emoji: '😕', label: 'Fair' },
    { value: 3, emoji: '🙂', label: 'Good' },
    { value: 4, emoji: '😃', label: 'Great' },
    { value: 5, emoji: '🤩', label: 'Excellent' }
  ];

  constructor(private testimonialApi: TestimonialApiService, private fb: FormBuilder) {
    this.form = this.fb.group({
      customerName: ['', Validators.required],
      rating: [5, Validators.required],
      reviewText: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.testimonialApi.getApproved().subscribe(testimonials => (this.testimonials = testimonials));
  }

  setRating(value: number): void {
    this.form.patchValue({ rating: value });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.errorMessage = '';
    this.testimonialApi.submit(this.form.value).subscribe({
      next: () => {
        this.submitted = true;
        this.submitting = false;
        this.form.reset({ rating: 5 });
      },
      error: () => {
        this.errorMessage = 'Something went wrong. Please try again.';
        this.submitting = false;
      }
    });
  }
}
