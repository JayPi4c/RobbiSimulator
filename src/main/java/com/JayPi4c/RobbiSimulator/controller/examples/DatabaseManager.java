package com.JayPi4c.RobbiSimulator.controller.examples;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.util.Pair;

/**
 * DatabaseManager to manage all database related tasks.
 * 
 * @author Jonas Pohl
 *
 */
public class DatabaseManager {
	private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
	private static DatabaseManager dbManager;
	private static boolean initialized = false;

	private static final String DB_NAME = "robbiDB";

	private static final String EXAMPLES_TABLE_NAME = "EXAMPLES";
	private static final String CREATE_EXMAPLES_TABLE = "CREATE TABLE " + EXAMPLES_TABLE_NAME
			+ " (ex_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(255), code VARCHAR(10000), territory VARCHAR(15000))";
	private static final String INSERT_EXAMPLE = "INSERT INTO " + EXAMPLES_TABLE_NAME
			+ " (name, code, territory) VALUES (?, ?, ?)";

	private static final String TAGS_TABLE_NAME = "TAGS";
	private static final String CREATE_TAGS_TABLE = "CREATE TABLE " + TAGS_TABLE_NAME
			+ " (ex_id INTEGER NOT NULL, tagname VARCHAR(20))";
	private static final String INSERT_TAG = "INSERT INTO " + TAGS_TABLE_NAME + " (ex_id, tagname) VALUES (?, ?)";
	private static final String SELECT_DISTINCT_TAGS = "SELECT DISTINCT tagname FROM " + TAGS_TABLE_NAME
			+ " ORDER BY tagname";
	private static final String SELECT_IDS_BY_TAG = "SELECT ex_id FROM " + TAGS_TABLE_NAME + " WHERE tagname=?";
	private static final String SELECT_NAME_BY_ID = "SELECT name FROM " + EXAMPLES_TABLE_NAME + " WHERE ex_id=?";
	private static final String SELECT_PROGRAM_BY_ID = "SELECT name, code, territory FROM " + EXAMPLES_TABLE_NAME
			+ " WHERE ex_id=?";

	/**
	 * Getter for the databaseManager to fulfill the singleton pattern.
	 * 
	 * @return the instance of the databaseManager
	 */
	public static DatabaseManager getDatabaseManager() {
		if (dbManager == null) {
			dbManager = new DatabaseManager();
		}
		return dbManager;
	}

	/**
	 * Private constructor to stop other classes of creating another manager
	 */
	private DatabaseManager() {

	}

	/**
	 * Method to store an example with its tags in the database.
	 * 
	 * @param programName   the name of the program
	 * @param editorContent the code in the editor
	 * @param territoryXML  the territory encoded in XML
	 * @param tags          the tags related to this example
	 * @return true if the example was stored successfully, false otherwise
	 */
	public boolean store(String programName, String editorContent, String territoryXML, List<String> tags) {
		if (!initialized)
			return false;
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			logger.info("storing data in database");
			Connection conn = connection.get();
			try {
				conn.setAutoCommit(false);
				try (PreparedStatement stmt = conn.prepareStatement(INSERT_EXAMPLE, Statement.RETURN_GENERATED_KEYS)) {
					stmt.setString(1, programName);
					stmt.setString(2, editorContent);
					stmt.setString(3, territoryXML);

					stmt.execute();

					try (ResultSet resultSet = stmt.getGeneratedKeys();
							PreparedStatement s = conn.prepareStatement(INSERT_TAG)) {
						int exampleKey = 0;
						if (resultSet.next()) {
							exampleKey = resultSet.getInt(1);
						}
						logger.debug("Examplekey is {}", exampleKey);
						s.setInt(1, exampleKey);
						for (String tag : tags) {
							s.setString(2, tag);
							s.execute();
						}
						conn.commit();
					}
				}
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException ignore) {
					// ignore
				}
				logger.debug("Could not store example in database");
				return false;
			} finally {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException ignore) {
					// ignore
				}
				try {
					conn.close();
				} catch (SQLException ignore) {
					// ignore
				}
			}
		}
		return true;
	}

	/**
	 * Method to get all examples identified by id and programName by their tag.
	 * 
	 * @param tag the tag to search the examples for
	 * @return List of examples as pairs of id and programName
	 */
	public Optional<List<Pair<Integer, String>>> query(String tag) {
		if (!initialized)
			return Optional.empty();

		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {

			logger.info("Loading Examples by tag {}", tag);
			try (Connection conn = connection.get();
					PreparedStatement stmt = conn.prepareStatement(SELECT_IDS_BY_TAG)) {
				// TODO combine both statements into one
				stmt.setString(1, tag);
				try (ResultSet rs = stmt.executeQuery();
						PreparedStatement s = conn.prepareStatement(SELECT_NAME_BY_ID)) {
					ArrayList<Pair<Integer, String>> programs = new ArrayList<>();
					while (rs.next()) {
						int id = rs.getInt("ex_id");
						s.setInt(1, id);
						try (ResultSet resultSet = s.executeQuery()) {
							if (resultSet.next()) { // ids are unique -> only one result
								String name = resultSet.getString("name");
								programs.add(new Pair<>(id, name));
							}
						}
					}
					if (programs.isEmpty())
						return Optional.empty();
					else
						return Optional.of(programs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		} else
			return Optional.empty();
	}

	/**
	 * Loads an example from the database by the given id.
	 * 
	 * @param id the ID of the example to load
	 * @return Optional containing the example or an empty Optional if no example
	 *         could be found
	 */
	public Optional<Example> loadExample(int id) {
		if (!initialized)
			return Optional.empty();
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			logger.debug("loading example from database");
			try (Connection conn = connection.get();
					PreparedStatement stmt = conn.prepareStatement(SELECT_PROGRAM_BY_ID)) {
				stmt.setInt(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					Example ex = null;
					while (rs.next()) {
						String name = rs.getString("name");
						String code = rs.getString("code");
						String territory = rs.getString("territory");
						ex = new Example(name, code, territory);
					}
					return Optional.ofNullable(ex);
				}
			} catch (SQLException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	/**
	 * Method to load all distinct tags from the database.
	 * 
	 * @return List of all distinct tags stored in the database
	 */
	public Optional<List<String>> getAllTags() {
		if (!initialized)
			return Optional.empty();
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {

			logger.debug("loading distinct tags from database");
			try (Connection conn = connection.get();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(SELECT_DISTINCT_TAGS)) {

				ArrayList<String> tags = new ArrayList<>();
				while (rs.next()) {
					String tag = rs.getString("tagname");
					tags.add(tag);
				}

				return Optional.ofNullable(tags);
			} catch (SQLException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	/**
	 * Getter for a Connection to the database by the defined URL.
	 * 
	 * @return a connection to the database
	 */
	private Optional<Connection> getConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";create=false");
			return Optional.ofNullable(conn);
		} catch (SQLException e) {
			logger.debug(e);
			return Optional.empty();
		}
	}

	/**
	 * Initializes the database and creates the tables if necessary.
	 * 
	 * @return true if the initialization was successful, false otherwise
	 */
	public static boolean initialize() {
		logger.debug("initialize database");
		try {
			DriverManager.registerDriver(new EmbeddedDriver());
		} catch (SQLException e) {
			return false;
		}

		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {

			conn = DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";create=true");

			// check if the database has the table already. Create if not
			DatabaseMetaData dmd = conn.getMetaData();
			rs = dmd.getTables(null, null, EXAMPLES_TABLE_NAME, null);
			if (!rs.next()) {
				logger.debug("creating Examples table");
				stmt = conn.createStatement();
				stmt.executeUpdate(CREATE_EXMAPLES_TABLE);
				stmt.close();
			}
			rs.close();
			rs = dmd.getTables(null, null, TAGS_TABLE_NAME, null);
			if (!rs.next()) {
				logger.debug("creating Tags table");
				stmt = conn.createStatement();
				stmt.executeUpdate(CREATE_TAGS_TABLE);
				stmt.close();
			}

			DatabaseManager.initialized = true;
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ignore) {
					// ignore
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignore) {
					// ignore
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
					// ignore
				}
			}
		}

	}

	/**
	 * Helper Method to drop all tables to reset the stored examples.
	 */
	public static void dropAllTables() {
		Optional<Connection> connection = DatabaseManager.getDatabaseManager().getConnection();
		connection.ifPresent(conn -> {
			Statement s = null;
			try {
				s = conn.createStatement();
				int result = s.executeUpdate("DROP TABLE " + EXAMPLES_TABLE_NAME);
				s.close();
				logger.info("Deletion result {}", result);
				s = conn.createStatement();
				result = s.executeUpdate("DROP TABLE " + TAGS_TABLE_NAME);
				s.close();
				logger.info("Deletion result {}", result);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (SQLException ignore) {
						// ignore
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
						// ignore
					}
				}
			}
		});
	}

	/**
	 * Getter to check if the initialization finished successfully.
	 * 
	 * @return true if the manager is initialized, false otherwise
	 */
	public static boolean isInitialized() {
		return initialized;
	}

	/**
	 * Utility function to shutdown the database connection.
	 */
	public void shutDown() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			logger.debug("Database closed.");
		}
	}

}
