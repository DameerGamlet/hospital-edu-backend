package edu.medical.demo.repository;

import edu.medical.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT u FROM User u
            """)
    Optional<User> findByUserId(UUID userId);

    boolean existsByEmail(String email);
}
