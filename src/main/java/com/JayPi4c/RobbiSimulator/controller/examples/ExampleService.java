package com.JayPi4c.RobbiSimulator.controller.examples;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import com.JayPi4c.RobbiSimulator.utils.HibernateUtils;

import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.merge(example);
			session.getTransaction().commit();
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
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			List<Example> allExamples = session.createQuery("from Example", Example.class).list();

			List<Example> taggedExamples = allExamples.stream().filter(ex -> ex.getTags().contains(tag)).toList();

			return Optional.of(taggedExamples.stream().map(ex -> new Pair<>(ex.getId(), ex.getProgramName())).toList());
		}
	}

	/**
	 * Loads an example from the database by the given id.
	 * 
	 * @param id the ID of the example to load
	 * @return Optional containing the example or an empty Optional if no example
	 *         could be found
	 */
	public static Optional<Example> loadExample(int id) {
		Example example;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			example = session.get(Example.class, id);
			session.getTransaction().commit();
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
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			List<Example> allExamples = session.createQuery("from Example", Example.class).list();
			tags = allExamples.stream().flatMap(ex -> ex.getTags().stream()).distinct().toList();
		}
		return Optional.ofNullable(tags);
	}

}
