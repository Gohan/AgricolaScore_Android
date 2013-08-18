package apps.baozishan.agricola_score.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class PlayerScoreInfo {
    private HashMap<String, Object> mapInfos;
    private JSONObject json;

    public PlayerScoreInfo(JSONObject json) {
        this.json = json;
    }

    public void PutInfo(String key, int value) {
        try {
            Object obj = json.opt(key);
            if (obj instanceof Integer) {
                json.put(key, value);
            } else if (obj instanceof JSONArray) {
                json.put(key, new JSONArray(Arrays.asList(value, value)));
            } else {
                assert false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PutInfo(String key, String choice, int value) {
        Object obj = json.opt(key);
        try {
            if (obj instanceof JSONArray) {
                JSONArray arr = (JSONArray)obj;
                arr.put(0, choice);
                arr.put(1, value);
                json.put(key, arr);
            } else {
                assert false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PutInfo(String key, String value) {
        try {
            Object obj = json.opt(key);
            if (obj instanceof String) {
                json.put(key, value);
            } else {
                assert false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject GetJsonObject() {
        return json;
    }
}
