package general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Optional;

public class CountryFormatter {
    private HashMap<String,String> countryList;

    public CountryFormatter(String fileName) throws RuntimeException {
        this.countryList = new HashMap<>();

        try {
            File file = new FileManager(fileName).getFileByName();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] countryArray = line.split(",");
                countryList.put(countryArray[1].substring(1, countryArray[1].length() - 1),
                        countryArray[0].substring(1, countryArray[0].length() - 1));
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load list of countries from file!");
        }
    }

    public Optional<String> getCountryNameByCode(String code) {
        return Optional.ofNullable(countryList.get(code));
    }
}
