package http.utility;

import java.net.*;
import java.util.Scanner;

public class HttpUtility {
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

    public static void makeGetRequest(HttpURLConnection con) {
        try {
            con.getResponseCode();
        } catch (Exception e) {
            throw new RuntimeException("Cannot make GET request!");
        }
    }

    public static String putDataToString(HttpURLConnection con) {
        try {
            Scanner scanner = new Scanner(con.getInputStream()).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("GET request not made before conversion!");
    }

    public static void closeUrlConnection(HttpURLConnection con) {
        try {
            con.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("Cannot close connection!");
        }
    }
}
