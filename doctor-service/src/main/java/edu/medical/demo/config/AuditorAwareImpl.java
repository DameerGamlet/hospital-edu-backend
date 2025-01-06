package edu.medical.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override

    public Optional<String> getCurrentAuditor() {
        return Optional.of("System");
    }
}
