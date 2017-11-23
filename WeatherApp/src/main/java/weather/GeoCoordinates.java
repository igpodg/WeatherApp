package weather;

import java.util.Locale;

public class GeoCoordinates {
    private double latitude;
    private double longitude;

    public GeoCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        // xx.xxxx:yyy.yyyy
        return String.format(Locale.ROOT, "%07.4f:%08.4f",
                this.latitude, this.longitude);
    }
}
