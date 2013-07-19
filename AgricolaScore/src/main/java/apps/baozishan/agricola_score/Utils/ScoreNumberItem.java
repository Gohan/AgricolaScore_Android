package apps.baozishan.agricola_score.Utils;

import android.widget.EditText;

import org.json.JSONObject;

public class ScoreNumberItem extends ScoreItem{
    private EditText et;

    public ScoreNumberItem(String key, EditText et, JSONObject json) {
        super(key, json);
        this.factor = 1;
        this.et = et;
    }

    @Override
    public void UpdateItem() {
        String text = et.getText().toString();
        if (text != null)
            value = Integer.parseInt(text);
        else
            value = 0;

        score = value * factor;
    }

    @Override
    public void SetFactor(int factor) {
        this.factor = factor;
        score = value * factor;
    }
}
