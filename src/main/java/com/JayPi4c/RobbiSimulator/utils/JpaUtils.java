package com.JayPi4c.RobbiSimulator.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to manage the JPA EntityManagerFactor.
 *
 * @author Jonas Pohl
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaUtils {

    /**
     * The entity manager factory to interact with the database via jpa.
     */
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Getter for the EntityManagerFactory. If the EntityManagerFactory is null, it will be
     * created.
     *
     * @return The EntityManagerFactory.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            System.setProperty("derby.stream.error.file", "logs/derby.log"); // TODO: write db logs to dedicated folder
            entityManagerFactory = Persistence.createEntityManagerFactory("RobbiSimulator");
        }
        return entityManagerFactory;
    }

    /**
     * Convenience method to create a new EntityManager.
     *
     * @return A new EntityManager.
     */
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Utility function to shut down the database connection.
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

}
