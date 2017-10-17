package weather;

import http.utility.HttpUtility;
import json.JsonObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherRequest {
    private final static String BASE_URL_FORMAT = "http://api.openweathermap.org/data/2.5/%s?q=Tallinn,EE&appid=%s";
    private final static String CURRENT_WEATHER = "weather";
    private final static String FORECAST_WEATHER = "forecast";

    private String apiKey;
    private temperatureFormat format;

    public enum dayOfWeek {
        TODAY,
        TOMORROW,
        AFTER_TOMORROW,
        AFTER_AFTER_TOMORROW
    }

    public enum temperatureFormat {
        CELSIUS,
        FAHRENHEIT
    }

    public WeatherRequest(String apiKey, temperatureFormat defaultFormat) {
        this.apiKey = apiKey;
        this.format = defaultFormat;
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

    private double getDoubleFromAPI(String weatherString, String key) {
        HttpURLConnection connection = HttpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, weatherString, this.apiKey));
        HttpUtility.makeGetRequest(connection);

        String jsonString = HttpUtility.putDataToString(connection);
        HttpUtility.closeUrlConnection(connection);
        //System.out.println("jsonString: " + jsonString);
        JsonObject jsonObject = JsonObject.getJsonObject(jsonString);
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
        try {
            double kelvinTemp = getDoubleFromAPI(CURRENT_WEATHER, "main,temp");
            return getTemperatureInCurrentFormat(kelvinTemp);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get current temperature!");
        }
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
        HttpURLConnection connection = HttpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, FORECAST_WEATHER, this.apiKey));
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
        double ans = jsonObject.getValueByKeyDouble(levelString);

        return getTemperatureInCurrentFormat(ans);
    }

    public double getHighestTemperature(dayOfWeek day) {
        try {
            return getLeveledTemperature(day, "temp_max");
        } catch (Exception e) {
            throw new RuntimeException("Cannot get highest temperature!");
        }
    }

    public double getLowestTemperature(dayOfWeek day) {
        try {
            return getLeveledTemperature(day, "temp_min");
        } catch (Exception e) {
            throw new RuntimeException("Cannot get lowest temperature!");
        }
    }

    public String getGeoCoordinates() {
        // xx.xxxx:yyy.yyyy
        try {
            double latitude = getDoubleFromAPI(FORECAST_WEATHER, "city,coord,lat");
            double longitude = getDoubleFromAPI(FORECAST_WEATHER, "city,coord,lon");
            return String.format(Locale.ROOT, "%07.4f:%08.4f", latitude, longitude);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get geographical coordinates!");
        }
    }
}
