package com.JayPi4c.RobbiSimulator.controller.examples;

import com.JayPi4c.RobbiSimulator.utils.JpaUtils;
import jakarta.persistence.EntityManager;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Service to easily access examples from the database using the HibernateUtils
 * class.
 *
 * @author Jonas Pohl
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleService {

    /**
     * Method to store an example with its tags in the database.
     *
     * @param programName   the name of the program
     * @param editorContent the code in the editor
     * @param territoryXML  the territory encoded in XML
     * @param tags          the tags related to this example
     * @return true if the example was stored successfully, false otherwise
     */
    public static boolean store(String programName, String editorContent, String territoryXML, List<String> tags) {
        Example example = new Example();
        example.setProgramName(programName);
        example.setCode(editorContent);
        example.setTerritory(territoryXML);
        example.setTags(tags);

        try (EntityManager em = JpaUtils.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(example);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            logger.error("Could not store example", e);
            return false;
        }
    }

    /**
     * Method to get all examples identified by id and programName by their tag.
     *
     * @param tag the tag to search the examples for
     * @return List of examples as pairs of id and programName
     */
    public static Optional<List<Pair<Integer, String>>> query(String tag) {
        try (EntityManager em = JpaUtils.createEntityManager()) {
            List<Example> allExamples = em.createQuery("SELECT e FROM Example e", Example.class).getResultList();
            List<Example> taggedExamples = allExamples.stream().filter(ex -> ex.getTags().contains(tag)).toList();

            return Optional.of(taggedExamples.stream().map(ex -> new Pair<>(ex.getId(), ex.getProgramName())).toList());
        }
    }

    /**
     * Loads an example from the database by the given id.
     *
     * @param id the ID of the example to load
     * @return Optional containing the example or an empty Optional if no example
     * could be found
     */
    public static Optional<Example> loadExample(int id) {
        Example example;
        try (EntityManager em = JpaUtils.createEntityManager()) {
            em.getTransaction().begin();
            example = em.find(Example.class, id);
            em.getTransaction().commit();
        }
        return Optional.ofNullable(example);
    }

    /**
     * Method to load all distinct tags from the database.
     *
     * @return List of all distinct tags stored in the database
     */
    public static Optional<List<String>> getAllTags() {
        List<String> tags;
        try (EntityManager em = JpaUtils.createEntityManager()) {
            tags = em.createQuery("SELECT DISTINCT t FROM Example e join e.tags t", String.class).getResultList();
            logger.debug("Foung {} distinct tags", tags.size());
        }
        return Optional.of(tags);
    }

}
