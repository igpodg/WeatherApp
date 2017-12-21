package weather;

import general.TempConverter;
import http.HttpUtility;
import json.JsonObject;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherRequest {
    private final static String BASE_URL_FORMAT = "http://api.openweathermap.org/data/2.5/%s?q=%s,EE&appid=%s";
    private final static String CURRENT_WEATHER = "weather";
    private final static String FORECAST_WEATHER = "forecast";

    private final static String API_LOWEST_TEMP = "temp_min";
    private final static String API_HIGHEST_TEMP = "temp_max";

    private String apiKey;
    private WeatherConstants.TemperatureFormat format;
    private String currentCity;

    private HttpUtility httpUtility;

    private boolean isCityDefined() {
        return !(this.currentCity == null || this.currentCity.isEmpty());
    }

    private void ensureCityIsDefined() throws RuntimeException {
        if (!isCityDefined()) {
            throw new RuntimeException("City not defined!");
        }
    }

    public WeatherRequest(HttpUtility httpUtility, String apiKey, WeatherConstants.TemperatureFormat defaultFormat) {
        this.httpUtility = httpUtility;
        this.apiKey = apiKey;
        this.format = defaultFormat;
        this.currentCity = null;
    }

    public void setCity(String city) {
        this.currentCity = city;
    }

    public String getCity() {
        ensureCityIsDefined();
        return this.currentCity;
    }

    public void setTemperatureFormat(WeatherConstants.TemperatureFormat format) throws RuntimeException {
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

        HttpURLConnection connection = this.httpUtility.makeUrlConnection(
                String.format(BASE_URL_FORMAT, weatherString, this.currentCity, this.apiKey));
        this.httpUtility.makeGetRequest(connection);

        String jsonString = this.httpUtility.putDataToString(connection);
        this.httpUtility.closeUrlConnection(connection);
        System.out.println("jsonString: " + jsonString);
        JsonObject jsonObject = JsonObject.getJsonObject(jsonString);
        if (useDate) {
            int iterator = 0;
            String searchResult;
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

    public double getCurrentTemperature() throws RuntimeException {
        ensureCityIsDefined();
        double kelvinTemp = getDoubleFromAPI(CURRENT_WEATHER, "main,temp", false, null);
        if (Double.isNaN(kelvinTemp)) {
            throw new RuntimeException("Cannot get current temperature!");
        }
        return getTemperatureInCurrentFormat(kelvinTemp);
    }

    private double getLeveledTemperature(WeatherConstants.DayOfWeek day, WeatherConstants.TemperatureLevel level)
            throws RuntimeException {
        ensureCityIsDefined();
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

    public double getHighestTemperature(WeatherConstants.DayOfWeek day) throws RuntimeException {
        try {
            return getLeveledTemperature(day, WeatherConstants.TemperatureLevel.HIGHEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get highest temperature!");
        }
    }

    public double getLowestTemperature(WeatherConstants.DayOfWeek day) throws RuntimeException {
        try {
            return getLeveledTemperature(day, WeatherConstants.TemperatureLevel.LOWEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get lowest temperature!");
        }
    }

    public String getGeoCoordinates() throws RuntimeException {
        ensureCityIsDefined();
        try {
            double latitude = getDoubleFromAPI(
                    FORECAST_WEATHER, "city,coord,lat", false, null);
            double longitude = getDoubleFromAPI(
                    FORECAST_WEATHER, "city,coord,lon", false, null);
            return new GeoCoordinates(latitude, longitude).toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot get geographical coordinates!");
        }
    }
}
