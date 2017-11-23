package weather;

import general.FileManager;
import http.HttpUtility;
import json.JsonObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.Assert.*;

public class WeatherRequestTest {
    private static WeatherRequest request;
    private static String apiUrl;
    private static String apiKey = "fbbf4f7271272d04a548efe35b69c3cf";
    private static JsonObject jsonObj;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests

    }

    @Before
    public void setUpTest() {
        // before each test
        apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tallinn,EE" +
                "&appid=" + apiKey;
        request = new WeatherRequest(apiKey,
                WeatherConstants.TemperatureFormat.CELSIUS);
        request.setCity("Tallinn");
    }

    @Test
    public void testIsCorrectCurrentTemp() {
        try {
            HttpURLConnection con = HttpUtility.makeUrlConnection(apiUrl);
            assertNotNull(con);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            assertEquals(HttpUtility.HTTP_CODE_SUCCESS, con.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObj = JsonObject.getJsonObject(response.toString());
            assertEquals(jsonObj.getValueByKeyDouble("main,temp") - 273.15,
                    request.getCurrentTemperature(), 0.001);
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
            assertEquals(HttpUtility.HTTP_CODE_SUCCESS, con.getResponseCode());

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

            assertEquals(new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_max")) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(),
                    request.getHighestTemperature(WeatherConstants.DayOfWeek.TOMORROW), 0.001);
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
            assertEquals(con.getResponseCode(), HttpUtility.HTTP_CODE_SUCCESS);

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

            assertEquals(new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_min")) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(),
                    request.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW), 0.001);
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
            WeatherRequest request = new WeatherRequest("", WeatherConstants.TemperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSetGetCityNull() {
        request.setCity(null);
        assertEquals(null, request.getCity());
    }

    @Test
    public void testSetGetCityLongString() {
        request.setCity("'Sc]rDKSb8us@JR/TdQCGELe~q+X5gw=k8r#?jgRY>BEg(<`T&'d}@#{2N']" +
                "kpH\\qZU?x`?kq4KTFd!{^:r\\Q[nwbvSM+8,&Jd:r-YnGU$M`q,Df)v.fK=f5XUshS)hn");
        assertEquals("'Sc]rDKSb8us@JR/TdQCGELe~q+X5gw=k8r#?jgRY>BEg(<`T&'d}@#{2N']" +
                "kpH\\qZU?x`?kq4KTFd!{^:r\\Q[nwbvSM+8,&Jd:r-YnGU$M`q,Df)v.fK=f5XUshS)hn", request.getCity());
    }

    @Test
    public void testIsCorrectCurrentTempSetCityTartu() {
        apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tartu,EE" +
                "&appid=" + apiKey;
        request.setCity("Tartu");
        try {
            HttpURLConnection con = HttpUtility.makeUrlConnection(apiUrl);
            assertNotNull(con);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            assertEquals(HttpUtility.HTTP_CODE_SUCCESS, con.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObj = JsonObject.getJsonObject(response.toString());
            assertEquals(jsonObj.getValueByKeyDouble("main,temp") - 273.15,
                    request.getCurrentTemperature(),0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /*@Test
    public void testSetCityFromConsole() {
        request.setCity("");
        ByteArrayInputStream in = new ByteArrayInputStream(
                "y\nNarva\n".getBytes());
        System.setIn(in);
        System.out.println(request.getCurrentTemperature());
        System.setIn(System.in);
        assertEquals("Narva", request.getCity());
    }*/

    @Test
    public void testCorrectInput() {
        try {
            request.setCity("SomecitycitySomeCity");
            String fileContents = new String(Files.readAllBytes(Paths.get("./input.txt")));
            assertEquals("SomecitycitySomeCity", fileContents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectOutput() {
        try {
            FileManager.writeContents("output.txt", "", false, false);
            double result = request.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_TOMORROW);
            String fileContents = new String(Files.readAllBytes(Paths.get("./output.txt")))
                    .replace("\r", "");
            assertEquals(String.format(Locale.ROOT, "%.3f\n", result), fileContents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
