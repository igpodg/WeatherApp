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
        String latitudeMinus = (this.latitude < 0) ? "-" : "";
        String longitudeMinus = (this.longitude < 0) ? "-" : "";
        return String.format(Locale.ROOT, "%s%07.4f:%s%08.4f",
                latitudeMinus, Math.abs(this.latitude),
                longitudeMinus, Math.abs(this.longitude));
    }
}
