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
            assertEquals(objJson.getValueByKey("two"), "2");
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
            assertEquals(objJson.getValueByKeyInt("two"), 2);
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
            assertEquals(objJson.getValueByKeyDouble("two"), 2.1234, 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
