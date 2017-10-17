package country;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

public class CountryFormatter {
    private final static String CANNOT_LOAD = "Cannot load list of countries from file!";

    private HashMap<String,String> countryList;

    public CountryFormatter(String fileName) {
        this.countryList = new HashMap<>();

        File f = new File(Paths.get(".").toAbsolutePath().normalize().toString() + "/countries.txt");
        if (fileName != null && f.exists() && !f.isDirectory()) {
            try {
                FileReader fileReader = new FileReader(f);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] countryArray = line.split(",");
                    countryList.put(countryArray[1].substring(1, countryArray[1].length() - 1),
                            countryArray[0].substring(1, countryArray[0].length() - 1));
                }
                fileReader.close();
            } catch (Exception e) {
                throw new RuntimeException(CANNOT_LOAD);
            }
        } else {
            throw new RuntimeException(CANNOT_LOAD);
        }
    }

    public Optional<String> getCountryNameByCode(String code) {
        return Optional.ofNullable(countryList.get(code));
    }
}
