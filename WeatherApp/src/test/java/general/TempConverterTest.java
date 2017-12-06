package general;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import weather.WeatherConstants;

import static org.junit.Assert.*;

public class TempConverterTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testConvertZeroToCelsius() {
        double kelvinTemp = 0;
        assertEquals(-273.15, TempConverter.convertToCelsius(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertDecimalNumberToCelsius() {
        double kelvinTemp = 123.45;
        assertEquals(-149.70, TempConverter.convertToCelsius(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertNegativeDecimalNumberToCelsius() {
        double kelvinTemp = -10000.76543;
        assertEquals(-10273.92, TempConverter.convertToCelsius(kelvinTemp), 0.1);
    }

    @Test
    public void testConvertZeroToFahrenheit() {
        double kelvinTemp = 0;
        assertEquals(-459.67, TempConverter.convertToFahrenheit(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertDecimalNumberToFahrenheit() {
        double kelvinTemp = 123.45;
        assertEquals(-237.46, TempConverter.convertToFahrenheit(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertNegativeDecimalNumberToFahrenheit() {
        double kelvinTemp = -10000.76543;
        assertEquals(-18461.05, TempConverter.convertToFahrenheit(kelvinTemp), 0.1);
    }

    @Test
    public void testGetTempInFormatCelsius() {
        double celsiusTemp = TempConverter.getTemperatureInFormat(
                WeatherConstants.TemperatureFormat.CELSIUS, 939.15);
        assertEquals(666, celsiusTemp, 0.01);
    }

    @Test
    public void testGetTempInFormatFahrenheit() {
        double fahrenheitTemp = TempConverter.getTemperatureInFormat(
                WeatherConstants.TemperatureFormat.FAHRENHEIT, 687.039);
        assertEquals(777, fahrenheitTemp, 0.01);
    }
}
