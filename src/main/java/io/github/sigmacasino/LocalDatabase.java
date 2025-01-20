package io.github.sigmacasino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple wrapper around a PostgreSQL database connection. It is used to connect to a local
 * database and run SQL scripts/queries.
 */
public class LocalDatabase implements AutoCloseable {
    /**
     * The JDBC connection to the PostgreSQL database.
     */
    private Connection connection;

    /**
     * The port number of the PostgreSQL database.
     */
    private int port;
    /**
     * The username to connect to the PostgreSQL database.
     */
    private String user;
    /**
     * The name of the database to connect to.
     */
    private String database;

    /**
     * The logger for this class.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Gets the raw connection to the PostgreSQL database.
     *
     * @return the connection to the PostgreSQL database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Gets the port number of the PostgreSQL database.
     *
     * @return the port number of the PostgreSQL database
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the username to connect to the PostgreSQL database.
     *
     * @return the username to connect to the PostgreSQL database
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the name of the database to connect to.
     *
     * @return the name of the database to connect to
     */
    public String getDatabaseName() {
        return database;
    }

    /**
     * Creates a new LocalDatabase object and connects to the PostgreSQL database.
     *
     * @param port the port number of the PostgreSQL database
     * @param user the username to connect to the PostgreSQL database
     * @param password the password to connect to the PostgreSQL database
     * @param database the name of the database to connect to
     */
    public LocalDatabase(int port, String user, String password, String database) {
        this.port = port;
        this.user = user;
        this.database = database;
        connect(password);
    }

    /**
     * Connects to the PostgreSQL database using the given password via JDBC.
     * If the connection fails, it will retry 5 times with an exponential backoff,
     * starting from 1s and doubling for each failure.
     * If the connection is successful, it will log the success.
     *
     * @param password the password to connect to the PostgreSQL database
     */
    private void connect(String password) {
        String url = String.format("jdbc:postgresql://localhost:%d/%s", port, database);
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        int retries = 5;
        int backoff = 1;
        SQLException lastException = null;
        while (connection == null && retries-- > 0) {
            try {
                connection = DriverManager.getConnection(url, props);
            } catch (SQLException ex) {
                logger.warn("Failed to connect to the PostgreSQL database! Retrying in {}s.", backoff);
                lastException = ex;
            }
            try {
                Thread.sleep(backoff * 1_000L);
            } catch (InterruptedException ex) {
                break;
            }
            backoff *= 2;
        }
        if (connection != null) {
            logger.info("Successfully connected to {}", url);
        } else {
            logger.error("Failed to connect to the PostgreSQL database! No queries will succeed.", lastException);
        }
    }

    /**
     * Prepares a statement for the given SQL template.
     *
     * @param sqlTemplate the SQL template to prepare a statement for
     * @return the prepared statement for the given SQL template
     * @see Connection#prepareStatement(String)
     */
    public PreparedStatement prepareStatement(String sqlTemplate) {
        if (connection != null) {
            try {
                return connection.prepareStatement(sqlTemplate);
            } catch (SQLException ex) {
                logger.error("Failed to create statement, returning null.", ex);
            }
        } else {
            logger.warn("No database connection, returning null statement.");
        }
        return null;
    }

    /**
     * Runs the given SQL script by splitting it into individual queries and executing them in a
     * batch statement. It filters out comments and empty lines.
     *
     * @param script the SQL script contents
     */
    public void runScript(String script) {
        if (connection == null) {
            logger.warn("Cannot run the SQL script, because there is no database connection.");
            return;
        }
        var lines = script.split("\n");
        var currentQuery = new StringBuilder();
        var queries = new ArrayList<String>();
        for (var line : lines) {
            int comment = line.indexOf("--");
            if (comment != -1) {
                line = line.substring(0, comment);
            }
            line = line.trim();
            currentQuery.append(' ');
            currentQuery.append(line);
            if (line.endsWith(";")) {
                var s = currentQuery.toString().trim();
                queries.add(s);
                logger.debug("Adding query to batch statement: {}", s);
                currentQuery = new StringBuilder();
            }
        }
        try (var stmt = connection.createStatement()) {
            for (var query : queries) {
                stmt.addBatch(query);
            }
            stmt.executeBatch();
            logger.info("Database script was run successfully.");
        } catch (SQLException ex) {
            logger.error("Failed to run query: {}", currentQuery, ex);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
