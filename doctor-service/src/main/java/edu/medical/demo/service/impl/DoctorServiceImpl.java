package edu.medical.demo.service.impl;

import edu.medical.demo.model.Doctor;
import edu.medical.demo.model.request.DoctorCreateRequest;
import edu.medical.demo.model.response.DoctorCreateResponse;
import edu.medical.demo.service.DoctorService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final SessionFactory sessionFactory;

    private final MeterRegistry meterRegistry;

    /**
     * Создать запись о новом докторе.
     *
     * @param request - базовые знания о докторе, включая имя и почтовый адрес.
     * @return - ответ о создании новой записи.
     */
    @Override
    @Transactional
    public DoctorCreateResponse createDoctor(DoctorCreateRequest request) {
        log.info("Добавление новой учетной записи для доктора: {}", request);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            final boolean isExist = session.createQuery("""
                            SELECT COUNT(d) > 0
                            FROM Doctor d
                            WHERE d.email = :email
                            """, Boolean.class)
                    .setParameter("email", request.email())
                    .uniqueResult();

            if (isExist) {
                session.getTransaction().rollback();
                meterRegistry.counter("doctor_failed_created").increment();
                return new DoctorCreateResponse(null, "User already exist");
            }

            final Doctor doctor = new Doctor(request.fullName(), request.email());
            session.persist(doctor);
            session.getTransaction().commit();

            meterRegistry.counter("doctor_success_created").increment();
            return new DoctorCreateResponse(doctor.getDoctorId(), null);
        } catch (Exception e) {
            final String errorMessage = "Ошибка при добавлении доктора: ";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage + e);
        }
    }

    @Override
    public List<Doctor> getDoctors() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT d FROM Doctor d LEFT JOIN FETCH d.experiences",
                            Doctor.class
                    )
                    .list();
        } catch (Exception e) {
            final String errorMessage = "Ошибка при получении списка пользователей: ";
            log.error(errorMessage, e);
            meterRegistry.counter("doctor_failed_get_list").increment();
            throw new RuntimeException(errorMessage + e);
        }
    }
}
