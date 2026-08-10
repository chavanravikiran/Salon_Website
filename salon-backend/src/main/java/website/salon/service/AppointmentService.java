package website.salon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.salon.common.PagedResponse;
import website.salon.common.PageableFactory;
import website.salon.dto.AppointmentDtos.AppointmentFilterRequest;
import website.salon.dto.AppointmentDtos.AppointmentRequest;
import website.salon.dto.AppointmentDtos.AppointmentResponse;
import website.salon.entity.Appointment;
import website.salon.entity.AppointmentStatus;
import website.salon.entity.SalonService;
import website.salon.entity.Staff;
import website.salon.exception.ResourceNotFoundException;
import website.salon.notification.AppointmentNotificationService;
import website.salon.repository.AppointmentRepository;
import website.salon.repository.SalonServiceRepository;
import website.salon.repository.StaffRepository;
import website.salon.specification.AppointmentSpecifications;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AppointmentService {

    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "customerName", "appointmentDate", "createdAt");

    private final AppointmentRepository repository;
    private final SalonServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final AppointmentNotificationService notificationService;

    public AppointmentService(AppointmentRepository repository,
                               SalonServiceRepository serviceRepository,
                               StaffRepository staffRepository,
                               AppointmentNotificationService notificationService) {
        this.repository = repository;
        this.serviceRepository = serviceRepository;
        this.staffRepository = staffRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public PagedResponse<AppointmentResponse> search(AppointmentFilterRequest filter, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageableFactory.build(page, size, sortBy, sortDir, SORTABLE_FIELDS, "appointmentDate");
        return PagedResponse.from(repository.findAll(AppointmentSpecifications.build(filter), pageable), this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public AppointmentResponse create(AppointmentRequest request) {
        Appointment entity = new Appointment();
        entity.setCustomerName(request.customerName());
        entity.setCustomerEmail(request.customerEmail());
        entity.setCustomerPhone(request.customerPhone());
        entity.setAppointmentDate(request.appointmentDate());
        entity.setAppointmentTime(request.appointmentTime());
        entity.setNotes(request.notes());
        entity.setStatus(AppointmentStatus.PENDING);

        SalonService salonService = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + request.serviceId()));
        entity.setService(salonService);

        if (request.staffId() != null) {
            Staff staff = staffRepository.findById(request.staffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + request.staffId()));
            entity.setStaff(staff);
        }

        AppointmentResponse response = toResponse(repository.save(entity));
        notificationService.notifyAppointmentCreated(response);
        return response;
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment entity = getEntity(id);
        entity.setStatus(status);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found: " + id);
        }
        repository.deleteById(id);
    }

    private Appointment getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
    }

    private AppointmentResponse toResponse(Appointment entity) {
        Staff staff = entity.getStaff();
        return new AppointmentResponse(
                entity.getId(),
                entity.getCustomerName(),
                entity.getCustomerEmail(),
                entity.getCustomerPhone(),
                entity.getService().getId(),
                entity.getService().getName(),
                staff != null ? staff.getId() : null,
                staff != null ? staff.getName() : null,
                entity.getAppointmentDate(),
                entity.getAppointmentTime(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }
}
