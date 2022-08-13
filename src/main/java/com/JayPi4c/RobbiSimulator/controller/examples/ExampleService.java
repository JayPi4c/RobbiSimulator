package com.JayPi4c.RobbiSimulator.controller.examples;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import com.JayPi4c.RobbiSimulator.utils.HibernateUtils;

import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleService {

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

	public static Optional<List<Pair<Integer, String>>> query(String tag) {
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			List<Example> allExamples = session.createQuery("from Example", Example.class).list();

			List<Example> taggedExamples = allExamples.stream().filter(ex -> ex.getTags().contains(tag)).toList();

			return Optional.of(taggedExamples.stream().map(ex -> new Pair<>(ex.getId(), ex.getProgramName())).toList());
		}
	}

	public static Optional<Example> loadExample(int id) {
		Example example;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			session.beginTransaction();
			example = session.get(Example.class, id);
			session.getTransaction().commit();
		}
		return Optional.ofNullable(example);
	}

	public static Optional<List<String>> getAllTags() {
		List<String> tags;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			List<Example> allExamples = session.createQuery("from Example", Example.class).list();
			tags = allExamples.stream().flatMap(ex -> ex.getTags().stream()).distinct().toList();
		}
		return Optional.ofNullable(tags);
	}
}
