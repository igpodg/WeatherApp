package json;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonObjectTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testBadJson() {
        try {
            String strJson = "123hello";
            JsonObject objJson = JsonObject.getJsonObject(strJson);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectJson() {
        try {
            String strJson = "{\"one\":\"1\",\"two\":\"2\"}";
            JsonObject objJson = JsonObject.getJsonObject(strJson);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetValue() {
        try {
            String strJson = "{\"one\":\"1\",\"two\":\"2\"}";
            JsonObject objJson = JsonObject.getJsonObject(strJson);
            assertEquals("2", objJson.getValueByKey("two"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetValueInt() {
        try {
            String strJson = "{\"one\":\"1\",\"two\":\"2\"}";
            JsonObject objJson = JsonObject.getJsonObject(strJson);
            assertEquals(2, objJson.getValueByKeyInt("two"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetValueDouble() {
        try {
            String strJson = "{\"one\":\"1\",\"two\":\"2.1233444444444444444444\"}";
            JsonObject objJson = JsonObject.getJsonObject(strJson);
            assertEquals(2.1234, objJson.getValueByKeyDouble("two"), 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
