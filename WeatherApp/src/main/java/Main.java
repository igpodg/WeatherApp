import general.FileManager;
import http.HttpUtility;
import weather.WeatherConstants;
import weather.WeatherReport;
import weather.WeatherRequest;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        /*WeatherRequest request = new WeatherRequest("fbbf4f7271272d04a548efe35b69c3cf",
                WeatherConstants.TemperatureFormat.CELSIUS);
        System.out.println(request.getCurrentTemperature());*/
        String[] cities = {"Tallinn", "Tartu", "Paide"};
        //String[] cities = null;
        WeatherReport report = new WeatherReport(new WeatherRequest(new HttpUtility(),
                "fbbf4f7271272d04a548efe35b69c3cf",
                WeatherConstants.TemperatureFormat.CELSIUS), cities, LocalDateTime::now, null);
        report.setTemperatureFormat(WeatherConstants.TemperatureFormat.FAHRENHEIT);
        String readyReport = report.getReport();
        System.out.println(readyReport);
    }
}
