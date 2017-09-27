package json.utility;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilityTest {
    private static int HTTP_CODE_SUCCESS = 200;

    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testJsonTooManyCommas() {
        String json = "{\"one\":\"1\",,,\"two\":\"2\"}";
        assertFalse(JsonUtility.checkJsonCorrect(json));
    }

    @Test
    public void testJsonFinalComma() {
        String json = "{\"one\":\"1\",\"two\":\"2\",}";
        assertFalse(JsonUtility.checkJsonCorrect(json));
    }

    @Test
    public void testJsonArrayNeverEnds() {
        String json = "{\"one\":[\"1\",\"2\",\"two\":\"2\"}";
        assertFalse(JsonUtility.checkJsonCorrect(json));
    }

    @Test
    public void testJsonTooManyQuotes() {
        String json = "{\"one\":\"1\"\"}";
        assertFalse(JsonUtility.checkJsonCorrect(json));
    }

    @Test
    public void testJsonNotJson() {
        String json = "123hello";
        assertFalse(JsonUtility.checkJsonCorrect(json));
    }

    @Test
    public void testJsonValidJson() {
        String json = "{\"title\":\"Person\",\"type\":\"object\",\"properties\":{\"firstName\":" +
                "{\"type\":\"string\"},\"lastName\":{\"type\":\"string\"},\"age\":{\"description\":" +
                "\"Age in years\",\"type\":\"integer\",\"minimum\":0}},\"required\":[\"firstName\",\"lastName\"]}";
        assertTrue(JsonUtility.checkJsonCorrect(json));
        fail();
    }
}
