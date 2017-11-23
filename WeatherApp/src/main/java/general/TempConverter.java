package general;

import weather.WeatherConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TempConverter {
    public static double convertToFahrenheit(double kelvinTemp) {
        try {
            return ((kelvinTemp - 273.15) * 9 / 5) + 32;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Fahrenheit!");
        }
    }

    public static double convertToCelsius(double kelvinTemp) {
        try {
            return kelvinTemp - 273.15;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Celsius!");
        }
    }

    public static double getTemperatureInFormat(WeatherConstants.TemperatureFormat format, double kelvinTemp) {
        if (format == WeatherConstants.TemperatureFormat.CELSIUS) {
            return new BigDecimal(convertToCelsius(kelvinTemp))
                    .setScale(10, RoundingMode.HALF_UP).doubleValue();
        } else if (format == WeatherConstants.TemperatureFormat.FAHRENHEIT) {
            return new BigDecimal(convertToFahrenheit(kelvinTemp))
                    .setScale(10, RoundingMode.HALF_UP).doubleValue();
        } else {
            throw new RuntimeException("Unknown temperature format set!");
        }
    }
}
