package edu.medical.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QualificationLevel {
    INTERN("Стажер"),
    JUNIOR_SPECIALIST("Младший специалист"),
    SPECIALIST("Специалист"),
    SENIOR_SPECIALIST("Старший специалист"),
    EXPERT("Эксперт"),
    PROFESSOR("Профессор"),
    CONSULTANT("Консультант"),
    HEAD_OF_DEPARTMENT("Заведующий отделением"),
    DIRECTOR("Директор");

    private final String description;
}
