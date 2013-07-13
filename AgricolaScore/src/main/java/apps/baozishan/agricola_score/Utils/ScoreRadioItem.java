package apps.baozishan.agricola_score.Utils;

import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by gohan on 7/12/13.
 */
public class ScoreRadioItem extends ScoreItem{
    private RadioGroup rg;
    public ScoreRadioItem(String key, RadioGroup rg) {
        super(key);
    }

    @Override
    public void UpdateItem() {
        RadioButton rd = (RadioButton)rg.findViewById(rg.getCheckedRadioButtonId());
        if (rd != null && rd.isChecked()) {
            value = (Integer)rd.getTag();
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

    @Override
    public void SetFactor(int factor) {
        this.factor = factor;
        score = value * factor;
    }
}
