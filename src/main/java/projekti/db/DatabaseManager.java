package projekti.db;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;
import projekti.main.Main;

/**
 * Is used to manage database configuration.
 * 
 * @author vili
 *
 */
public class DatabaseManager {
	private String url;
	private String username;
	private String password;
	private Connection conn;
	private final String schemaPath = "sql/schema.sql";

	/**
	 * Creates database if it does not exist.
	 * 
	 * @param url      url for driver
	 * @param username username for db "sa"
	 * @param password password for db ""
	 * @throws SQLException if something goes wrong
	 */
	public DatabaseManager(String url, String username, String password) throws SQLException {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cant find H2 driver");
		}
		this.url = url;
		this.username = username;
		this.password = password;
		this.conn = null;
		this.setupSchema();
	}

	/**
	 * Make a safe connection. Useful if autocommit is of.
	 * 
	 * @return Connection connection to db.
	 * @throws SQLException if something goes wrong.
	 */
	public Connection connect() throws SQLException {
		if (this.conn == null || this.conn.isClosed()) {
			this.conn = DriverManager.getConnection(url, username, password);
		}
		return this.conn;
	}
	/**
	 * Convenience method for closing connection.
	 * @throws SQLException if something goes wrong.
	 */
	public void disconnect() throws SQLException {
		this.conn.close();
	}

	/**
	 * Schema is written in "IF EXISTS" format so this is safe to call every time.
	 *
	 * @throws SQLException if something goes wrong.
	 */
	public void setupSchema() throws SQLException {
		try (Connection c = this.connect();
				Statement stmnt = c.createStatement()) {
			this.connect();
			String schema = readResourceFile(this.schemaPath);
			stmnt.executeUpdate(schema);
		} catch (SQLException e) {
			throw e;
		}

	}

	private String readResourceFile(String filename) {
		try (InputStreamReader isr = new InputStreamReader(
				Main.class.getClassLoader().getResourceAsStream(filename));
				BufferedReader br = new BufferedReader(isr)) {
			String value = br.lines().collect(Collectors.joining("\n"));
			return value;
		} catch (IOException ex) {
			return "";
		}
	}

}
