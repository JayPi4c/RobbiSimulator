package com.JayPi4c.RobbiSimulator.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to manage the Hibernate SessionFactory.
 * 
 * @author Jonas Pohl
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtils {

    /**
     * The session factory to interact with the database via hibernate.
     */
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        System.setProperty("derby.stream.error.file", "logs/derby.log"); // make sure log-file is in logs folder
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            logger.error("Initial SessionFactory creation failed: {}", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Utility function to shutdown the database connection.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
