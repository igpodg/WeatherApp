package general;

import java.io.*;
import java.nio.file.Paths;

public class FileManager {
    private static File getFileObject(String name) {
        return new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/" + name);
    }

    public static File getFileByName(String name) {
        File file = getFileObject(name);
        if (!(name != null && file.exists() && !file.isDirectory())) {
            throw new RuntimeException("Cannot load file!");
        }

        return file;
    }

    public static void createNewFile(String name) {
        try {
            getFileObject(name).createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new file!");
        }
    }

    public static void writeContents(String name, String contents, boolean useLines, boolean append) {
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
}
