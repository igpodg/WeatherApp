package weather;

import general.FileManager;
import http.HttpUtility;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WeatherReportTest {
    private static String apiKey = "fbbf4f7271272d04a548efe35b69c3cf";
    private final static String INPUT_FILENAME = "input.txt";
    private static WeatherReport reportObject;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    private void setUpDefault() {
        reportObject = new WeatherReport(new WeatherRequest(new HttpUtility(),
                apiKey, WeatherConstants.TemperatureFormat.CELSIUS), null, LocalDateTime::now, null);
    }

    private void setUpStubbed(String[] cities, FileManager manager) {
        WeatherRequest requestMock = mock(WeatherRequest.class);
        when(requestMock.getCity()).thenReturn("CITYNAME");
        when(requestMock.getGeoCoordinates()).thenReturn("CO.ORDI:NAT.ES");
        when(requestMock.getHighestTemperature(WeatherConstants.DayOfWeek.TOMORROW)).thenReturn(10d);
        when(requestMock.getHighestTemperature(WeatherConstants.DayOfWeek.AFTER_TOMORROW)).thenReturn(20d);
        when(requestMock.getHighestTemperature(WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW)).thenReturn(30d);
        when(requestMock.getLowestTemperature(WeatherConstants.DayOfWeek.TOMORROW)).thenReturn(40d);
        when(requestMock.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_TOMORROW)).thenReturn(50d);
        when(requestMock.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW)).thenReturn(60d);
        when(requestMock.getCurrentTemperature()).thenReturn(70d);
        reportObject = new WeatherReport(requestMock, cities, () -> LocalDateTime.of(
                2001, Month.FEBRUARY, 3, 4, 5, 6), manager);
    }

    @Test
    public void testGetFromInputEmptyLines() {
        new FileManager(INPUT_FILENAME).getFileByName().delete();
        new FileManager(INPUT_FILENAME).createNewFile();
        new FileManager(INPUT_FILENAME).writeContents("\n\nINPUTCITY\n", false, false);
        setUpStubbed(null, null);

        String output = reportObject.getReport();
        String expected = "\nIlma raport -- 3 Feb 2001 04:05:06:\n" +
                "\t- Linn: INPUTCITY\n" +
                "\t- Linna koordinaadid: lat CO.ORDI lon NAT.ES\n" +
                "\t- Maksimaalne temperatuur:\n" +
                "\t\t- Homme: 10.00 °C\n" +
                "\t\t- Ülehomme: 20.00 °C\n" +
                "\t\t- Üleülehomme: 30.00 °C\n" +
                "\t- Minimaalne temperatuur:\n" +
                "\t\t- Homme: 40.00 °C\n" +
                "\t\t- Ülehomme: 50.00 °C\n" +
                "\t\t- Üleülehomme: 60.00 °C\n" +
                "\t- Praegune temperatuur: 70.00 °C\n";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTwoCitiesFahrenheit() {
        new FileManager(INPUT_FILENAME).getFileByName().delete();
        new FileManager(INPUT_FILENAME).createNewFile();
        new FileManager(INPUT_FILENAME).writeContents("Bigcity\nSmallcity", false, false);
        setUpStubbed(null, null);
        reportObject.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);

        String output = reportObject.getReport();
        String expected = "\nIlma raport -- 3 Feb 2001 04:05:06:\n" +
                "\t- Linn: Bigcity\n" +
                "\t- Linna koordinaadid: lat CO.ORDI lon NAT.ES\n" +
                "\t- Maksimaalne temperatuur:\n" +
                "\t\t- Homme: 10.00 °F\n" +
                "\t\t- Ülehomme: 20.00 °F\n" +
                "\t\t- Üleülehomme: 30.00 °F\n" +
                "\t- Minimaalne temperatuur:\n" +
                "\t\t- Homme: 40.00 °F\n" +
                "\t\t- Ülehomme: 50.00 °F\n" +
                "\t\t- Üleülehomme: 60.00 °F\n" +
                "\t- Praegune temperatuur: 70.00 °F\n" +
                "\nIlma raport -- 3 Feb 2001 04:05:06:\n" +
                "\t- Linn: Smallcity\n" +
                "\t- Linna koordinaadid: lat CO.ORDI lon NAT.ES\n" +
                "\t- Maksimaalne temperatuur:\n" +
                "\t\t- Homme: 10.00 °F\n" +
                "\t\t- Ülehomme: 20.00 °F\n" +
                "\t\t- Üleülehomme: 30.00 °F\n" +
                "\t- Minimaalne temperatuur:\n" +
                "\t\t- Homme: 40.00 °F\n" +
                "\t\t- Ülehomme: 50.00 °F\n" +
                "\t\t- Üleülehomme: 60.00 °F\n" +
                "\t- Praegune temperatuur: 70.00 °F\n";
        assertEquals(expected, output);
    }

    @Test
    public void testVerifyInputAsked() {
        ByteArrayInputStream in = new ByteArrayInputStream("n".getBytes());
        System.setIn(in);

        FileManager mockedManager = mock(FileManager.class);
        try {
            setUpStubbed(null, mockedManager);
            reportObject.getReport();
        } catch (RuntimeException e) {
            verify(mockedManager).setName("input.txt");
        } finally {
            System.setIn(System.in);
        }
    }

    @Test
    public void testVerifyCityWritten() {
        FileManager mockedManager = mock(FileManager.class);
        String[] cities = {"CityToCreate"};
        setUpStubbed(cities, mockedManager);
        reportObject.getReport();
        verify(mockedManager).setName("CityToCreate.txt");
    }
}
