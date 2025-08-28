package com.roshkatest.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Date currentDate = new Date();
        
        entityManager.createNativeQuery(
            "UPDATE tasks SET created_at = ?, updated_at = ?"
        ).setParameter(1, currentDate)
         .setParameter(2, currentDate)
         .executeUpdate();
        
        entityManager.createNativeQuery(
            "UPDATE subtasks SET created_at = ?, updated_at = ?"
        ).setParameter(1, currentDate)
         .setParameter(2, currentDate)
         .executeUpdate();
        
    }
}
