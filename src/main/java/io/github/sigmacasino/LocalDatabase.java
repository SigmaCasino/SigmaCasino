package io.github.sigmacasino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDatabase implements AutoCloseable {
    private Connection connection;

    private int port;
    private String user;
    private String database;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Connection getConnection() {
        return connection;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getDatabaseName() {
        return database;
    }

    public LocalDatabase(int port, String user, String password, String database) {
        this.port = port;
        this.user = user;
        this.database = database;
        connect(password);
    }

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
                Thread.sleep(backoff * 1000L);
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

    public void runScript(String script) {
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
