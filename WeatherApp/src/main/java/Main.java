import weather.WeatherConstants;
import weather.WeatherRequest;

public class Main {
    public static void main(String[] args) {
        WeatherRequest request = new WeatherRequest("fbbf4f7271272d04a548efe35b69c3cf",
                WeatherConstants.TemperatureFormat.CELSIUS);
        System.out.println(request.getCurrentTemperature());
    }
}
