package edu.medical.demo.service;

import edu.medical.demo.model.Doctor;
import edu.medical.demo.model.request.DoctorCreateRequest;
import edu.medical.demo.model.response.DoctorCreateResponse;
import edu.medical.demo.service.impl.DoctorServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

    private DoctorServiceImpl doctorService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Boolean> queryBoolean;

    @Mock
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);

        doctorService = new DoctorServiceImpl(sessionFactory);
    }

    @Test
    void createDoctorShouldCreateNewDoctor() {
        final DoctorCreateRequest request = new DoctorCreateRequest("First Second", "first.second@gmail.com");

        when(session.createQuery(anyString(), eq(Boolean.class))).thenReturn(queryBoolean);
        when(queryBoolean.setParameter(eq("email"), eq(request.email()))).thenReturn(queryBoolean);
        when(queryBoolean.uniqueResult()).thenReturn(false);

        doNothing().when(transaction).commit();

        final DoctorCreateResponse response = doctorService.createDoctor(request);

        Assertions.assertNotNull(response);
        verify(session).persist(any(Doctor.class));
        verify(transaction).commit();
    }
}