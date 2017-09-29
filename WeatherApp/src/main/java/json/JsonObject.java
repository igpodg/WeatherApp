package json;

public class JsonObject {
    private String json;

    public static JsonObject getJsonObject(String json) {
        throw new RuntimeException("JSON is invalid!");
    }

    private JsonObject(String json) {
        this.json = json;
    }

    public String getValueByKey(String key) {
        throw new RuntimeException("Cannot get value!");
    }

    public int getValueByKeyInt(String key) {
        throw new RuntimeException("Cannot get int value!");
    }

    public double getValueByKeyDouble(String key) {
        throw new RuntimeException("Cannot get double value!");
    }
}
