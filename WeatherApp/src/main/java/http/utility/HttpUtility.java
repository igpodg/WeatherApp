package http.utility;

import java.net.HttpURLConnection;

public class HttpUtility {
    public static HttpURLConnection makeUrlConnection(String url) {
        return null;
    }

    public static void makeGetRequest(HttpURLConnection con) {
        throw new RuntimeException("Cannot make GET request!");
    }

    public static String putDataToString(HttpURLConnection con) {
        throw new RuntimeException("GET request not made before conversion!");
    }

    public static void closeUrlConnection(HttpURLConnection con) {
        throw new RuntimeException("Cannot close connection!");
    }
}
