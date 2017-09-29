package weather;

public class WeatherRequest {
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

    private static double convertToFahrenheit(double celsiusTemp) {
        throw new RuntimeException("Cannot convert to Fahrenheit!");
    }

    private static double convertToCelsius(double fahrenheitTemp) {
        throw new RuntimeException("Cannot convert to Celsius!");
    }

    public void setTemperatureFormat(temperatureFormat format) {
        throw new RuntimeException("Cannot set temperature format!");
    }

    public double getCurrentTemperature() {
        throw new RuntimeException("Cannot get current temperature!");
    }

    public double getHighestTemperature(dayOfWeek day) {
        throw new RuntimeException("Cannot get highest temperature!");
    }

    public double getLowestTemperature(dayOfWeek day) {
        throw new RuntimeException("Cannot get lowest temperature!");
    }

    public String getGeoCoordinates() {
        // xxx:yyy
        return null;
    }
}
