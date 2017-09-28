package weather;

public class WeatherAPI {
    private String apiKey;

    public enum dayOfWeek {
        TOMORROW,
        AFTER_TOMORROW,
        AFTER_AFTER_TOMORROW
    }

    public WeatherAPI(String apiKey) {
        this.apiKey = apiKey;
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
