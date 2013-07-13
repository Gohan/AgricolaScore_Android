package apps.baozishan.agricola_score.Utils;

import android.widget.EditText;

/**
 * Created by gohan on 7/13/13.
 */
public class ScoreNumberItem extends ScoreItem{
    private EditText et;

    public ScoreNumberItem(String key, EditText et) {
        super(key);
        this.et = et;
    }

    @Override
    public void UpdateItem() {
        value = Integer.parseInt(et.getText().toString());
        score = value * factor;
    }

    @Override
    public void SetFactor(int factor) {
        this.factor = factor;
        score = value * factor;
    }
}
