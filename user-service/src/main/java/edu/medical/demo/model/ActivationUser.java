package edu.medical.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "activation-users")
@NoArgsConstructor
public class ActivationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID userId;

    @NotNull
    private String code;

    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public ActivationUser(UUID userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusHours(1);
    }
}
