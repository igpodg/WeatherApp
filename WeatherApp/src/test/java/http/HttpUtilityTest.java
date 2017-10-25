package http;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.*;

public class HttpUtilityTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testHttpConnectionReturnNull() {
        try {
            String url = "https://example.com/";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            assertNotNull(con);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    // Test actual connection to http
    public void testHttpSuccessfulConnection() {
        try {
            String url = "https://www.example.com/";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            assertEquals(HttpUtility.HTTP_CODE_SUCCESS, con.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testHttpBadConnection() {
        try {
            String url = "aaaaa://website.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            assertTrue(con == null || con.getResponseCode() != HttpUtility.HTTP_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMakeGetRequest() {
        try {
            String url = "https://example.com/";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            HttpUtility.makeGetRequest(con);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCorrectString() {
        try {
            String url = "https://www.le.ac.uk/oerresources/bdra/html/resources/example.txt";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            HttpUtility.makeGetRequest(con);
            String result = HttpUtility.putDataToString(con);
            assertEquals("\nThis is an example of a simple ASCII text file stored on a Web server. " +
                    "Note that it has a file\nextension of \".txt\".\n\nAlthough such files may contains some basic " +
                    "layout formatting, such as paragraphs, there is no\nsupport for the text to have attributes, " +
                    "such as bolding.\n\nText files can contain Hypertext Mark-up codes but these will not be " +
                    "interpreted by the \nbrowser. For example, if the following characters <strong>hello</strong> " +
                    "were typed into an\n\"html\" file then the word \"hello\" would be shown in bold.\n\n", result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testConnectionClose() {
        try {
            String url = "https://example.com/";
            HttpURLConnection con = HttpUtility.makeUrlConnection(url);
            HttpUtility.closeUrlConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
