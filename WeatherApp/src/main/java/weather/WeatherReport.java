package weather;

import general.FileManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherReport {
    private WeatherRequest request;
    private WeatherConstants.TemperatureFormat currentFormat;
    private String[] allCities;

    private String[] getCityFromConsoleOrInput() {
        try {
            File inputFile = FileManager.getFileByName("input.txt");
            String loadedCities = FileManager.loadContents(inputFile);
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
                FileManager.writeContents("input.txt", inputCity[0], true, true);
                return inputCity;
            } else {
                throw new RuntimeException("Cannot get city from console!");
            }
        }
    }

    public WeatherReport(WeatherRequest request, String[] cities) {
        this.request = request;
        this.currentFormat = WeatherConstants.TemperatureFormat.CELSIUS;
        request.setTemperatureFormat(currentFormat);
        this.allCities = (cities == null || cities.length == 0) ? getCityFromConsoleOrInput() : cities;
    }

    public void setTemperatureFormat(WeatherConstants.TemperatureFormat format) {
        this.currentFormat = format;
        request.setTemperatureFormat(currentFormat);
    }

    public void printAndSaveReport() {
        String temperatureIndicator = (this.currentFormat == WeatherConstants.TemperatureFormat.CELSIUS) ?
                " °C" : (this.currentFormat == WeatherConstants.TemperatureFormat.FAHRENHEIT ? " °F" : "");
        System.out.println("Koostan raportid, palun oodake...");

        for (int i = 0; i < allCities.length; i++) {
            LocalDateTime currentDate = LocalDateTime.now();
            String cityName = allCities[i];
            request.setCity(cityName);
            try {
                String[] coordinates = request.getGeoCoordinates().split(":");
                coordinates[1] = (coordinates[1].charAt(0) == '0') ? coordinates[1].substring(1) : coordinates[1];
                String resultReport = String.format("Ilma raport -- %s:\n\t- Linn: %s\n" +
                                "\t- Linna koordinaadid: lat %s lon %s\n" +
                                "\t- Maksimaalne temperatuur:\n\t\t- Homme: %.2f%s\n\t\t- Ülehomme: %.2f%s\n" +
                                "\t\t- Üleülehomme: %.2f%s\n\t- Minimaalne temperatuur:\n\t\t- Homme: %.2f%s\n" +
                                "\t\t- Ülehomme: %.2f%s\n\t\t- Üleülehomme: %.2f%s\n\t- Praegune temperatuur: %.2f%s\n",
                        currentDate.format(DateTimeFormatter.ofPattern("d MMM YYYY HH:mm:ss")),
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
                System.out.println(resultReport);
                FileManager.createNewFile(cityName + ".txt");
                FileManager.writeContents(cityName + ".txt",
                        resultReport.replace("\n", "\r\n"), false, false);
            } catch (RuntimeException e) {
                System.out.println("Rapordi koostamise ajal oli viga: " + e.getMessage());
            }
        }
    }
}