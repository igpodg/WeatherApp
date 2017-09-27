package weather;

import http.utility.HttpUtility;

public class WeatherAPI {
    private HttpUtility httpUtil;

    public enum dayOfWeek {
        TOMORROW,
        AFTER_TOMORROW,
        AFTER_AFTER_TOMORROW
    }

    public WeatherAPI() {
        httpUtil = new HttpUtility();
    }

    public double getCurrentTemperature() {
        return 0;
    }

    public double getHighestTemperature(dayOfWeek day) {
        return 1.5;
    }

    public double getLowestTemperature(dayOfWeek day) {
        return -1.5;
    }

    public String getGeoCoordinates() {
        // xxx:yyy
        return null;
    }
}
