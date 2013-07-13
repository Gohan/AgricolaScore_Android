package apps.baozishan.agricola_score.Utils;

import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by gohan on 7/13/13.
 */
public abstract class ScoreItem {
    protected String key;
    protected int factor;
    protected int score;
    protected int value;

    public ScoreItem(String key) {
        this.key = key;
    }

    public abstract void UpdateItem();
    public abstract void SetFactor(int factor);
    public String GetKey() {
        return key;
    }
}
