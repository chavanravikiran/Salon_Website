export type AppointmentStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';

export interface Appointment {
  id?: number;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  serviceId: number;
  serviceName?: string;
  staffId?: number | null;
  staffName?: string;
  appointmentDate: string;
  appointmentTime: string;
  status?: AppointmentStatus;
  notes?: string;
  createdAt?: string;
}
