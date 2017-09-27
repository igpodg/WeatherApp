package http.utility;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class HttpUtilityTest {
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
    // Test actual connection to http
    public void testHttpSuccessfulConnection() {
        try {
            String url = "";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            assertNotNull(con);
            assertEquals(con.getResponseCode(), HTTP_CODE_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMakeGetRequest() {
        try {
            String url = "";
            HttpUtility.makeGetRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
