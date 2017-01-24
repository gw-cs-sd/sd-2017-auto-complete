package gwcssd.autocomplete;

import static spark.Spark.*;

/**
 * API
 *
 */
public class Api {
    public static void main(String[] args) {
        get("/autocomplete", (req, res) -> "Coming Soon!");
    }
}
