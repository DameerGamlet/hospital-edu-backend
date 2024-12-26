package edu.medical.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.medical.demo.models.request.ActivationMessage;

public interface KafkaProducerService {
    boolean sendAuthenticationCode(ActivationMessage mailRequest) throws JsonProcessingException;
}
