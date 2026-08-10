export interface Testimonial {
  id?: number;
  customerName: string;
  rating: number;
  reviewText: string;
  createdAt?: string;
  approved?: boolean;
}
