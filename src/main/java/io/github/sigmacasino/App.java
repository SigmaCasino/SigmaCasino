package io.github.sigmacasino;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import spark.Spark;

public class App {
    private static final String[] MAIN_ROUTES = { "index", "games", "login", "register" };
    private static final String[] GAMES_ROUTES = { "horse_racing", "roulette" };

    private void run() {
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
                String html = loadFileContentsFromResources("/templates" + path + ".html");
                res.type("text/html");
                return html;
            });
        }
    }

    public static String loadFileContentsFromResources(String fileName) {
        InputStream inputStream = App.class.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("Wrong resource path!");
        }

        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String html = scanner.useDelimiter("\\A").next();
        scanner.close();
        return html;
    }

    public static void main(String[] args) {
        new App().run();
    }
}
