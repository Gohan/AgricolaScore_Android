package apps.baozishan.agricola_score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;

import apps.baozishan.agricola_score.Utils.JsonHelper;
import apps.baozishan.agricola_score.Utils.PlayerScoreInfo;
import apps.baozishan.agricola_score.Utils.ScoreItem;
import apps.baozishan.agricola_score.Utils.ScoreNumberItem;
import apps.baozishan.agricola_score.Utils.ScoreRadioItem;

public class CaculateScoreActivity extends Activity {

    private final static int ID_START = 100;
    private JSONObject oPlayerInfo = null;
    private int nId= 100;
    private int nIndex = 0; // Player Index
    private ArrayList<ScoreItem> arrScoreItem = new ArrayList<ScoreItem>();
    private HashMap<String, Integer> mapFactorTable = new HashMap<String, Integer>();
    private PlayerScoreInfo oPlayerInfoWrapper = null;

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
            oPlayerInfoWrapper = new PlayerScoreInfo(oPlayerInfo);
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

    public void onDestroy() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.caculatescore_relativelayout);
        layout.removeAllViews();
        super.onDestroy();
    }

    private void InitUIComponents() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.caculatescore_relativelayout);
        final EditText txtName = AppendEditTextArea(layout, "你的大名:", oPlayerInfo.optString("name"), null);

        JSONObject jsonItem = JsonHelper.GetJSONObjectFromStream(
                getResources().openRawResource(R.raw.raw_scoretable));

        View afterView = txtName;
        JSONArray array = jsonItem.optJSONArray("controls");
        for (int i = 0; i < array.length(); i++) {
            JSONObject row = array.optJSONObject(i);
            String key = row.optString("key");
            String type = row.optString("type");
            mapFactorTable.put(key, row.optInt("factor", 1));
            if (type.equals("radio")) {
                RadioGroup rg = AppendRadioGroupArea(layout, row, afterView);
                arrScoreItem.add(new ScoreRadioItem(key, rg, row));
                afterView = rg;
            } else if (type.equals("number")) {
                EditText et = AppendEditNumberArea(layout, row, afterView);
                arrScoreItem.add(new ScoreNumberItem(key, et, row));
                afterView = et;
            }
        }
        Button btnConfirm = AppendButtonArea(layout, "确定", afterView);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalScore = 0;
                int nRoomTypeFactor = -1;
                String strRoomType = "";
                for (ScoreItem item:arrScoreItem) {
                    String key = item.GetKey();
                    if (key.equalsIgnoreCase("room_type")) {
                        assert item instanceof ScoreRadioItem;
                        ScoreRadioItem radio = (ScoreRadioItem)item;
                        item.UpdateItem();
                        nRoomTypeFactor = item.GetValue();
                        strRoomType = radio.GetChoiceName();
                        continue;
                    }

                    JSONObject obj = item.GetUserData();
                    Object factor = obj.opt("factor");
                    int nFactor = 1;
                    if (factor instanceof Integer) {
                        nFactor = (Integer)factor;
                    } else if (factor instanceof String ) {
                        assert ((String) factor).equalsIgnoreCase("room_type");
                        assert nRoomTypeFactor >= 0;
                        nFactor = nRoomTypeFactor;
                    }

                    item.SetFactor(nFactor);
                    item.UpdateItem();

                    if (item instanceof ScoreRadioItem){
                        String strValue = ((ScoreRadioItem)item).GetStringValue();
                        if (strValue != null) {
                            // color / room_type 是有StringValue的
                            oPlayerInfoWrapper.PutInfo(key, strValue);
                            continue;
                        }

                        strValue = ((ScoreRadioItem)item).GetChoiceName();
                        int score = item.GetScore();
                        totalScore += score;
                        oPlayerInfoWrapper.PutInfo(key, strValue, score);
                    } else {
                        String description = String.format("%d", item.GetValue());
                        oPlayerInfoWrapper.PutInfo(key,
                                description,
                                item.GetScore());
                        totalScore += item.GetScore();
                    }
                }

                oPlayerInfoWrapper.PutInfo("name", txtName.getText().toString());
                oPlayerInfoWrapper.PutInfo("total_score", totalScore);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("index", nIndex);
                returnIntent.putExtra("result",
                        oPlayerInfoWrapper.GetJsonObject().toString());
                setResult(RESULT_OK, returnIntent);
                Log.v("CalcScore", oPlayerInfoWrapper.GetJsonObject().toString());
                finish();
            }

        });
    }

    private Button AppendButtonArea(RelativeLayout layout, String title, View control) {
        Button btn = new Button(this);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, control.getId());
        lp.setMargins(30, 0, 30, 0);
        btn.setText(title);
        btn.setLayoutParams(lp);
        btn.setId(nId++);
        layout.addView(btn);
        return btn;
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
        edit.setSingleLine(true);
        edit.setText(value);
        edit.setId(nId++);
        edit.setLayoutParams(lp);
        layout.addView(edit);

        return edit;
    }

    private EditText AppendEditNumberArea(RelativeLayout layout, JSONObject jsonObject, View afterView) {
        EditText et = AppendEditTextArea(layout, jsonObject, afterView);
        class LocalListener implements View.OnClickListener {
            public static final int PLUS = 1;
            public static final int MINUS = 2;
            private int m_nEditId = 0;
            private int m_nType = PLUS;

            public LocalListener(int nEditId, int type) {
                m_nEditId = nEditId;
                m_nType = type;
            }

            @Override
            public void onClick(View view) {
                EditText et = (EditText)findViewById(m_nEditId);
                et.requestFocus();
                int newValue = Integer.parseInt(et.getText().toString());
                if (m_nType == PLUS)
                    newValue += 1;
                else
                    newValue -= 1;
                et.setText(Integer.toString(newValue));
            }
        };

        Button minusButton = new Button(this);
        minusButton.setId(nId++);
        minusButton.setText("-");
        minusButton.setOnClickListener(new LocalListener(et.getId(), LocalListener.MINUS));

        Button plusButton = new Button(this);
        plusButton.setId(nId++);
        plusButton.setText("+");
        plusButton.setOnClickListener(new LocalListener(et.getId(), LocalListener.PLUS));

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (afterView != null)
            lp.addRule(RelativeLayout.BELOW, afterView.getId());
        lp.addRule(RelativeLayout.RIGHT_OF, et.getId());
        minusButton.setLayoutParams(lp);
        layout.addView(minusButton);

        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (afterView != null)
            lp.addRule(RelativeLayout.BELOW, afterView.getId());
        lp.addRule(RelativeLayout.RIGHT_OF, minusButton.getId());
        plusButton.setLayoutParams(lp);
        layout.addView(plusButton);
        return et;
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
        edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
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
            item.setTag(jsonItem);
            rg.addView(item);

            int nFactor = jsonObject.optInt("factor", 1);
            // 处理各种score结构中数组情况例如牛[1(个数),1(分数)]
            try {
                JSONArray playerArrayValue = oPlayerInfo.getJSONArray(key);
                if (playerArrayValue.optInt(1, 0) == jsonItem.optInt("score", 0)*nFactor) {
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
        }

        return rg;
    }
}