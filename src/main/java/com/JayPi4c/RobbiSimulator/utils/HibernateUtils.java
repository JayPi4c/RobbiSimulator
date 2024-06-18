package com.JayPi4c.RobbiSimulator.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
    private static SessionFactory sessionFactory;

    /**
     * Getter for the SessionFactory. If the SessionFactory is null, it will be
     * created.
     *
     * @return The SessionFactory.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.setProperty("derby.stream.error.file", "logs/derby.log"); // make sure log-file is in logs folder
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Exception ex) {
                logger.error("Initial SessionFactory creation failed.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    /**
     * Utility function to shut down the database connection.
     */
    public static void shutdown() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

}
