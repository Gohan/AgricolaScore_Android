package apps.baozishan.agricola_score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apps.baozishan.agricola_score.Utils.JsonHelper;
import apps.baozishan.agricola_score.Utils.ScoreItem;
import apps.baozishan.agricola_score.Utils.ScoreNumberItem;
import apps.baozishan.agricola_score.Utils.ScoreRadioItem;

public class CaculateScoreActivity extends Activity {

    private final static int ID_START = 100;
    private JSONObject oPlayerInfo = null;
    private int nId= 100;
    private int nIndex = 0; // Player Index
    private ArrayList<ScoreItem> arrScoreItem = new ArrayList<ScoreItem>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caculatescore);

        String strPlayerInfo = getIntent().getStringExtra("player_info");
        nIndex = getIntent().getIntExtra("index", 0);

        if (strPlayerInfo == null) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
            return;
        }

        try {
            oPlayerInfo = new JSONObject(strPlayerInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (oPlayerInfo == null) {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
            return;
        }

        nId = ID_START;
        InitUIComponents();

    }

    private void InitUIComponents() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.caculatescore_relativelayout);
        EditText txtName = AppendEditTextArea(layout, "你的大名:", oPlayerInfo.optString("name"), null);

        JSONObject jsonItem = JsonHelper.GetJSONObjectFromStream(
                getResources().openRawResource(R.raw.raw_scoretable));

        View afterView = txtName;
        JSONArray array = jsonItem.optJSONArray("controls");
        for (int i = 0; i < array.length(); i++) {
            JSONObject row = array.optJSONObject(i);
            String key = row.optString("key");
            String type = row.optString("type");
            if (type.equals("radio")) {
                RadioGroup rg = AppendRadioGroupArea(layout, row, afterView);
                arrScoreItem.add(new ScoreRadioItem(key, rg));
                afterView = rg;
            } else if (type.equals("number")) {
                EditText et = AppendEditTextArea(layout, row, afterView);
                arrScoreItem.add(new ScoreNumberItem(key, et));
                afterView = et;
            }
        }
    }

    private EditText AppendEditTextArea(RelativeLayout layout, String title, String value, View control) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (control != null)
            lp.addRule(RelativeLayout.BELOW, control.getId());
        TextView tv = new TextView(this);
        tv.setId(nId++);
        tv.setText(title);
        tv.setLayoutParams(lp);
        layout.addView(tv);

        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, tv.getId());
        EditText edit = new EditText(this);
        edit.setText(value);
        edit.setId(nId++);
        edit.setLayoutParams(lp);
        layout.addView(edit);

        return edit;
    }

    private EditText AppendEditTextArea(RelativeLayout layout, JSONObject jsonObject, View afterView) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (afterView != null)
            lp.addRule(RelativeLayout.BELOW, afterView.getId());

        String name = jsonObject.optString("name");
        String key = jsonObject.optString("key");
        int value = 0;
        Object valueOjbect = oPlayerInfo.opt(key);
        if (valueOjbect instanceof Integer) {
            value = (Integer)valueOjbect;
        } else if (valueOjbect instanceof JSONArray) {
            value = ((JSONArray) valueOjbect).optInt(0);
        }

        TextView tv = new TextView(this);
        tv.setId(nId++);
        tv.setText(name);
        tv.setLayoutParams(lp);
        layout.addView(tv);

        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, tv.getId());
        EditText edit = new EditText(this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit.setText(Integer.toString(value));
        edit.setId(nId++);
        edit.setLayoutParams(lp);

        Object factor = jsonObject.opt("factor");
        if (factor != null) {
            edit.setTag(factor);
        }
        layout.addView(edit);
        return edit;
    }

    private RadioGroup AppendRadioGroupArea(RelativeLayout layout, JSONObject jsonObject, View afterView) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (afterView != null)
            lp.addRule(RelativeLayout.BELOW, afterView.getId());
        TextView tv = new TextView(this);
        tv.setId(nId++);
        tv.setText(jsonObject.optString("name", "没配置!"));
        tv.setLayoutParams(lp);
        layout.addView(tv);

        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, tv.getId());
        RadioGroup rg = new RadioGroup(this);
        rg.setId(nId++);
        rg.setLayoutParams(lp);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        layout.addView(rg);

        String key = jsonObject.optString("key", "");
        JSONArray array = jsonObject.optJSONArray("options");
        for (int i = 0; i < array.length(); i++) {
            RadioButton item = new RadioButton(this);
            JSONObject jsonItem = array.optJSONObject(i);

            item.setText(jsonItem.optString("name"));
            item.setTextColor(JsonHelper.GetColorFromString(jsonItem.optString("color", "black")));
            item.setTag(Integer.valueOf(jsonItem.optInt("score", 0)));
            rg.addView(item);

            // 处理各种score结构中数组情况例如牛[1(个数),1(分数)]
            try {
                JSONArray playerArrayValue = oPlayerInfo.getJSONArray(key);
                if (playerArrayValue.optInt(1, 0) == jsonItem.optInt("score", 0)) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
                continue;
            } catch (JSONException e) {}

            // 处理房屋类型, 处理颜色
            try {
                String playerStringValue = oPlayerInfo.getString(key);
                if (playerStringValue.equalsIgnoreCase(jsonItem.optString("key", ""))) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
                continue;
            } catch (JSONException e) {}

            // 处理其他数据, 只保存分数那种
            try {
                int playerIntValue = oPlayerInfo.getInt(key);
                if (playerIntValue == jsonItem.optInt("score", 0)) {
                    item.setChecked(true);
                } else {
                    item.setChecked(false);
                }
            } catch (JSONException e) {}
        }

        return rg;
    }
}