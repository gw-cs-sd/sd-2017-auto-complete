package gwcssd.autocomplete;

import static spark.Spark.*;

/**
 * API
 *
 */
public class Api {
    public static void main(String[] args) {
        get("/autocomplete/:text", (req, res) -> getCompletion(
            req.params(":text")
        ));
    }

    public static String getCompletion(String givenText) {
        MongoQuerier mq = new MongoQuerier();
        return mq.queryResults(givenText).toJson();
    }
}
