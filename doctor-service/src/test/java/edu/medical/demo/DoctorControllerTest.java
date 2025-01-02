package edu.medical.demo;

import edu.medical.demo.controller.DoctorController;
import edu.medical.demo.model.Doctor;
import edu.medical.demo.model.request.DoctorCreateRequest;
import edu.medical.demo.model.response.DoctorCreateResponse;
import edu.medical.demo.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String PRIVATE_DOCTOR_API = "/private/api/v1/doctors";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService service;

    @Test
    void createDoctorShouldReturnSuccessResponse() throws Exception {
        final DoctorCreateResponse response = new DoctorCreateResponse(UUID.randomUUID(), null);

        Mockito.when(service.createDoctor(any(DoctorCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(PRIVATE_DOCTOR_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "fullName": "John Doe",
                                    "email": "john.doe@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(response.doctorId().toString()))
                .andExpect(jsonPath("$.errorMessage").doesNotExist());
    }

    @Test
    void getAllDoctorsShouldReturnListOfDoctors() throws Exception {
        final List<Doctor> doctors = List.of(new Doctor(TEST_NAME, TEST_EMAIL));

        Mockito.when(service.getDoctors()).thenReturn(doctors);

        mockMvc.perform(get(PRIVATE_DOCTOR_API))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value(TEST_NAME))
                .andExpect(jsonPath("$[0].email").value(TEST_EMAIL));
    }
}
