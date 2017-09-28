package json;

public class JsonValidator {
    public static boolean checkJsonCorrect(String json) {
        return json.length() <= 30;
    }

    /*public static String getJsonValue(String json, String key) {
        return null;
    }*/
}
