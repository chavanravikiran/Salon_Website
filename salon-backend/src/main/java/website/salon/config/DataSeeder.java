package website.salon.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import website.salon.entity.*;
import website.salon.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@Order(2)
public class DataSeeder implements CommandLineRunner {

    private final SalonServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final GalleryImageRepository galleryImageRepository;
    private final TestimonialRepository testimonialRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSeeder(SalonServiceRepository serviceRepository,
                       StaffRepository staffRepository,
                       GalleryImageRepository galleryImageRepository,
                       TestimonialRepository testimonialRepository,
                       AppointmentRepository appointmentRepository) {
        this.serviceRepository = serviceRepository;
        this.staffRepository = staffRepository;
        this.galleryImageRepository = galleryImageRepository;
        this.testimonialRepository = testimonialRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        List<SalonService> services = seedServices();
        List<Staff> staff = seedStaff();
        seedGallery();
        seedTestimonials();
        seedAppointments(services, staff);
    }

    private List<SalonService> seedServices() {
        if (serviceRepository.count() > 0) {
            return serviceRepository.findAll();
        }
        List<SalonService> services = List.of(
                service("Signature Haircut & Style",
                        "A precision haircut tailored to your face shape, finished with a professional blowout.",
                        new BigDecimal("45.00"), 45, "Hair", "/uploads/service-haircut.jpg"),
                service("Full Hair Coloring",
                        "Complete color transformation using premium, ammonia-free color.",
                        new BigDecimal("95.00"), 120, "Hair", "/uploads/service-haircolor.jpg"),
                service("Deep Conditioning Treatment",
                        "Intensive moisture treatment to repair and strengthen damaged hair.",
                        new BigDecimal("35.00"), 30, "Hair", "/uploads/service-conditioning.jpg"),
                service("Classic Manicure",
                        "Nail shaping, cuticle care and polish of your choice.",
                        new BigDecimal("25.00"), 30, "Nails", "/uploads/service-manicure.jpg"),
                service("Signature Facial",
                        "Deep-cleansing facial customized to your skin type.",
                        new BigDecimal("65.00"), 60, "Skincare", "/uploads/service-facial.jpg"),
                service("Relaxing Full Body Massage",
                        "A calming full-body massage to relieve tension and stress.",
                        new BigDecimal("80.00"), 60, "Spa", "/uploads/service-massage.jpg")
        );
        return serviceRepository.saveAll(services);
    }

    private SalonService service(String name, String description, BigDecimal price, int duration, String category, String imageUrl) {
        SalonService s = new SalonService();
        s.setName(name);
        s.setDescription(description);
        s.setPrice(price);
        s.setDurationMinutes(duration);
        s.setCategory(category);
        s.setImageUrl(imageUrl);
        s.setActive(true);
        return s;
    }

    private List<Staff> seedStaff() {
        if (staffRepository.count() > 0) {
            return staffRepository.findAll();
        }
        List<Staff> staff = List.of(
                staffMember("Ava Bennett", "Senior Hair Stylist",
                        "With over 10 years of experience, Ava specializes in precision cuts and modern styling.",
                        "/uploads/staff-1.jpg"),
                staffMember("Liam Carter", "Color Specialist",
                        "Liam is passionate about color theory and creates stunning, dimensional looks for every client.",
                        "/uploads/staff-2.jpg"),
                staffMember("Sophia Martinez", "Esthetician",
                        "Sophia focuses on personalized skincare routines and rejuvenating facial treatments.",
                        "/uploads/staff-3.jpg"),
                staffMember("Noah Thompson", "Massage Therapist",
                        "Noah brings a calm, therapeutic touch to every session, helping clients unwind completely.",
                        "/uploads/staff-4.jpg")
        );
        return staffRepository.saveAll(staff);
    }

    private Staff staffMember(String name, String specialty, String bio, String photoUrl) {
        Staff s = new Staff();
        s.setName(name);
        s.setSpecialty(specialty);
        s.setBio(bio);
        s.setPhotoUrl(photoUrl);
        s.setActive(true);
        return s;
    }

    private void seedGallery() {
        if (galleryImageRepository.count() > 0) {
            return;
        }
        List<GalleryImage> images = List.of(
                galleryImage("Salon Reception", "Interior", "/uploads/gallery-1.jpg"),
                galleryImage("Styling Station", "Interior", "/uploads/gallery-2.jpg"),
                galleryImage("Balayage Transformation", "Hair", "/uploads/gallery-3.jpg"),
                galleryImage("Bridal Updo", "Hair", "/uploads/gallery-4.jpg"),
                galleryImage("Gel Nail Art", "Nails", "/uploads/gallery-5.jpg"),
                galleryImage("Relaxation Room", "Spa", "/uploads/gallery-6.jpg"),
                galleryImage("Facial Treatment in Progress", "Skincare", "/uploads/gallery-7.jpg"),
                galleryImage("Client Consultation", "Interior", "/uploads/gallery-8.jpg")
        );
        galleryImageRepository.saveAll(images);
    }

    private GalleryImage galleryImage(String title, String category, String imageUrl) {
        GalleryImage g = new GalleryImage();
        g.setTitle(title);
        g.setCategory(category);
        g.setImageUrl(imageUrl);
        return g;
    }

    private void seedTestimonials() {
        if (testimonialRepository.count() > 0) {
            return;
        }
        List<Testimonial> testimonials = List.of(
                testimonial("Emily Johnson", 5, "Absolutely loved my haircut! The stylist really listened to what I wanted.", true),
                testimonial("Michael Chen", 4, "Great massage, very relaxing atmosphere. Will be back!", true),
                testimonial("Priya Patel", 5, "Best facial I've ever had. My skin feels amazing.", true),
                testimonial("Jessica Lee", 3, "Good service overall, though I had to wait a bit past my appointment time.", false),
                testimonial("David Kim", 5, "The color came out exactly how I imagined. Highly recommend Liam!", false)
        );
        testimonialRepository.saveAll(testimonials);
    }

    private Testimonial testimonial(String customerName, int rating, String reviewText, boolean approved) {
        Testimonial t = new Testimonial();
        t.setCustomerName(customerName);
        t.setRating(rating);
        t.setReviewText(reviewText);
        t.setApproved(approved);
        return t;
    }

    private void seedAppointments(List<SalonService> services, List<Staff> staff) {
        if (appointmentRepository.count() > 0 || services.isEmpty() || staff.isEmpty()) {
            return;
        }
        LocalDate today = LocalDate.now();

        List<Appointment> appointments = List.of(
                appointment("Rachel Adams", "rachel.adams@example.com", "555-0101",
                        services.get(0), staff.get(0), today.plusDays(2), LocalTime.of(10, 0),
                        AppointmentStatus.CONFIRMED, ""),
                appointment("Tom Nguyen", "tom.nguyen@example.com", "555-0102",
                        services.get(1), staff.get(1), today.plusDays(3), LocalTime.of(13, 30),
                        AppointmentStatus.PENDING, ""),
                appointment("Olivia Brown", "olivia.brown@example.com", "555-0103",
                        services.get(4), staff.get(2), today.plusDays(1), LocalTime.of(15, 0),
                        AppointmentStatus.PENDING, ""),
                appointment("James Wilson", "james.wilson@example.com", "555-0104",
                        services.get(5), staff.get(3), today.minusDays(2), LocalTime.of(11, 0),
                        AppointmentStatus.COMPLETED, "")
        );
        appointmentRepository.saveAll(appointments);
    }

    private Appointment appointment(String name, String email, String phone, SalonService service, Staff staff,
                                     LocalDate date, LocalTime time, AppointmentStatus status, String notes) {
        Appointment a = new Appointment();
        a.setCustomerName(name);
        a.setCustomerEmail(email);
        a.setCustomerPhone(phone);
        a.setService(service);
        a.setStaff(staff);
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setStatus(status);
        a.setNotes(notes);
        return a;
    }
}
