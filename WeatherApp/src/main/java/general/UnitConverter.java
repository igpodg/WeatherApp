package general;

public class UnitConverter {
    public static double convertTempToFahrenheit(double kelvinTemp) {
        try {
            return ((kelvinTemp - 273.15) * 9 / 5) + 32;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Fahrenheit!");
        }
    }

    public static double convertTempToCelsius(double kelvinTemp) {
        try {
            return kelvinTemp - 273.15;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert to Celsius!");
        }
    }
}
