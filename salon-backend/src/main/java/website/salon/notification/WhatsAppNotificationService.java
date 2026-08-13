//package website.salon.notification;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//
//@Service
//public class WhatsAppNotificationService {
//
//    private static final Logger log = LoggerFactory.getLogger(WhatsAppNotificationService.class);
//
//    private final RestClient restClient;
//    private final boolean enabled;
//    private final String phoneNumberId;
//    private final String accessToken;
//    private final String defaultCountryCode;
//
//    public WhatsAppNotificationService(@Value("${app.notifications.whatsapp.enabled:false}") boolean enabled,
//                                        @Value("${app.notifications.whatsapp.api-version:v21.0}") String apiVersion,
//                                        @Value("${app.notifications.whatsapp.phone-number-id:}") String phoneNumberId,
//                                        @Value("${app.notifications.whatsapp.access-token:}") String accessToken,
//                                        @Value("${app.notifications.whatsapp.default-country-code:91}") String defaultCountryCode) {
//        this.enabled = enabled;
//        this.phoneNumberId = phoneNumberId;
//        this.accessToken = accessToken;
//        this.defaultCountryCode = defaultCountryCode;
//        this.restClient = RestClient.builder()
//                .baseUrl("https://graph.facebook.com/" + apiVersion)
//                .build();
//    }
//
//    public void sendTextMessage(String toPhoneNumber, String message) {
//        if (!enabled) {
//            return;
//        }
//        if (phoneNumberId.isBlank() || accessToken.isBlank()) {
//            log.warn("WhatsApp notifications are enabled but phone-number-id/access-token is not configured; skipping message");
//            return;
//        }
//        String to = normalizePhoneNumber(toPhoneNumber);
//        try {
//            restClient.post()
//                    .uri("/{phoneNumberId}/messages", phoneNumberId)
//                    .header("Authorization", "Bearer " + accessToken)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(new WhatsAppTextMessageRequest(to, new WhatsAppTextMessageRequest.Text(message)))
//                    .retrieve()
//                    .toBodilessEntity();
//        } catch (Exception ex) {
//            log.error("Failed to send WhatsApp message to {}: {}", to, ex.getMessage(), ex);
//        }
//    }
//
//    private String normalizePhoneNumber(String rawPhone) {
//        String digits = rawPhone.replaceAll("[^0-9]", "");
//        if (digits.length() == 10) {
//            return defaultCountryCode + digits;
//        }
//        return digits;
//    }
//
//    private record WhatsAppTextMessageRequest(
//            @JsonProperty("messaging_product") String messagingProduct,
//            String to,
//            String type,
//            Text text
//    ) {
//        WhatsAppTextMessageRequest(String to, Text text) {
//            this("whatsapp", to, "text", text);
//        }
//
//        record Text(@JsonProperty("preview_url") boolean previewUrl, String body) {
//            Text(String body) {
//                this(false, body);
//            }
//        }
//    }
//}

package website.salon.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

@Service
public class WhatsAppNotificationService {

	private static final Logger log = LoggerFactory.getLogger(WhatsAppNotificationService.class);

	private final RestClient restClient;
	private final boolean enabled;
	private final String phoneNumberId;
	private final String accessToken;
	private final String defaultCountryCode;

	// Replace with your EXACT approved Meta template name
	private final String appointmentTemplateName;

	public WhatsAppNotificationService(@Value("${app.notifications.whatsapp.enabled:false}") boolean enabled,

			@Value("${app.notifications.whatsapp.api-version:v21.0}") String apiVersion,
			@Value("${app.notifications.whatsapp.phone-number-id:}") String phoneNumberId,
			@Value("${app.notifications.whatsapp.access-token:}") String accessToken,
			@Value("${app.notifications.whatsapp.default-country-code:91}") String defaultCountryCode,
			@Value("${app.notifications.whatsapp.appointment-template-name:appointment_confirmation}") String appointmentTemplateName) {
		this.enabled = enabled;
		this.phoneNumberId = phoneNumberId;
		this.accessToken = accessToken;
		this.defaultCountryCode = defaultCountryCode;
		this.appointmentTemplateName = appointmentTemplateName;
		this.restClient = RestClient.builder().baseUrl("https://graph.facebook.com/" + apiVersion).build();
	}

	public void sendAppointmentConfirmation(String toPhoneNumber, String customerName, String appointmentDateTime,
			String serviceName, String confirmationNumber) {
		if (!enabled) {
			log.info("WhatsApp notifications are disabled");
			return;
		}

		if (phoneNumberId.isBlank() || accessToken.isBlank()) {
			log.warn("WhatsApp notifications enabled but configuration is missing");
			return;
		}
		String to = normalizePhoneNumber(toPhoneNumber);
		try {
			WhatsAppTemplateRequest request = new WhatsAppTemplateRequest("whatsapp", to, "template",
					new WhatsAppTemplateRequest.Template(appointmentTemplateName,
							new WhatsAppTemplateRequest.Language("en"),
							List.of(new WhatsAppTemplateRequest.Component("body",
									List.of(new WhatsAppTemplateRequest.Parameter("text", customerName),
											new WhatsAppTemplateRequest.Parameter("text", appointmentDateTime),
											new WhatsAppTemplateRequest.Parameter("text", serviceName),
											new WhatsAppTemplateRequest.Parameter("text", confirmationNumber))))));
			restClient.post().uri("/{phoneNumberId}/messages", phoneNumberId)
					.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
					.body(request).retrieve().toBodilessEntity();
			log.info("WhatsApp appointment confirmation sent successfully to {}", to);
		} catch (Exception ex) {
			log.error("Failed to send WhatsApp template message to {}: {}", to, ex.getMessage(), ex);
		}
	}

	private String normalizePhoneNumber(String rawPhone) {
		String digits = rawPhone.replaceAll("[^0-9]", "");
		if (digits.length() == 10) {
			return defaultCountryCode + digits;
		}
		return digits;
	}

	private record WhatsAppTemplateRequest(@JsonProperty("messaging_product") String messagingProduct,
			String to,
			String type,
			Template template) {

		record Template(String name,
				Language language,
				List<Component> components) {
		}

		record Language(String code) {
		}

		record Component(String type,
				List<Parameter> parameters) {
		}

		record Parameter(String type,
				String text) {
		}
	}
}