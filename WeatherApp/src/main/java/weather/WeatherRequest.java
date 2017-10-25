package weather;

import general.FileManager;
import http.HttpUtility;
import json.JsonObject;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class WeatherRequest {
    private final static String BASE_URL_FORMAT = "http://api.openweathermap.org/data/2.5/%s?q=%s,EE&appid=%s";
    private final static String CURRENT_WEATHER = "weather";
    private final static String FORECAST_WEATHER = "forecast";

    private final static String INPUT_FILENAME = "input.txt";
    private final static String OUTPUT_FILENAME = "output.txt";

    private String apiKey;
    private temperatureFormat format;
    private String currentCity;

    public enum dayOfWeek {
        TOMORROW,
        AFTER_TOMORROW,
        AFTER_AFTER_TOMORROW
    }

    public enum temperatureFormat {
        CELSIUS,
        FAHRENHEIT
    }

    private static String loadFromFileOrCreateNew(String fileName) {
        try {
            File file = FileManager.getFileByName(fileName);
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (Exception e) {
            FileManager.createNewFile(fileName);
        }

        return "";
    }

    private static void writeToFile(String fileName, String stringToWrite) {
        //Thread.dumpStack();
        loadFromFileOrCreateNew(fileName);
        if (fileName.equals(INPUT_FILENAME)) {
            FileManager.writeContents(fileName, stringToWrite, false, false);
        } else {
            FileManager.writeContents(fileName, stringToWrite, true, true);
        }
    }

    public WeatherRequest(String apiKey, temperatureFormat defaultFormat) {
        this.apiKey = apiKey;
        this.format = defaultFormat;
        this.currentCity = loadFromFileOrCreateNew(INPUT_FILENAME);
        loadFromFileOrCreateNew(OUTPUT_FILENAME);
    }

    public void setCity(String city) {
        this.currentCity = city;
        writeToFile(INPUT_FILENAME, city);
    }

    public String getCity() {
        writeToFile(OUTPUT_FILENAME, this.currentCity);
        return this.currentCity;
    }

    private static double convertToFahrenheit(double kelvinTemp) {
        try {
            return ((kelvinTemp - 273.15) * 9 / 5) + 32;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Fahrenheit!");
        }
    }

    private static double convertToCelsius(double kelvinTemp) {
        try {
            return kelvinTemp - 273.15;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Celsius!");
        }
    }

    public void setTemperatureFormat(temperatureFormat format) {
        try {
            this.format = format;
        } catch (Exception e) {
            throw new RuntimeException("Cannot set temperature format!");
        }
    }

    private void ensureCityIsPresent() {
        if (this.currentCity == null || this.currentCity.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("City not defined, do you want to type its name from console? (y/n): ");
            if (Character.toLowerCase(scanner.next().charAt(0)) == 'y') {
                scanner = new Scanner(System.in);
                System.out.print("Please enter city: ");
                setCity(scanner.nextLine());
            } else {
                throw new RuntimeException("City not defined!");
            }
        }
    }

    private double getDoubleFromAPI(String weatherString, String key) {
        try {
            ensureCityIsPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }

        HttpURLConnection connection = HttpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, weatherString, this.currentCity, this.apiKey));
        HttpUtility.makeGetRequest(connection);

        String jsonString = HttpUtility.putDataToString(connection);
        HttpUtility.closeUrlConnection(connection);
        //System.out.println("jsonString: " + jsonString);
        JsonObject jsonObject = JsonObject.getJsonObject(jsonString);
        return jsonObject.getValueByKeyDouble(key);
    }

    private double getDoubleFromAPIDate(String dateToSearch, String key) {
        try {
            ensureCityIsPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }

        HttpURLConnection connection = HttpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, FORECAST_WEATHER, this.currentCity, this.apiKey));
        HttpUtility.makeGetRequest(connection);

        String jsonString = HttpUtility.putDataToString(connection);
        //System.out.println("jsonString: " + jsonString);
        JsonObject jsonObject = JsonObject.getJsonObject(jsonString);
        int iterator = 0;
        String searchResult = null;
        do {
            searchResult = jsonObject.getValueByKey("list,dt_txt", iterator);
            iterator++;
        } while (!searchResult.contains(dateToSearch));
        jsonObject = JsonObject.getJsonObject(jsonObject.getValueByKey("list,main", iterator));
        return jsonObject.getValueByKeyDouble(key);
    }

    private double getTemperatureInCurrentFormat(double kelvinTemp) {
        if (this.format == temperatureFormat.CELSIUS) {
            return new BigDecimal(convertToCelsius(kelvinTemp)).setScale(10, RoundingMode.HALF_UP).doubleValue();
        } else if (this.format == temperatureFormat.FAHRENHEIT) {
            return new BigDecimal(convertToFahrenheit(kelvinTemp)).setScale(10, RoundingMode.HALF_UP).doubleValue();
        } else {
            throw new RuntimeException("Unknown temperature format set!");
        }
    }

    public double getCurrentTemperature() {
        double kelvinTemp = getDoubleFromAPI(CURRENT_WEATHER, "main,temp");
        if (Double.isNaN(kelvinTemp)) {
            throw new RuntimeException("Cannot get current temperature!");
        }
        double currentTemp = getTemperatureInCurrentFormat(kelvinTemp);
        writeToFile(OUTPUT_FILENAME, String.valueOf(currentTemp));
        return currentTemp;
    }

    private double getLeveledTemperature(dayOfWeek day, String levelString) {
        LocalDateTime currentDate = LocalDateTime.now();
        switch (day) {
            case TOMORROW:
                currentDate = currentDate.plusDays(1);
                break;
            case AFTER_TOMORROW:
                currentDate = currentDate.plusDays(2);
                break;
            case AFTER_AFTER_TOMORROW:
                currentDate = currentDate.plusDays(3);
                break;
        }
        String dateToSearch = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        double answer = getDoubleFromAPIDate(dateToSearch, levelString);
        if (Double.isNaN(answer)) {
            throw new RuntimeException("Cannot get leveled temperature");
        }

        return getTemperatureInCurrentFormat(answer);
    }

    public double getHighestTemperature(dayOfWeek day) {
        try {
            double maxTemp = getLeveledTemperature(day, "temp_max");
            writeToFile(OUTPUT_FILENAME, String.valueOf(maxTemp));
            return maxTemp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get highest temperature!");
        }
    }

    public double getLowestTemperature(dayOfWeek day) {
        try {
            double minTemp = getLeveledTemperature(day, "temp_min");
            writeToFile(OUTPUT_FILENAME, String.valueOf(minTemp));
            return minTemp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get lowest temperature!");
        }
    }

    public String getGeoCoordinates() {
        // xx.xxxx:yyy.yyyy
        try {
            double latitude = getDoubleFromAPI(FORECAST_WEATHER, "city,coord,lat");
            double longitude = getDoubleFromAPI(FORECAST_WEATHER, "city,coord,lon");
            String outputString = String.format(Locale.ROOT, "%07.4f:%08.4f", latitude, longitude);
            writeToFile(OUTPUT_FILENAME, outputString);
            return outputString;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get geographical coordinates!");
        }
    }
}
