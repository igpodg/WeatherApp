package weather;

import http.utility.HttpUtility;
import json.JsonObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class WeatherRequestTest {
    private final static int HTTP_CODE_SUCCESS = 200;
    private static WeatherRequest request;
    private static String apiUrl;
    private static String apiKey = "fbbf4f7271272d04a548efe35b69c3cf";
    private static JsonObject jsonObj;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tallinn,EE" +
                "&appid=" + apiKey;

    }

    @Before
    public void setUpTest() {
        // before each test
        request = new WeatherRequest(apiKey,
                WeatherRequest.temperatureFormat.CELSIUS);
    }

    @Test
    public void testIsCorrectCurrentTemp() {
        try {
            HttpURLConnection con = HttpUtility.makeUrlConnection(apiUrl);
            assertNotNull(con);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            assertEquals(HTTP_CODE_SUCCESS, con.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObj = JsonObject.getJsonObject(response.toString());
            assertEquals(request.getCurrentTemperature(),
                    jsonObj.getValueByKeyDouble("main,temp") - 273.15, 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectHighestTemp() {
        apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Tallinn,EE" +
                "&appid=" + apiKey;
        try {
            HttpURLConnection con = HttpUtility.makeUrlConnection(apiUrl);
            assertNotNull(con);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            assertEquals(HTTP_CODE_SUCCESS, con.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObj = JsonObject.getJsonObject(response.toString());
            String afterAfterTomorrow = LocalDateTime.now().plusDays(1)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int iterator = 0;
            String searchResult = null;
            do {
                searchResult = jsonObj.getValueByKey("list,dt_txt", iterator);
                iterator++;
            } while (!searchResult.contains(afterAfterTomorrow));
            jsonObj = JsonObject.getJsonObject(jsonObj.getValueByKey("list,main", iterator));

            assertEquals(request.getHighestTemperature(WeatherRequest.dayOfWeek.TOMORROW),
                    new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_max")) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectLowestTemp() {
        apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Tallinn,EE" +
                "&appid=" + apiKey;
        try {
            HttpURLConnection con = HttpUtility.makeUrlConnection(apiUrl);
            assertNotNull(con);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            assertEquals(con.getResponseCode(), HTTP_CODE_SUCCESS);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObj = JsonObject.getJsonObject(response.toString());
            String afterAfterTomorrow = LocalDateTime.now().plusDays(3)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int iterator = 0;
            String searchResult = null;
            do {
                searchResult = jsonObj.getValueByKey("list,dt_txt", iterator);
                iterator++;
            } while (!searchResult.contains(afterAfterTomorrow));
            jsonObj = JsonObject.getJsonObject(jsonObj.getValueByKey("list,main", iterator));

            assertEquals(request.getLowestTemperature(WeatherRequest.dayOfWeek.AFTER_AFTER_TOMORROW),
                    new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_min")) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testNullGeoCoordinates() {
        assertNotNull(request.getGeoCoordinates());
    }

    @Test
    public void testCorrectFormatGeoCoordinates() {
        String coords = request.getGeoCoordinates();
        assertNotNull(coords);
        assertEquals("xx.xxxx:yyy.yyyy".length(), coords.length());

        assertEquals('.', coords.charAt(2));
        assertEquals(':', coords.charAt(7));
        assertEquals('.', coords.charAt(11));

        try {
            Double.parseDouble(coords.substring(0, 7));
            Double.parseDouble(coords.substring(8));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSetFormat() {
        try {
            WeatherRequest request = new WeatherRequest("", WeatherRequest.temperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherRequest.temperatureFormat.FAHRENHEIT);
            request.setTemperatureFormat(WeatherRequest.temperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherRequest.temperatureFormat.FAHRENHEIT);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
