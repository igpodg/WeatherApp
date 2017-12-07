package general;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {
    private static File getFileObject(String name) {
        return new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/" + name);
    }

    public static File getFileByName(String name) throws RuntimeException {
        File file = getFileObject(name);
        if (!(name != null && file.exists() && !file.isDirectory())) {
            throw new RuntimeException("Cannot load file!");
        }

        return file;
    }

    public static void createNewFile(String name) throws RuntimeException {
        try {
            getFileObject(name).createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new file!");
        }
    }

    public static void writeContents(String name, String contents, boolean useLines, boolean append)
            throws RuntimeException {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(name, append));
            if (useLines) {
                writer.println(contents);
            } else {
                writer.print(contents);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Cannot find file!");
        }
    }

    public static String loadContents(File file) throws RuntimeException {
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load file contents!");
        }
    }
}
