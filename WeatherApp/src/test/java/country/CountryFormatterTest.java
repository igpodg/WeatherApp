package country;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CountryFormatterTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testNullFile() {
        try {
            CountryFormatter formatter = new CountryFormatter(null);
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNullCountryCode() {
        try {
            CountryFormatter formatter = new CountryFormatter("countries.txt");
            assertFalse(formatter.getCountryNameByCode(null).isPresent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testWrongCountryCode() {
        try {
            CountryFormatter formatter = new CountryFormatter("countries.txt");
            assertFalse(formatter.getCountryNameByCode("00").isPresent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCountryCodeExists() {
        try {
            CountryFormatter formatter = new CountryFormatter("countries.txt");
            assertTrue(formatter.getCountryNameByCode("US").isPresent());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCountryCodeCorrectResult() {
        try {
            CountryFormatter formatter = new CountryFormatter("countries.txt");
            if (!formatter.getCountryNameByCode("US").isPresent()) {
                fail();
            }
            assertEquals("United States", formatter.getCountryNameByCode("US").get());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
