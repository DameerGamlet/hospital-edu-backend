package edu.medical.demo.repository;

import edu.medical.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT u FROM User u
            WHERE u.userId = :userId
            """)
    Optional<User> findByUserId(@Param("userId") UUID userId);

    @Query(value = """
            SELECT u FROM User u
            WHERE u.email = :email
            """)
    Optional<User> findByUserEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
