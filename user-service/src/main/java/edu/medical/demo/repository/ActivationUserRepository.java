package edu.medical.demo.repository;

import edu.medical.demo.model.ActivationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivationUserRepository extends JpaRepository<ActivationUser, Long> {
    Optional<ActivationUser> findByUserId(UUID userId);
}
