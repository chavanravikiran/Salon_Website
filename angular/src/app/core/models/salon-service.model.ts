export interface SalonService {
  id?: number;
  name: string;
  description?: string;
  price: number;
  durationMinutes: number;
  category?: string;
  imageUrl?: string;
  active: boolean;
}
