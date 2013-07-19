package apps.baozishan.agricola_score.Utils;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

public class ScoreRadioItem extends ScoreItem{
    private String choice = null;
    private String stringValue = null;
    private RadioGroup rg;
    public ScoreRadioItem(String key, RadioGroup rg, JSONObject json) {
        super(key, json);
        this.rg = rg;
        factor = 1;
    }

    @Override
    public void UpdateItem() {
        RadioButton rd = (RadioButton)rg.findViewById(rg.getCheckedRadioButtonId());
        if (rd != null && rd.isChecked()) {
            JSONObject item = (JSONObject)rd.getTag();
            value = item.optInt("score", 0);
            choice = item.optString("name", null);
            stringValue = item.optString("key", null);

            score = value * factor;
        }

//        int nCount = rg.getChildCount();
//        for (int i = 0; i < nCount; i++) {
//            RadioButton rd = (RadioButton)rg.getChildAt(i);
//            if (rd != null && rd.isChecked()) {
//                value = (Integer)rd.getTag();
//                score = value * factor;
//                break;
//            }
//        }
    }

    public String GetStringValue() {
        return stringValue;
    }

    public String GetChoiceName() {
        return choice;
    }

    @Override
    public void SetFactor(int factor) {
        this.factor = factor;
        score = value * factor;
    }
}
