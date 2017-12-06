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
            File file = FileManager.getFileByName("test.txt");
            assertEquals("Hello world\n", new String(Files.readAllBytes(file.toPath()))
                    .replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test (expected = RuntimeException.class)
    public void testGetNonExistingFile() {
        FileManager.getFileByName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    public void testCreateNewFile() {
        new File("test2.txt").delete();
        FileManager.createNewFile("test2.txt");
        assertTrue(new File("test2.txt").exists());
    }

    @Test
    public void testWriteContents() {
        new File("test2.txt").delete();
        FileManager.createNewFile("test2.txt");
        FileManager.writeContents("test2.txt", "contents", false, false);
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
        FileManager.createNewFile("test2.txt");
        FileManager.writeContents("test2.txt", "contents", true, false);
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
        FileManager.createNewFile("test2.txt");
        FileManager.writeContents("test2.txt", "hello", false, false);
        FileManager.writeContents("test2.txt", "world", false, false);
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
        FileManager.createNewFile("test2.txt");
        FileManager.writeContents("test2.txt", "hello", false, true);
        FileManager.writeContents("test2.txt", "world", false, true);
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

            File file = FileManager.getFileByName("randomfile.txt");
            String loadedContents = FileManager.loadContents(file);
            assertEquals("random string!!!", loadedContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test (expected = RuntimeException.class)
    public void testLoadContentsNoFile() {
        File file = FileManager.getFileByName("nofile.txt");
        FileManager.loadContents(file);
    }
}
