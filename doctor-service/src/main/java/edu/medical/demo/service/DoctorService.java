package edu.medical.demo.service;

import edu.medical.demo.model.Doctor;
import edu.medical.demo.model.request.DoctorCreateRequest;
import edu.medical.demo.model.response.DoctorCreateResponse;

import java.util.List;

public interface DoctorService {
    DoctorCreateResponse createDoctor(DoctorCreateRequest request);

    List<Doctor> getDoctors();
}
