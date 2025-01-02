package edu.medical.demo.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetails {
    private String position;

    private String specialization;

    @Enumerated(EnumType.STRING)
    private QualificationLevel level;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ElementCollection
    private List<String> achievements;

    @ElementCollection
    private List<String> certifications;
}
