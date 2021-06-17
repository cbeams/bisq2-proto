package bisq;

import static spark.Spark.*;

public class HttpServer {
    public static void main(String[] args) {
        port(8080);
        get("/hello", (req, res) -> "Hello World");
    }
}
