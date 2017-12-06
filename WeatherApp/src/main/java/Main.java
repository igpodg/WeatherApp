import weather.WeatherConstants;
import weather.WeatherReport;
import weather.WeatherRequest;

public class Main {
    public static void main(String[] args) {
        /*WeatherRequest request = new WeatherRequest("fbbf4f7271272d04a548efe35b69c3cf",
                WeatherConstants.TemperatureFormat.CELSIUS);
        System.out.println(request.getCurrentTemperature());*/
        String[] cities = {"Tallinn", "Tartu", "Paide"};
        WeatherReport report = new WeatherReport(new WeatherRequest("fbbf4f7271272d04a548efe35b69c3cf",
                WeatherConstants.TemperatureFormat.CELSIUS), cities);
        report.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
        report.printAndSaveReport();
    }
}
