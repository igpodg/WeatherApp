package general;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {
    private String name;
    private File file;

    public FileManager(String name) {
        this.name = name;
    }

    public FileManager(File file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    private File getFileObject() {
        return (file == null) ?
                new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/" + name) : file;
    }

    public File getFileByName() throws RuntimeException {
        File file = getFileObject();
        if (!(name != null && file.exists() && !file.isDirectory())) {
            throw new RuntimeException("Cannot load file!");
        }

        return file;
    }

    public void createNewFile() throws RuntimeException {
        try {
            getFileObject().createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new file!");
        }
    }

    public void writeContents(String contents, boolean useLines, boolean append)
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

    public String loadContents() throws RuntimeException {
        try {
            return new Scanner(getFileObject()).useDelimiter("\\Z").next();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load file contents!");
        }
    }
}
