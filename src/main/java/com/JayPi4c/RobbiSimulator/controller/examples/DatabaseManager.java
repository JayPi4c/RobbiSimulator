package com.JayPi4c.RobbiSimulator.controller.examples;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.model.Territory;

public class DatabaseManager {
	private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
	private static DatabaseManager dbManager;
	private static boolean initialized = false;

	private static final String DB_NAME = "robbiDB";

	private static final String EXAMPLES_TABLE_NAME = "EXAMPLES";
	private static final String CREATE_EXMAPLES_TABLE = "CREATE TABLE " + EXAMPLES_TABLE_NAME
			+ " (ex_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(255), code VARCHAR(255), territory VARCHAR(255))";
	private static final String INSERT_EXAMPLE = "INSERT INTO " + EXAMPLES_TABLE_NAME
			+ " (name, code, territory) VALUES (?, ?, ?)";

	private static final String TAGS_TABLE_NAME = "TAGS";
	private static final String CREATE_TAGS_TABLE = "CREATE TABLE " + TAGS_TABLE_NAME
			+ " (ex_id INTEGER NOT NULL, tagname VARCHAR(20))";
	private static final String INSERT_TAG = "INSERT INTO " + TAGS_TABLE_NAME + " (ex_id, tagname) VALUES (?, ?)";

	public static DatabaseManager getDatabaseManager() {
		if (dbManager == null) {
			dbManager = new DatabaseManager();
		}
		return dbManager;
	}

	private DatabaseManager() {

	}

	public boolean store(Program program, Territory territory, String tags[]) {
		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			logger.info("storing data in database");
			try {
				PreparedStatement stmt = conn.prepareStatement(INSERT_EXAMPLE, PreparedStatement.RETURN_GENERATED_KEYS);
				stmt.setString(1, "example1");
				stmt.setString(2, "some Code");
				stmt.setString(3, "territory code");

				stmt.execute();
				ResultSet resultSet = stmt.getGeneratedKeys();
				int exampleKey = 0;
				if (resultSet.next()) {
					exampleKey = resultSet.getInt(1);
				}
				logger.debug("Examplekey is {}", exampleKey);
				for (String tag : tags) {
					PreparedStatement s = conn.prepareStatement(INSERT_TAG);
					s.setInt(1, exampleKey);
					s.setString(2, tag);
					s.execute();
					s.close();
				}
				resultSet.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public boolean query(String tag) {
		if (!initialized)
			return false;

		Optional<Connection> connection = getConnection();
		if (connection.isPresent()) {
			Connection conn = connection.get();
			logger.info("loading data from exmaples");
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT ex_id FROM " + TAGS_TABLE_NAME + " WHERE tagname='" + tag + "'");
				while (rs.next()) {
					int id = rs.getInt("ex_id");
					logger.info("found: {}", id);

					Statement s = conn.createStatement();
					ResultSet resultSet = s
							.executeQuery("SELECT * FROM " + EXAMPLES_TABLE_NAME + " WHERE ex_id=" + id + "");
					while (resultSet.next()) {
						String name = resultSet.getString("name");
						String code = resultSet.getString("code");
						String territory = resultSet.getString("territory");
						logger.info("found {}, {}, {}", name, code, territory);
					}
					resultSet.close();
					s.close();

				}
				rs.close();
				stmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}

		} else
			return false;
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
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			return false;
		}
		try {

			// dropAllTables();
			Connection conn = DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";create=true");

			// check if the database has the table already. Create if not
			DatabaseMetaData dmd = conn.getMetaData();
			ResultSet rs = dmd.getTables(null, null, EXAMPLES_TABLE_NAME, null);
			if (!rs.next()) {
				logger.debug("creating Examples table");
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(CREATE_EXMAPLES_TABLE);
			}
			rs = dmd.getTables(null, null, TAGS_TABLE_NAME, null);
			if (!rs.next()) {
				logger.debug("creating Tags table");
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(CREATE_TAGS_TABLE);
			}

			initialized = true;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	private static void dropAllTables() {
		Optional<Connection> connection = DatabaseManager.getDatabaseManager().getConnection();
		connection.ifPresent(conn -> {
			try {
				Statement s = conn.createStatement();
				int result = s.executeUpdate("DROP TABLE " + EXAMPLES_TABLE_NAME);
				logger.info("Deletion result {}", result);
				s = conn.createStatement();
				result = s.executeUpdate("DROP TABLE " + TAGS_TABLE_NAME);
				logger.info("Deletion result {}", result);
			} catch (SQLException e) {
				e.printStackTrace();
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
