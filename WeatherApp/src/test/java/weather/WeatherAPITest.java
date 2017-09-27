package weather;

import http.utility.HttpUtility;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static org.junit.Assert.*;

public class WeatherAPITest {
    private static int HTTP_CODE_SUCCESS = 200;
    private static WeatherAPI api;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
        api = new WeatherAPI();
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

            assertEquals(response.toString(), String.format("%.1f C", api.getCurrentTemperature()));
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
                    String.format("%.1f C", api.getHighestTemperature(WeatherAPI.dayOfWeek.TOMORROW)));
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
                    String.format("%.1f C", api.getLowestTemperature(WeatherAPI.dayOfWeek.AFTER_AFTER_TOMORROW)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testNullGeoCoordinates() {
        assertNotNull(api.getGeoCoordinates());
    }

    @Test
    public void testCorrectFormatGeoCoordinates() {
        String coords = api.getGeoCoordinates();
        assertNotNull(coords);
        assertEquals(coords.length(), "xxx:yyy".length());

        assertTrue(Character.isDigit(coords.charAt(0)));
        assertTrue(Character.isDigit(coords.charAt(1)));
        assertTrue(Character.isDigit(coords.charAt(2)));
        assertEquals(coords.charAt(3), ':');
        assertTrue(Character.isDigit(coords.charAt(4)));
        assertTrue(Character.isDigit(coords.charAt(5)));
        assertTrue(Character.isDigit(coords.charAt(6)));
    }
}
