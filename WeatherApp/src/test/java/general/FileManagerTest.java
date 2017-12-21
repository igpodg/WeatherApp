package general;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class FileManagerTest {
    @BeforeClass
    public static void setUpAllTests() {
        // before all tests
    }

    @Before
    public void setUpTest() {
        // before each test
    }

    @Test
    public void testGetSomeFile() {
        try {
            PrintWriter writer = new PrintWriter("test.txt", "UTF-8");
            writer.println("Hello world");
            writer.close();
            File file = new FileManager("test.txt").getFileByName();
            assertEquals("Hello world\n", new String(Files.readAllBytes(file.toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test (expected = RuntimeException.class)
    public void testGetNonExistingFile() {
        new FileManager("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").getFileByName();
    }

    @Test
    public void testCreateNewFile() {
        new File("test2.txt").delete();
        new FileManager("test2.txt").createNewFile();
        assertTrue(new File("test2.txt").exists());
    }

    @Test
    public void testWriteContents() {
        new File("test2.txt").delete();
        new FileManager("test2.txt").createNewFile();
        new FileManager("test2.txt").writeContents("contents", false, false);
        try {
            assertEquals("contents", new String(Files.readAllBytes(new File("test2.txt").toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteContentsLines() {
        new File("test2.txt").delete();
        new FileManager("test2.txt").createNewFile();
        new FileManager("test2.txt").writeContents("contents", true, false);
        try {
            assertEquals("contents\n", new String(Files.readAllBytes(new File("test2.txt").toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteContentsNoAppend() {
        new File("test2.txt").delete();
        new FileManager("test2.txt").createNewFile();
        new FileManager("test2.txt").writeContents("hello", false, false);
        new FileManager("test2.txt").writeContents("world", false, false);
        try {
            assertEquals("world", new String(Files.readAllBytes(new File("test2.txt").toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteContentsAppend() {
        new File("test2.txt").delete();
        new FileManager("test2.txt").createNewFile();
        new FileManager("test2.txt").writeContents("hello", false, true);
        new FileManager("test2.txt").writeContents("world", false, true);
        try {
            assertEquals("helloworld", new String(Files.readAllBytes(new File("test2.txt").toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadContentsCorrect() {
        try {
            PrintWriter out = new PrintWriter("randomfile.txt");
            out.println("random string!!!");
            out.close();

            File file = new FileManager("randomfile.txt").getFileByName();
            String loadedContents = new FileManager(file).loadContents();
            assertEquals("random string!!!", loadedContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test (expected = RuntimeException.class)
    public void testLoadContentsNoFile() {
        File file = new FileManager("nofile.txt").getFileByName();
        new FileManager(file).loadContents();
    }
}
