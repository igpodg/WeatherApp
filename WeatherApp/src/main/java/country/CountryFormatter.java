package country;

import java.util.HashMap;
import java.util.Optional;

public class CountryFormatter {
    private HashMap<String,String> countryList;

    public CountryFormatter(String fileName) {
        this.countryList = new HashMap<>();
        throw new RuntimeException("Cannot load list of countries from file!");
    }

    public Optional<String> getCountryNameByCode(String code) {
        return Optional.empty();
    }
}
