package io.github.sigmacasino;

import static spark.Spark.get;
import static spark.Spark.port;

public class App {
    public static void main(String[] args) {
        port(6969);

        get("/", (request, response) -> "Hello World!");
    }
}
