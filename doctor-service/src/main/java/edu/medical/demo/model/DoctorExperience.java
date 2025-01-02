package edu.medical.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor_experience")
public class DoctorExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    private String position;

    @NotNull
    private String specialization;

    @Enumerated(EnumType.STRING)
    private QualificationLevel level;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Temporal(TemporalType.DATE)
    private Date leaveDate;

    @Transient
    private Period workExperience;

    public DoctorExperience(Doctor doctor, String position, String specialization, QualificationLevel level, Date hireDate) {
        this.doctor = doctor;
        this.position = position;
        this.specialization = specialization;
        this.level = level;
        this.hireDate = hireDate;
    }

    public Period getWorkExperience() {
        if (hireDate == null) {
            return Period.ZERO;
        }

        final LocalDate startDate = hireDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        final LocalDate endDate = leaveDate != null
                ? leaveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : LocalDate.now();

        return Period.between(startDate, endDate);
    }
}
