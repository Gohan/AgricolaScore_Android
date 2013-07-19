package apps.baozishan.agricola_score.Utils;

import org.json.JSONObject;

public abstract class ScoreItem {
    protected String key;
    protected int factor = 1;
    protected int score = 0;
    protected int value = 0;
    protected JSONObject json = null;

    public ScoreItem(String key, JSONObject json) {
        this.key = key;
        this.json = json;
    }

    public abstract void UpdateItem();
    public abstract void SetFactor(int factor);
    public JSONObject GetUserData() {
        return json;
    }
    public String GetKey() {
        return key;
    }
    public int GetScore() {
        return score;
    }
    public int GetValue() {
        return value;
    }
}
