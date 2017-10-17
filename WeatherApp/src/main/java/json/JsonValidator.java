package json;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonValidator {
    public static boolean checkJsonCorrect(String json) {
        try {
            String parsedJson = new JSONParser().parse(json).toString();
            return (parsedJson.length() == json.length());
        } catch (ParseException e) {
            return false;
        }
    }
}
