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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherRequestTest {
    private static WeatherRequest request;
    private static String apiUrl;
    private static String apiKey = "fbbf4f7271272d04a548efe35b69c3cf";
    private static JsonObject jsonObj;
    private static HttpUtility httpUtility;
    private static HttpUtility httpUtilityMock;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
        httpUtility = new HttpUtility();
        httpUtilityMock = mock(HttpUtility.class);
    }

    @Before
    public void setUpTest() {
        // before each test
        apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tallinn,EE" +
                "&appid=" + apiKey;
        request = new WeatherRequest(httpUtilityMock, apiKey,
                WeatherConstants.TemperatureFormat.CELSIUS);
        request.setCity("Tallinn");
    }

    private void tryLeveledTemp(String levelString, WeatherConstants.DayOfWeek day) {
        int dayOfWeekCode = 0;
        switch (day) {
            case TOMORROW:
                dayOfWeekCode = 1;
                break;
            case AFTER_TOMORROW:
                dayOfWeekCode = 2;
                break;
            case AFTER_AFTER_TOMORROW:
                dayOfWeekCode = 3;
                break;
        }
        apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Tallinn,EE" +
                "&appid=" + apiKey;
        try {
            HttpURLConnection con = httpUtility.makeUrlConnection(apiUrl);
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
            String afterAfterTomorrow = LocalDateTime.now().plusDays(dayOfWeekCode)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int iterator = 0;
            String searchResult = null;
            do {
                searchResult = jsonObj.getValueByKey("list,dt_txt", iterator);
                iterator++;
            } while (!searchResult.contains(afterAfterTomorrow));
            jsonObj = JsonObject.getJsonObject(jsonObj.getValueByKey("list,main", iterator));

            assertEquals(new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_" + levelString)) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(),
                    request.getLowestTemperature(day), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void tryCurrentTemp() {
        try {
            HttpURLConnection con = httpUtility.makeUrlConnection(apiUrl);
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

    private void tryMockedLeveledTemp(String levelString, WeatherConstants.DayOfWeek day) {
        int dayOfWeekCode = 0;
        switch (day) {
            case TOMORROW:
                dayOfWeekCode = 1;
                break;
            case AFTER_TOMORROW:
                dayOfWeekCode = 2;
                break;
            case AFTER_AFTER_TOMORROW:
                dayOfWeekCode = 3;
                break;
        }
        try {
            String json = "{\"cod\":\"200\",\"message\":0.0611,\"cnt\":39,\"list\":[{\"dt\":1513825200" +
                    ",\"main\":{\"temp\":276.27,\"temp_min\":276.27,\"temp_max\":276.847},\"dt_txt\":\"" +
                    "2017-12-21 03:00:00\"},{\"dt\":1513836000,\"main\":{\"temp\":276.97,\"temp_min\":" +
                    "276.97,\"temp_max\":277.406},\"dt_txt\":\"2017-12-21 06:00:00\"},{\"dt\":15138468" +
                    "00,\"main\":{\"temp\":275.97,\"temp_min\":275.97,\"temp_max\":276.26},\"dt_txt\":" +
                    "\"2017-12-21 09:00:00\"},{\"dt\":1513857600,\"main\":{\"temp\":275.63,\"temp_min\"" +
                    ":275.63,\"temp_max\":275.772},\"dt_txt\":\"2017-12-21 12:00:00\"},{\"dt\":15138684" +
                    "00,\"main\":{\"temp\":274.11,\"temp_min\":274.11,\"temp_max\":274.11},\"dt_txt\":\"" +
                    "2017-12-21 15:00:00\"},{\"dt\":1513879200,\"main\":{\"temp\":273.32,\"temp_min\":" +
                    "273.32,\"temp_max\":273.32},\"dt_txt\":\"2017-12-21 18:00:00\"},{\"dt\":1513890000" +
                    ",\"main\":{\"temp\":273.205,\"temp_min\":273.205,\"temp_max\":273.205},\"dt_txt\":" +
                    "\"2017-12-21 21:00:00\"},{\"dt\":1513900800,\"main\":{\"temp\":272.925,\"temp_min\"" +
                    ":272.925,\"temp_max\":272.925},\"dt_txt\":\"2017-12-22 00:00:00\"},{\"dt\":1513911" +
                    "600,\"main\":{\"temp\":273.728,\"temp_min\":273.728,\"temp_max\":273.728},\"dt_txt" +
                    "\":\"2017-12-22 03:00:00\"},{\"dt\":1513922400,\"main\":{\"temp\":273.423,\"temp_mi" +
                    "n\":273.423,\"temp_max\":273.423},\"dt_txt\":\"2017-12-22 06:00:00\"},{\"dt\":15139" +
                    "33200,\"main\":{\"temp\":274.778,\"temp_min\":274.778,\"temp_max\":274.778},\"dt_t" +
                    "xt\":\"2017-12-22 09:00:00\"},{\"dt\":1513944000,\"main\":{\"temp\":275.234,\"temp" +
                    "_min\":275.234,\"temp_max\":275.234},\"dt_txt\":\"2017-12-22 12:00:00\"},{\"dt\":1" +
                    "513954800,\"main\":{\"temp\":275.121,\"temp_min\":275.121,\"temp_max\":275.121},\"" +
                    "dt_txt\":\"2017-12-22 15:00:00\"},{\"dt\":1513965600,\"main\":{\"temp\":274.64,\"" +
                    "temp_min\":274.64,\"temp_max\":274.64},\"dt_txt\":\"2017-12-22 18:00:00\"},{\"dt\"" +
                    ":1513976400,\"main\":{\"temp\":274.212,\"temp_min\":274.212,\"temp_max\":274.212}" +
                    ",\"dt_txt\":\"2017-12-22 21:00:00\"},{\"dt\":1513987200,\"main\":{\"temp\":273.19" +
                    "9,\"temp_min\":273.199,\"temp_max\":273.199},\"dt_txt\":\"2017-12-23 00:00:00\"}," +
                    "{\"dt\":1513998000,\"main\":{\"temp\":272.463,\"temp_min\":272.463,\"temp_max\":" +
                    "272.463},\"dt_txt\":\"2017-12-23 03:00:00\"},{\"dt\":1514008800,\"main\":{\"temp" +
                    "\":272.44,\"temp_min\":272.44,\"temp_max\":272.44},\"dt_txt\":\"2017-12-23 06:00" +
                    ":00\"},{\"dt\":1514019600,\"main\":{\"temp\":274.218,\"temp_min\":274.218,\"temp_m" +
                    "ax\":274.218},\"dt_txt\":\"2017-12-23 09:00:00\"},{\"dt\":1514030400,\"main\":{\"te" +
                    "mp\":274.576,\"temp_min\":274.576,\"temp_max\":274.576},\"dt_txt\":\"2017-12-23 1" +
                    "2:00:00\"},{\"dt\":1514041200,\"main\":{\"temp\":273.637,\"temp_min\":273.637,\"t" +
                    "emp_max\":273.637},\"dt_txt\":\"2017-12-23 15:00:00\"},{\"dt\":1514052000,\"main\"" +
                    ":{\"temp\":274.271,\"temp_min\":274.271,\"temp_max\":274.271},\"dt_txt\":\"2017-12" +
                    "-23 18:00:00\"},{\"dt\":1514062800,\"main\":{\"temp\":277.619,\"temp_min\":277.619" +
                    ",\"temp_max\":277.619},\"dt_txt\":\"2017-12-23 21:00:00\"},{\"dt\":1514073600,\"ma" +
                    "in\":{\"temp\":277.426,\"temp_min\":277.426,\"temp_max\":277.426},\"dt_txt\":\"201" +
                    "7-12-24 00:00:00\"},{\"dt\":1514084400,\"main\":{\"temp\":276.149,\"temp_min\":276" +
                    ".149,\"temp_max\":276.149},\"dt_txt\":\"2017-12-24 03:00:00\"},{\"dt\":1514095200," +
                    "\"main\":{\"temp\":275.705,\"temp_min\":275.705,\"temp_max\":275.705},\"dt_txt\":\"2" +
                    "017-12-24 06:00:00\"},{\"dt\":1514106000,\"main\":{\"temp\":275.886,\"temp_min\":27" +
                    "5.886,\"temp_max\":275.886},\"dt_txt\":\"2017-12-24 09:00:00\"},{\"dt\":1514116800,\"" +
                    "main\":{\"temp\":275.949,\"temp_min\":275.949,\"temp_max\":275.949},\"dt_txt\":\"20" +
                    "17-12-24 12:00:00\"},{\"dt\":1514127600,\"main\":{\"temp\":275.371,\"temp_min\":27" +
                    "5.371,\"temp_max\":275.371},\"dt_txt\":\"2017-12-24 15:00:00\"},{\"dt\":1514138400" +
                    ",\"main\":{\"temp\":274.886,\"temp_min\":274.886,\"temp_max\":274.886},\"dt_txt\"" +
                    ":\"2017-12-24 18:00:00\"},{\"dt\":1514149200,\"main\":{\"temp\":274.328,\"temp_min" +
                    "\":274.328,\"temp_max\":274.328},\"dt_txt\":\"2017-12-24 21:00:00\"},{\"dt\":151416" +
                    "0000,\"main\":{\"temp\":273.733,\"temp_min\":273.733,\"temp_max\":273.733},\"dt_txt" +
                    "\":\"2017-12-25 00:00:00\"},{\"dt\":1514170800,\"main\":{\"temp\":273.933,\"temp_mi" +
                    "n\":273.933,\"temp_max\":273.933},\"dt_txt\":\"2017-12-25 03:00:00\"},{\"dt\":15141" +
                    "81600,\"main\":{\"temp\":276.035,\"temp_min\":276.035,\"temp_max\":276.035},\"dt_tx" +
                    "t\":\"2017-12-25 06:00:00\"},{\"dt\":1514192400,\"main\":{\"temp\":274.965,\"temp_mi" +
                    "n\":274.965,\"temp_max\":274.965},\"dt_txt\":\"2017-12-25 09:00:00\"},{\"dt\":151420" +
                    "3200,\"main\":{\"temp\":274.488,\"temp_min\":274.488,\"temp_max\":274.488},\"dt_txt" +
                    "\":\"2017-12-25 12:00:00\"},{\"dt\":1514214000,\"main\":{\"temp\":274.087,\"temp_min" +
                    "\":274.087,\"temp_max\":274.087},\"dt_txt\":\"2017-12-25 15:00:00\"},{\"dt\":1514224800,\"" +
                    "main\":{\"temp\":274.296,\"temp_min\":274.296,\"temp_max\":274.296},\"dt_txt\":\"2017" +
                    "-12-25 18:00:00\"},{\"dt\":1514235600,\"main\":{\"temp\":273.438,\"temp_min\":273.438" +
                    ",\"temp_max\":273.438},\"dt_txt\":\"2017-12-25 21:00:00\"}]}";
            when(httpUtilityMock.putDataToString(any())).thenReturn(json);

            jsonObj = JsonObject.getJsonObject(json);
            String afterAfterTomorrow = LocalDateTime.now().plusDays(dayOfWeekCode)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int iterator = 0;
            String searchResult = null;
            do {
                searchResult = jsonObj.getValueByKey("list,dt_txt", iterator);
                iterator++;
            } while (!searchResult.contains(afterAfterTomorrow));
            jsonObj = JsonObject.getJsonObject(jsonObj.getValueByKey("list,main", iterator));

            assertEquals(new BigDecimal(Double.parseDouble(jsonObj.getValueByKey("temp_" + levelString)) - 273.15)
                            .setScale(10, RoundingMode.HALF_UP).doubleValue(),
                    request.getLowestTemperature(day), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

        @Test
    public void testCorrectHighestTemp() {
        tryMockedLeveledTemp("max", WeatherConstants.DayOfWeek.TOMORROW);
        tryMockedLeveledTemp("max", WeatherConstants.DayOfWeek.AFTER_TOMORROW);
        tryMockedLeveledTemp("max", WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW);
    }

    @Test
    public void testCorrectLowestTemp() {
        tryMockedLeveledTemp("min", WeatherConstants.DayOfWeek.TOMORROW);
        tryMockedLeveledTemp("min", WeatherConstants.DayOfWeek.AFTER_TOMORROW);
        tryMockedLeveledTemp("min", WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW);
    }

    @Test
    public void testNullGeoCoordinates() {
        String json = "{\"cod\":\"200\",\"message\":0.0132,\"cnt\":39,\"city\":{\"id\":590447,\"name\"" +
                ":\"Tallinn\",\"coord\":{\"lat\":59.4372,\"lon\":24.7454},\"country\":\"EE\",\"population\":16630}}";
        when(httpUtilityMock.putDataToString(any())).thenReturn(json);
        assertNotNull(request.getGeoCoordinates());
    }

    @Test
    public void testCorrectFormatGeoCoordinates() {
        String json = "{\"cod\":\"200\",\"message\":0.0132,\"cnt\":39,\"city\":{\"id\":590447,\"name\"" +
                ":\"Tallinn\",\"coord\":{\"lat\":59.4372,\"lon\":24.7454},\"country\":\"EE\",\"population\":16630}}";
        when(httpUtilityMock.putDataToString(any())).thenReturn(json);
        String coords = request.getGeoCoordinates();
        assertNotNull(coords);
        assertEquals("xx.xxxx:yyy.yyyy".length(), coords.length());

        assertEquals('.', coords.charAt(2));
        assertEquals(':', coords.charAt(7));
        assertEquals('.', coords.charAt(11));

        try {
            Double.parseDouble(coords.substring(0, 7));
            Double.parseDouble(coords.substring(8));
            assertEquals("59.4372:024.7454", coords);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSetFormat() {
        try {
            WeatherRequest request = new WeatherRequest(null, "",
                    WeatherConstants.TemperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.CELSIUS);
            request.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test (expected = RuntimeException.class)
    public void testSetGetCityNull() {
        request.setCity(null);
        request.getCity();
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
        String json = "{\"coord\":{\"lon\":26.72,\"lat\":58.37},\"weather\":[{\"id\":803," +
                "\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}]," +
                "\"base\":\"stations\",\"main\":{\"temp\":272.15,\"pressure\":1021,\"humidity\"" +
                ":80,\"temp_min\":272.15,\"temp_max\":272.15},\"visibility\":10000,\"wind\":{\"" +
                "speed\":5.7,\"deg\":190},\"clouds\":{\"all\":80},\"dt\":1513821000,\"sys\":{\"" +
                "type\":1,\"id\":5015,\"message\":0.002,\"country\":\"EE\",\"sunrise\":1513839636" +
                ",\"sunset\":1513862521},\"id\":588335,\"name\":\"Tartu\",\"cod\":200}";
        when(httpUtilityMock.putDataToString(any())).thenReturn(json);
        apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=Tartu,EE" +
                "&appid=" + apiKey;
        request.setCity("Tartu");
        //tryCurrentTemp();
        jsonObj = JsonObject.getJsonObject(json);
        assertEquals(jsonObj.getValueByKeyDouble("main,temp") - 273.15,
                request.getCurrentTemperature(), 0.001);
    }
}
