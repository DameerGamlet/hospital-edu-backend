package edu.medical.demo.controller;

import edu.medical.demo.model.Doctor;
import edu.medical.demo.model.request.DoctorCreateRequest;
import edu.medical.demo.model.response.DoctorCreateResponse;
import edu.medical.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("private/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorCreateResponse createDoctor(@RequestBody DoctorCreateRequest request) {
        return doctorService.createDoctor(request);
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getDoctors();
    }
}
