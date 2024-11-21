package io.github.sigmacasino;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class App {
    private static final String[] MAIN_ROUTES = { "index", "games", "login", "register" };
    private static final String[] GAMES_ROUTES = { "horse_racing", "roulette" };

    private Logger logger = LoggerFactory.getLogger(getClass());
    private LocalDatabase db;

    private void run() {
        db = new LocalDatabase(5_432, "sigmacasino", System.getenv("POSTGRES_PASSWORD"), "sigmacasino");
        db.runScript(readResource("database_init.sql"));
        initializeSpark();
        addMainHTMLRoutes();
    }

    private void initializeSpark() {
        Spark.staticFiles.location("/static");
        Spark.port(6_969);
    }

    private void addMainHTMLRoutes() {
        Spark.get("/", (req, res) -> {
            res.redirect("/index");
            return "";
        });
        addHTMLRoutesByPath("", MAIN_ROUTES);
        addHTMLRoutesByPath("games/", GAMES_ROUTES);
    }

    private void addHTMLRoutesByPath(String pathPrefix, String[] routes) {
        for (var route : routes) {
            String path = "/" + pathPrefix + route;
            Spark.get(path, (req, res) -> {
                String html = readResource("templates" + path + ".html");
                res.type("text/html");
                return html;
            });
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

    public static void main(String[] args) {
        Thread.currentThread().setName("Main thread");
        new App().run();
    }
}
