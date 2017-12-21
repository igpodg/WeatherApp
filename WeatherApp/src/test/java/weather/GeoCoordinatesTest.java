package weather;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import weather.WeatherConstants;

import static org.junit.Assert.*;

public class GeoCoordinatesTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testRegularCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(25.123456789, 98.987654321);
        assertEquals("25.1235:098.9877", coords.toString());
    }

    @Test
    public void testNegativeCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(-25.123456789, -98.987654321);
        assertEquals("-25.1235:-098.9877", coords.toString());
    }

    @Test
    public void testZeroCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(0, 0);
        assertEquals("00.0000:000.0000", coords.toString());
    }

    @Test
    public void testZeroNegativeCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(0, -12345.67896789);
        assertEquals("00.0000:-12345.6790", coords.toString());
    }

    @Test
    public void testNegativeZeroCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(-12345.67896789, 0);
        assertEquals("-12345.6790:000.0000", coords.toString());
    }

    @Test
    public void testVeryLongCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(328472349234.28347289479823473274,
                23048.23489723948723984793824783247);
        assertEquals("328472349234.2835:23048.2349", coords.toString());
    }

    @Test
    public void testNoRoundingCoordinates() {
        GeoCoordinates coords = new GeoCoordinates(328472349234.28341289479823473274,
                23048.23484723948723984793824783247);
        assertEquals("328472349234.2834:23048.2348", coords.toString());
    }
}
