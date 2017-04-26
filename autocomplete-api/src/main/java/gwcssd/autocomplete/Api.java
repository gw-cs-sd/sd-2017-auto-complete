package gwcssd.autocomplete;

import static spark.Spark.*;

/**
 * API
 *
 */
public class Api {
    public static void main(String[] args) {
        enableCORS("*", "GET,OPTIONS,POST", "*");

        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        get("/autocomplete/:text", (req, res) -> getCompletion(
            req.params(":text")
        ));
    }

    public static String getCompletion(String givenText) {
        MongoQuerier mq = new MongoQuerier();
        return mq.queryResults(givenText, -1).toJson();
    }

    // CORS setup.
    // This method is an initialization method and should be called once.
    // Original from: https://sparktutorials.github.io/2016/05/01/cors.html
    private static void enableCORS(
            final String origin, final String methods, final String headers) {

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Methods", methods);
            response.header("Access-Control-Allow-Headers", headers);
            response.type("application/json");
        });
    }
}
