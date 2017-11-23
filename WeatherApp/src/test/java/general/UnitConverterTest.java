package general;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitConverterTest {
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
        assertEquals(-273.15, UnitConverter.convertTempToCelsius(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertDecimalNumberToCelsius() {
        double kelvinTemp = 123.45;
        assertEquals(-149.70, UnitConverter.convertTempToCelsius(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertNegativeDecimalNumberToCelsius() {
        double kelvinTemp = -10000.76543;
        assertEquals(-10273.92, UnitConverter.convertTempToCelsius(kelvinTemp), 0.1);
    }

    @Test
    public void testConvertZeroToFahrenheit() {
        double kelvinTemp = 0;
        assertEquals(-459.67, UnitConverter.convertTempToFahrenheit(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertDecimalNumberToFahrenheit() {
        double kelvinTemp = 123.45;
        assertEquals(-237.46, UnitConverter.convertTempToFahrenheit(kelvinTemp), 0.001);
    }

    @Test
    public void testConvertNegativeDecimalNumberToFahrenheit() {
        double kelvinTemp = -10000.76543;
        assertEquals(-18461.05, UnitConverter.convertTempToFahrenheit(kelvinTemp), 0.1);
    }
}
