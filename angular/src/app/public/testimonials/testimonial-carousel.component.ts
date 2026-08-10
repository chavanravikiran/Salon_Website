import { Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { Testimonial } from 'src/app/core/models/testimonial.model';

const AUTO_ADVANCE_MS = 4000;

@Component({
  selector: 'app-testimonial-carousel',
  templateUrl: './testimonial-carousel.component.html'
})
export class TestimonialCarouselComponent implements OnInit, OnChanges, OnDestroy {
  @Input() testimonials: Testimonial[] = [];

  activeIndex = 0;
  private timer: ReturnType<typeof setInterval> | null = null;

  ngOnInit(): void {
    this.startAutoAdvance();
  }

  ngOnChanges(): void {
    if (this.activeIndex >= this.testimonials.length) {
      this.activeIndex = 0;
    }
  }

  ngOnDestroy(): void {
    this.stopAutoAdvance();
  }

  startAutoAdvance(): void {
    this.stopAutoAdvance();
    if (this.testimonials.length > 1) {
      this.timer = setInterval(() => this.next(), AUTO_ADVANCE_MS);
    }
  }

  stopAutoAdvance(): void {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  }

  next(): void {
    this.activeIndex = (this.activeIndex + 1) % this.testimonials.length;
  }

  prev(): void {
    this.activeIndex = (this.activeIndex - 1 + this.testimonials.length) % this.testimonials.length;
  }

  goTo(index: number): void {
    this.activeIndex = index;
    this.startAutoAdvance();
  }

  onManualNav(action: 'next' | 'prev'): void {
    action === 'next' ? this.next() : this.prev();
    this.startAutoAdvance();
  }

  stars(rating: number): string {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  }
}
