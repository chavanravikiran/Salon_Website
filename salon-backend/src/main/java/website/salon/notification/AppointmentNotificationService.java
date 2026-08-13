//package website.salon.notification;
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import website.salon.dto.AppointmentDtos.AppointmentResponse;
//
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class AppointmentNotificationService {
//
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
//
//    private final EmailNotificationService emailNotificationService;
//    private final WhatsAppNotificationService whatsAppNotificationService;
//
//    public AppointmentNotificationService(EmailNotificationService emailNotificationService,
//                                           WhatsAppNotificationService whatsAppNotificationService) {
//        this.emailNotificationService = emailNotificationService;
//        this.whatsAppNotificationService = whatsAppNotificationService;
//    }
//
//    @Async
//    public void notifyAppointmentCreated(AppointmentResponse appointment) {
//        String when = appointment.appointmentDate().format(DATE_FORMATTER) + " at " + appointment.appointmentTime().format(TIME_FORMATTER);
//        String staffLine = appointment.staffName() != null ? "\nStylist: " + appointment.staffName() : "";
//
//        String message = "Hi " + appointment.customerName() + ", your appointment request for "
//                + appointment.serviceName() + " on " + when + staffLine
//                + " has been received. We'll contact you shortly to confirm.";
//
//        System.out.println("email message : "+message);
//        
//        emailNotificationService.sendPlainTextEmail(
//                appointment.customerEmail(),
//                "Appointment Request Received",
//                message);
//
//        whatsAppNotificationService.sendTextMessage(appointment.customerPhone(), message);
//    }
//}
package website.salon.notification;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import website.salon.dto.AppointmentDtos.AppointmentResponse;

import java.time.format.DateTimeFormatter;

@Service
public class AppointmentNotificationService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("hh:mm a");

    private final EmailNotificationService emailNotificationService;
    private final WhatsAppNotificationService whatsAppNotificationService;

    public AppointmentNotificationService(
            EmailNotificationService emailNotificationService,
            WhatsAppNotificationService whatsAppNotificationService
    ) {
        this.emailNotificationService = emailNotificationService;
        this.whatsAppNotificationService = whatsAppNotificationService;
    }

    @Async
    public void notifyAppointmentCreated(
            AppointmentResponse appointment
    ) {

        String formattedDate =
                appointment.appointmentDate()
                        .format(DATE_FORMATTER);

        String formattedTime =
                appointment.appointmentTime()
                        .format(TIME_FORMATTER);

        String emailMessage =
                "Hi " + appointment.customerName()
                        + ", your appointment request for "
                        + appointment.serviceName()
                        + " on "
                        + formattedDate
                        + " at "
                        + formattedTime
                        + " has been received. We'll contact you shortly.";

        emailNotificationService.sendPlainTextEmail(
                appointment.customerEmail(),
                "Appointment Request Received",
                emailMessage
        );

        whatsAppNotificationService.sendAppointmentConfirmation(
                appointment.customerPhone(),
                appointment.customerName(),                       // {{1}}
                formattedDate + " at " + formattedTime,           // {{2}}
                appointment.serviceName(),                         // {{3}}
                String.valueOf(appointment.id())                  // {{4}}
        );
    }
}