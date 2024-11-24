package io.github.sigmacasino;

import com.hubspot.jinjava.Jinjava;
import io.github.sigmacasino.routes.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class App {
    // private static final String[] MAIN_ROUTES = { "index", "games", "login", "register" };
    // private static final String[] GAMES_ROUTES = { "horse_racing", "roulette" };
    private HTTPRoute[] routes = {
        new Root(this),
        new Index(this),
    };

    private Logger logger = LoggerFactory.getLogger(getClass());
    private LocalDatabase db;

    private Jinjava jinjava = new Jinjava();

    private void run() {
        initializeDatabase();
        initializeSpark();
        addRoutes();
    }

    private void initializeDatabase() {
        var password = System.getenv("POSTGRES_PASSWORD");
        if (password == null) {
            logger.error("No password for the database has been set in the environment!");
            password = "";
        }
        db = new LocalDatabase(5_432, "sigmacasino", password, "sigmacasino");
        db.runScript(readResource("database_init.sql"));
    }

    private void initializeSpark() {
        Spark.staticFiles.location("/static");
        Spark.port(6_969);
    }

    private void addRoutes() {
        for (var route : routes) {
            route.registerSparkRoute();
            logger.info("Registered route: {}", route.path);
        }
    }

    public String readResource(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);

        if (inputStream == null) {
            logger.error("Failed to read resource file: {}", fileName);
            return "";
        }

        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String html = scanner.useDelimiter("\\A").next();
        scanner.close();
        return html;
    }

    public LocalDatabase getDatabase() {
        return db;
    }

    public Jinjava getJinjava() {
        return jinjava;
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("Main thread");
        new App().run();
    }
}
