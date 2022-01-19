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

	public static DatabaseManager getDatabaseManager() {
		if (dbManager == null) {
			dbManager = new DatabaseManager();
		}
		return dbManager;
	}

	private DatabaseManager() {

	}

	public boolean store(String programName, String editorContent, String territoryXML, List<String> tags) {
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			ResultSet resultSet = null;
			PreparedStatement stmt = null;
			PreparedStatement s = null;
			logger.info("storing data in database");
			try {
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(INSERT_EXAMPLE, PreparedStatement.RETURN_GENERATED_KEYS);
				stmt.setString(1, programName);
				stmt.setString(2, editorContent);
				stmt.setString(3, territoryXML);

				stmt.execute();
				resultSet = stmt.getGeneratedKeys();
				int exampleKey = 0;
				if (resultSet.next()) {
					exampleKey = resultSet.getInt(1);
				}
				logger.debug("Examplekey is {}", exampleKey);

				for (String tag : tags) {
					s = conn.prepareStatement(INSERT_TAG);
					s.setInt(1, exampleKey);
					s.setString(2, tag);
					s.execute();
					s.close();
				}
				conn.commit();
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException ignore) {
				}
				logger.debug("Could not store example in database");
				return false;
			} finally {
				if (s != null) { // if s.execute throws exception, s is not closed
					try {
						s.close();
					} catch (SQLException ignore) {
					}
				}
				if (resultSet != null)
					try {
						resultSet.close();
					} catch (SQLException ignore) {
					}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException ignore) {
					}
				}
				if (conn != null) {
					try {
						conn.setAutoCommit(true);
					} catch (SQLException ignore) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
					}
				}
			}
		}
		return true;
	}

	public Optional<List<Pair<Integer, String>>> query(String tag) {
		if (!initialized)
			return Optional.empty();

		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			PreparedStatement s = null;
			ResultSet resultSet = null;

			logger.info("Loading Examples by tag {}", tag);
			try {
				ArrayList<Pair<Integer, String>> programs = new ArrayList<>();
				stmt = conn.prepareStatement(SELECT_IDS_BY_TAG);
				stmt.setString(1, tag);
				rs = stmt.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("ex_id");

					s = conn.prepareStatement(SELECT_NAME_BY_ID);
					s.setInt(1, id);
					resultSet = s.executeQuery();

					if (resultSet.next()) { // ids are unique -> only one result
						String name = resultSet.getString("name");
						programs.add(new Pair<Integer, String>(id, name));
					}
					resultSet.close();
					s.close();

				}
				return Optional.of(programs);
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException ignore) {
					}
				}
				if (s != null) {
					try {
						s.close();
					} catch (SQLException ignore) {
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ignore) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException ignore) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
					}
				}
			}

		} else
			return Optional.empty();
	}

	public Optional<Example> loadExample(int id) {
		if (!initialized)
			return Optional.empty();
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			PreparedStatement stmt = null;
			ResultSet rs = null;

			logger.debug("loading example from database");
			try {
				stmt = conn.prepareStatement(SELECT_PROGRAM_BY_ID);
				stmt.setInt(1, id);
				rs = stmt.executeQuery();
				Example ex = null;
				while (rs.next()) {
					String name = rs.getString("name");
					String code = rs.getString("code");
					String territory = rs.getString("territory");
					ex = new Example(name, code, territory);
				}
				return Optional.ofNullable(ex);
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ignore) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException ignore) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
					}
				}
			}
		}
		return Optional.empty();
	}

	public Optional<List<String>> getAllTags() {
		if (!initialized)
			return Optional.empty();
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			Statement stmt = null;
			ResultSet rs = null;

			logger.debug("loading distinct tags from database");
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(SELECT_DISTINCT_TAGS);
				ArrayList<String> tags = new ArrayList<>();
				while (rs.next()) {
					String tag = rs.getString("tagname");
					tags.add(tag);
				}

				return Optional.ofNullable(tags);
			} catch (SQLException e) {
				return Optional.empty();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ignore) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException ignore) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
					}
				}
			}
		}
		return Optional.empty();
	}

	private Optional<Connection> getConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";create=false");
			return Optional.ofNullable(conn);
		} catch (SQLException e) {
			logger.debug(e);
			return Optional.empty();
		}
	}

	public boolean initialize() {
		logger.debug("initialize database");
		try {
			// Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			DriverManager.registerDriver(new EmbeddedDriver());
		} catch (SQLException e) {
			return false;
		}

		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {

			// dropAllTables();
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

			initialized = true;
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ignore) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignore) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
			}
		}

	}

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
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ignore) {
					}
				}
			}
		});
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public void shutDown() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			logger.debug("Database closed.");
		}
	}

}
