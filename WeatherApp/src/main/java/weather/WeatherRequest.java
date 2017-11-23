package weather;

import general.FileManager;
import general.TempConverter;
import http.HttpUtility;
import json.JsonObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherRequest {
    private final static String BASE_URL_FORMAT = "http://api.openweathermap.org/data/2.5/%s?q=%s,EE&appid=%s";
    private final static String CURRENT_WEATHER = "weather";
    private final static String FORECAST_WEATHER = "forecast";

    private final static String INPUT_FILENAME = "input.txt";
    private final static String OUTPUT_FILENAME = "output.txt";

    private final static String API_LOWEST_TEMP = "temp_min";
    private final static String API_HIGHEST_TEMP = "temp_max";

    private String apiKey;
    private WeatherConstants.TemperatureFormat format;
    private String currentCity;

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

    private boolean isCityDefined() {
        return !(this.currentCity == null || this.currentCity.isEmpty());
    }

    private void ensureCityIsDefined() {
        if (!isCityDefined()) {
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

    public WeatherRequest(String apiKey, WeatherConstants.TemperatureFormat defaultFormat) {
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

    public void setTemperatureFormat(WeatherConstants.TemperatureFormat format) {
        try {
            this.format = format;
        } catch (Exception e) {
            throw new RuntimeException("Cannot set temperature format!");
        }
    }

    private double getDoubleFromAPI(String weatherString, String key, boolean useDate, String dateToSearch) {
        if (!isCityDefined()) {
            return Double.NaN;
        }

        HttpURLConnection connection = HttpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, weatherString, this.currentCity, this.apiKey));
        HttpUtility.makeGetRequest(connection);

        String jsonString = HttpUtility.putDataToString(connection);
        HttpUtility.closeUrlConnection(connection);
        //System.out.println("jsonString: " + jsonString);
        JsonObject jsonObject = JsonObject.getJsonObject(jsonString);
        if (useDate) {
            int iterator = 0;
            String searchResult = null;
            do {
                searchResult = jsonObject.getValueByKey("list,dt_txt", iterator);
                iterator++;
            } while (!searchResult.contains(dateToSearch));
            jsonObject = JsonObject.getJsonObject(jsonObject.getValueByKey("list,main", iterator));
        }
        return jsonObject.getValueByKeyDouble(key);
    }

    private double getTemperatureInCurrentFormat(double kelvinTemp) {
        return TempConverter.getTemperatureInFormat(this.format, kelvinTemp);
    }

    public double getCurrentTemperature() {
        double kelvinTemp = getDoubleFromAPI(CURRENT_WEATHER, "main,temp", false, null);
        if (Double.isNaN(kelvinTemp)) {
            throw new RuntimeException("Cannot get current temperature!");
        }
        double currentTemp = getTemperatureInCurrentFormat(kelvinTemp);
        writeToFile(OUTPUT_FILENAME, String.valueOf(currentTemp));
        return currentTemp;
    }

    public double getLeveledTemperature(WeatherConstants.DayOfWeek day, WeatherConstants.TemperatureLevel level) {
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
        double answer = getDoubleFromAPI(FORECAST_WEATHER,
                (level == WeatherConstants.TemperatureLevel.LOWEST) ? API_LOWEST_TEMP : API_HIGHEST_TEMP,
                true, dateToSearch);
        if (Double.isNaN(answer)) {
            throw new RuntimeException("Cannot get leveled temperature");
        }

        return getTemperatureInCurrentFormat(answer);
    }

    public double getHighestTemperature(WeatherConstants.DayOfWeek day) {
        try {
            double maxTemp = getLeveledTemperature(day, WeatherConstants.TemperatureLevel.HIGHEST);
            writeToFile(OUTPUT_FILENAME, String.valueOf(maxTemp));
            return maxTemp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get highest temperature!");
        }
    }

    public double getLowestTemperature(WeatherConstants.DayOfWeek day) {
        try {
            double minTemp = getLeveledTemperature(day, WeatherConstants.TemperatureLevel.LOWEST);
            writeToFile(OUTPUT_FILENAME, String.valueOf(minTemp));
            return minTemp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get lowest temperature!");
        }
    }

    public String getGeoCoordinates() {
        try {
            double latitude = getDoubleFromAPI(
                    FORECAST_WEATHER, "city,coord,lat", false, null);
            double longitude = getDoubleFromAPI(
                    FORECAST_WEATHER, "city,coord,lon", false, null);
            String outputString = new GeoCoordinates(latitude, longitude).toString();
            writeToFile(OUTPUT_FILENAME, outputString);
            return outputString;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get geographical coordinates!");
        }
    }
}
