package io.github.sigmacasino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        try {
            String url = String.format("jdbc:postgresql://localhost:%d/%s", port, database);
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            props.setProperty("ssl", "true");
            connection = DriverManager.getConnection(url, props);
            logger.info("Successfully connected to {}.", url);
        } catch (SQLException ex) {
            logger.error(
                "Failed to connect to the PostgreSQL database! Using mock adapter, no queries will succeed.", ex
            );
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
        // TODO
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
