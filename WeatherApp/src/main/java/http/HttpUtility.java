package http;

import java.net.*;
import java.util.Scanner;

public class HttpUtility {
    public final static int HTTP_CODE_SUCCESS = 200;
    public final static int HTTP_CODE_NOTFOUND = 404;

    public static HttpURLConnection makeUrlConnection(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URI(url).toURL().openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "WeatherApp/1.0");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void makeGetRequest(HttpURLConnection con) throws RuntimeException {
        try {
            int currentResponse = con.getResponseCode();
            if (currentResponse == HTTP_CODE_NOTFOUND) {
                throw new RuntimeException("Page not found!");
            } else if (currentResponse != HTTP_CODE_SUCCESS) {
                throw new RuntimeException("GET request failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot make GET request!");
        }
    }

    public static String putDataToString(HttpURLConnection con) throws RuntimeException {
        try {
            Scanner scanner = new Scanner(con.getInputStream()).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("GET request not made before conversion!");
    }

    public static void closeUrlConnection(HttpURLConnection con) throws RuntimeException {
        try {
            con.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("Cannot close connection!");
        }
    }
}
