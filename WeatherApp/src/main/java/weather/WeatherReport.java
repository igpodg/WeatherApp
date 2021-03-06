package weather;

import general.FileManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Supplier;

public class WeatherReport {
    private WeatherRequest request;
    private WeatherConstants.TemperatureFormat currentFormat;
    private String[] allCities;
    private Supplier<LocalDateTime> dateTimeGetter;
    private FileManager fileManager;

    private String[] getCityFromConsoleOrInput() throws RuntimeException {
        try {
            this.fileManager.setName("input.txt");
            File inputFile = this.fileManager.getFileByName();
            String loadedCities = new FileManager(inputFile).loadContents();
            loadedCities = loadedCities.replace("\r\n", "\n");
            return loadedCities.split("\n");
        } catch (RuntimeException e) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("City not defined, do you want to type its name from console? (y/n): ");
            if (Character.toLowerCase(scanner.next().charAt(0)) == 'y') {
                scanner = new Scanner(System.in);
                System.out.print("Please enter city: ");
                String[] inputCity = new String[1];
                inputCity[0] = scanner.nextLine();
                this.fileManager.writeContents(inputCity[0], true, true);
                return inputCity;
            } else {
                throw new RuntimeException("Cannot get city from console!");
            }
        }
    }

    public WeatherReport(WeatherRequest request, String[] cities,
                         Supplier<LocalDateTime> dateTimeGetter, FileManager fileManager) {
        this.request = request;
        this.currentFormat = WeatherConstants.TemperatureFormat.CELSIUS;
        request.setTemperatureFormat(currentFormat);

        this.dateTimeGetter = dateTimeGetter;
        this.fileManager = (fileManager == null) ? new FileManager("") : fileManager;
        this.allCities = (cities == null || cities.length == 0) ? getCityFromConsoleOrInput() : cities;
    }

    public void setTemperatureFormat(WeatherConstants.TemperatureFormat format) {
        this.currentFormat = format;
        request.setTemperatureFormat(currentFormat);
    }

    private String formatReport(String cityName, String temperatureIndicator) throws RuntimeException {
        LocalDateTime currentDate = this.dateTimeGetter.get();
        request.setCity(cityName);
        try {
            String[] coordinates = request.getGeoCoordinates().split(":");
            coordinates[1] = (coordinates[1].charAt(0) == '0') ? coordinates[1].substring(1) : coordinates[1];
            return String.format("Ilma raport -- %s:\n\t- Linn: %s\n" +
                            "\t- Linna koordinaadid: lat %s lon %s\n" +
                            "\t- Maksimaalne temperatuur:\n\t\t- Homme: %.2f%s\n\t\t- Ülehomme: %.2f%s\n" +
                            "\t\t- Üleülehomme: %.2f%s\n\t- Minimaalne temperatuur:\n\t\t- Homme: %.2f%s\n" +
                            "\t\t- Ülehomme: %.2f%s\n\t\t- Üleülehomme: %.2f%s\n\t- Praegune temperatuur: %.2f%s\n",
                    currentDate.format(DateTimeFormatter.ofPattern("d MMM YYYY HH:mm:ss", Locale.ROOT)),
                    cityName, coordinates[0], coordinates[1],
                    request.getHighestTemperature(WeatherConstants.DayOfWeek.TOMORROW),
                    temperatureIndicator,
                    request.getHighestTemperature(WeatherConstants.DayOfWeek.AFTER_TOMORROW),
                    temperatureIndicator,
                    request.getHighestTemperature(WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW),
                    temperatureIndicator,
                    request.getLowestTemperature(WeatherConstants.DayOfWeek.TOMORROW),
                    temperatureIndicator,
                    request.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_TOMORROW),
                    temperatureIndicator,
                    request.getLowestTemperature(WeatherConstants.DayOfWeek.AFTER_AFTER_TOMORROW),
                    temperatureIndicator,
                    request.getCurrentTemperature(),
                    temperatureIndicator);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot format report for city " + cityName + "!");
        }
    }

    private void saveReport(String cityName, String reportString) throws RuntimeException {
        try {
            this.fileManager.setName(cityName + ".txt");
            this.fileManager.createNewFile();
            this.fileManager.writeContents(reportString.replace(
                    "\n", "\r\n"), false, false);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot save report for city " + cityName + "!");
        }
    }

    public String getReport() throws RuntimeException {
        String temperatureIndicator = (this.currentFormat == WeatherConstants.TemperatureFormat.CELSIUS) ?
                " °C" : (this.currentFormat == WeatherConstants.TemperatureFormat.FAHRENHEIT ? " °F" : "");
        System.out.println("Koostan raportid, palun oodake...");

        String allReports = "";
        for (int i = 0; i < allCities.length; i++) {
            if (allCities[i].isEmpty()) {
                continue;
            }
            try {
                String resultReport = formatReport(allCities[i], temperatureIndicator);
                allReports += "\n" + resultReport;
                saveReport(allCities[i], resultReport);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return allReports;
    }
}
