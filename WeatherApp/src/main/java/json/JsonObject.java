package json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonObject {
    private String json;

    public static JsonObject getJsonObject(String json) {
        if (JsonValidator.checkJsonCorrect(json)) {
            return new JsonObject(json);
        } else {
            throw new RuntimeException("JSON string invalid!");
        }
    }

    private JsonObject(String json) {
        this.json = json;
    }

    public String[] getValuesByKey(String key) {
        String[] allValues;
        if (key.contains(",")) {
            allValues = key.split(",");
        } else {
            allValues = new String[1];
            allValues[0] = key;
        }

        try {
            String temporaryJson = this.json;
            for (int i = 0; i < allValues.length; i++) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(temporaryJson);
                temporaryJson = jsonObj.get(allValues[i]).toString();
                //System.out.println("temporaryJson: " + temporaryJson);

                if (temporaryJson.charAt(0) == '[' && i != (allValues.length - 1)) {
                    JSONArray jsonArray = (JSONArray) jsonParser.parse(temporaryJson);
                    String[] valueArray = new String[jsonArray.size()];
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONObject secondJsonObj = (JSONObject) jsonParser.parse(jsonArray.get(j).toString());
                        valueArray[j] = secondJsonObj.get(allValues[i + 1]).toString();
                    }
                    return valueArray;
                }
            }
            String[] arrayToReturn = new String[1];
            arrayToReturn[0] = temporaryJson;
            return arrayToReturn;
        } catch (ParseException e1) {
            throw new RuntimeException("Cannot get value!");
        } catch (NullPointerException e2) {
            throw new RuntimeException("Value does not exist!");
        }
    }

    public String getValueByKey(String key) {
        return getValuesByKey(key)[0];
    }

    public String getValueByKey(String key, int occurence) {
        try {
            return getValuesByKey(key)[occurence];
        } catch (Exception e) {
            throw new RuntimeException("Array index does not exist!");
        }
    }

    public int getValueByKeyInt(String key) {
        try {
            return Integer.parseInt(getValueByKey(key));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot get int value!");
        }
    }

    public double getValueByKeyDouble(String key) {
        try {
            return Double.parseDouble(getValueByKey(key));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot get double value!");
        }
    }
}
