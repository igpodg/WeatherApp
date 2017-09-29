package weather;

import http.utility.HttpUtility;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static org.junit.Assert.*;

public class WeatherRequestTest {
    private final static int HTTP_CODE_SUCCESS = 200;
    private static WeatherRequest request;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
        request = new WeatherRequest("", WeatherRequest.temperatureFormat.CELSIUS);
    }

    @Test
    public void testIsCorrectCurrentTemp() {
        try {
            String url = "";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
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

            assertEquals(response.toString(), String.format("%.1f C", request.getCurrentTemperature()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectHighestTemp() {
        try {
            String url = "";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
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

            assertEquals(response.toString(),
                    String.format("%.1f C",
                            request.getHighestTemperature(WeatherRequest.dayOfWeek.TOMORROW)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectLowestTemp() {
        try {
            String url = "";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
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

            assertEquals(response.toString(),
                    String.format("%.1f C",
                            request.getLowestTemperature(WeatherRequest.dayOfWeek.TOMORROW)));
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
        assertEquals(coords.length(), "x.xxxx:y.yyyy".length());

        assertEquals(coords.charAt(1), '.');
        assertEquals(coords.charAt(6), ':');
        assertEquals(coords.charAt(8), '.');

        try {
            Double.parseDouble(coords.substring(0, 6));
            Double.parseDouble(coords.substring(7));
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
