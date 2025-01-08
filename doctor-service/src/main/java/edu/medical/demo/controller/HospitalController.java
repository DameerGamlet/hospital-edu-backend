package edu.medical.demo.controller;

import edu.medical.demo.model.Hospital;
import edu.medical.demo.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "public/api/v1/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<List<Hospital>> getHospitals() {
        return ResponseEntity.ok(hospitalService.getHospitals());
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<Hospital> getHospitalInfo(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(hospitalService.getHospitalById(hospitalId));
    }

    @RequestMapping("/favicon.ico")
    public void returnNoFavicon() {
        // метод без действия
    }
}
