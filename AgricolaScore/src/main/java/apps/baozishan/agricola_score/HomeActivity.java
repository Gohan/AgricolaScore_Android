package apps.baozishan.agricola_score;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import apps.baozishan.agricola_score.Utils.PlayerItem;

public class HomeActivity extends Activity {
    protected static final int OPEN_CACULATE_SCORE = 0;
    private GameInfoData gameInfoData = new GameInfoData();

    private void InitUIComponents() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.home_rg_playernumber);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View item = findViewById(i);
                if(item != null) {
                    item.performClick();
                    String strPlayerNumber = ((RadioButton) findViewById(i)).getText().toString();
                    ShowMessageBox(strPlayerNumber);
                    int nPlayerNumber = Integer.parseInt(strPlayerNumber);
                    gameInfoData.UpdatePlayerNumber(nPlayerNumber);
                    SetListViewByPlayerNumber(Integer.parseInt(strPlayerNumber));
                    SavePlayerInfo();
                }
            }
        });

        ListView lv = (ListView)findViewById(R.id.home_lv_players);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private ListView lv = null;
            private Context context = null;

            public AdapterView.OnItemClickListener init(ListView lv, Context context) {
                this.lv = lv;
                this.context = context;
                return this;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // int index = lv.getPositionForView(view);
                Intent intent = new Intent(context, CaculateScoreActivity.class);
                intent.putExtra("index", i);
                intent.putExtra("player_info", gameInfoData.getPlayerInfoJsonString(i));
                startActivityForResult(intent, OPEN_CACULATE_SCORE);
            }
        }.init(lv, this));

        Dictionary<Integer, Integer> mapRadio = new Hashtable<Integer, Integer>();
        mapRadio.put(1, R.id.home_radioButton1);
        mapRadio.put(2, R.id.home_radioButton2);
        mapRadio.put(3, R.id.home_radioButton3);
        mapRadio.put(4, R.id.home_radioButton4);
        mapRadio.put(5, R.id.home_radioButton5);
        int number = 0;
        number = gameInfoData.getPlayerNumber();
        RadioButton rb = (RadioButton) findViewById(mapRadio.get(number));
        if (rb != null) {
            rb.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameInfoData.AttachContext(this);
        gameInfoData.RebuildGameInfo();
        gameInfoData.Save(this);
        gameInfoData.Load();
        setContentView(R.layout.activity_home);
        InitUIComponents();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("LastGameInfo", gameInfoData.GetJsonString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameInfoData.AttachContext(this);
        if (savedInstanceState != null) {
            gameInfoData.LoadFromJsonString(savedInstanceState.getString("LastGameInfo"));
        } else {
            gameInfoData.Load();
        }
    }

    private void SavePlayerInfo() {
        gameInfoData.Save(this);
    }

    // String s = String.format("{\n        \"name\":\"Player%%%%d\",\n        \"color\":\"%%%%s\",\n        \"room_type\":\"Wood\",\n        \"fields\":[\n        0,\n        -1\n        ],\n        \"pastures\":[\n        0,\n        -1\n        ],\n        \"grain\":[\n        0,\n        -1\n        ],\n        \"vegetables\":[\n        0,\n        -1\n        ],\n        \"sheep\":[\n        0,\n        -1\n        ],\n        \"boar\":[\n        0,\n        -1\n        ],\n        \"cattle\":[\n        0,\n        -1\n        ],\n        \"unused_space\":[\n        0,\n        0\n        ],\n        \"fenced_stables\":[\n        0,\n        0\n        ],\n        \"rooms\":[\n        5,\n        0\n        ],\n        \"family_members\":[\n        2,\n        6\n        ],\n        \"victory_points\":0,\n        \"bonus_points\":0,\n        \"total_score\":-1\n        }")

    private void InitPlayerInfo() {
    }
    private JSONObject CreatePlayerJsonObject(int i, String s) {
        String strInitInfo = String.format(getString(R.string.home_static_jsonstring), i, s);
        JSONObject ret = null;
        try {
            ret = new JSONObject(strInitInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
        return ret;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == OPEN_CACULATE_SCORE) {

            if(resultCode == RESULT_OK){
                JSONObject result = null;
                int nPlayerIndex = data.getIntExtra("index", 0);
                try {
                    result = new JSONObject(data.getStringExtra("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result != null) {
                    gameInfoData.UpdatePlayerInfo(nPlayerIndex, result);
                    gameInfoData.Save(this);
                    SetListViewByPlayerNumber(gameInfoData.getPlayerNumber());
                }

            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }//onActivityResult

    private void ShowMessageBox(String choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Your choose:")
                .setMessage("Are you sure?" + choose)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static int getItemHeightofListView(ListView listView, int items) {

        ListAdapter mAdapter = listView.getAdapter();

        int listviewElementsHeight = 0;
        // for listview total item height
        // items = mAdapter.getCount();


        for (int i = 0; i < items; i++) {

            View childView = mAdapter.getView(i, null, listView);
            childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            listviewElementsHeight+= childView.getMeasuredHeight();
        }

        return listviewElementsHeight;
    }

    private void SetListViewByPlayerNumber(int i) {
        ListView lv = (ListView)findViewById(R.id.home_lv_players);

        ArrayList<PlayerItem> myList = gameInfoData.getPlayerItemList();
        lv.setAdapter(new PlayerItemAdapter(this, R.layout.listviewitem_playerinfo, myList));
        int height = getItemHeightofListView(lv, myList.size());
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.height = height;
        lv.setLayoutParams(lp);
    }
}

//"\"name\":\"Player%1$d\",\n        \"color\":\"%2$s\",\n        \"room_type\":\"Wood\",\n        \"fields\":[\n        0,\n        -1\n        ],\n        \"pastures\":[\n        0,\n        -1\n        ],\n        \"grain\":[\n        0,\n        -1\n        ],\n        \"vegetables\":[\n        0,\n        -1\n        ],\n        \"sheep\":[\n        0,\n        -1\n        ],\n        \"boar\":[\n        0,\n        -1\n        ],\n        \"cattle\":[\n        0,\n        -1\n        ],\n        \"unused_space\":[\n        0,\n        0\n        ],\n        \"fenced_stables\":[\n        0,\n        0\n        ],\n        \"rooms\":[\n        5,\n        0\n        ],\n        \"family_members\":[\n        2,\n        6\n        ],\n        \"victory_points\":0,\n        \"bonus_points\":0,\n        \"total_score\":-1"

