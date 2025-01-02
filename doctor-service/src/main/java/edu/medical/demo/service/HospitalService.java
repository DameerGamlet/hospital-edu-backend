package edu.medical.demo.service;

import edu.medical.demo.model.Hospital;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Получить список госпиталей.
     *
     * @return список госпиталей, либо ошибку.
     */
    public List<Hospital> getHospitals() {
        final TypedQuery<Hospital> query = entityManager.createQuery("""
                        SELECT h
                        Hospital h
                        """,
                Hospital.class
        );

        return query.getResultList();
    }

    /**
     * Получить госпиталь по его UUID.
     *
     * @param hospitalId - входное значение UUID госпиталя.
     * @return получить найденные сведения о госпитале, либо ошибку в случае проблем.
     */
    public Hospital getHospitalById(UUID hospitalId) {
        final TypedQuery<Hospital> query = entityManager.createQuery("""
                        SELECT h
                        FROM Hospital h
                        WHERE h.hospitalId = :hospitalId
                        """,
                Hospital.class
        );

        query.setParameter("hospitalId", hospitalId);

        return query.getSingleResult();
    }
}
