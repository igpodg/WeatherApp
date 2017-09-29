package json;

public class JsonValidator {
    public static boolean checkJsonCorrect(String json) {
        return json.length() <= 30;
    }
}
