package apps.baozishan.agricola_score;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import apps.baozishan.agricola_score.Utils.PlayerItem;

public class HomeActivity extends Activity {

    private JSONObject gameInfo = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LoadPlayerInfoOnStart(savedInstanceState);

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
                    SetListViewByPlayerNumber(Integer.parseInt(strPlayerNumber));
                    try {
                        gameInfo.put("PlayerNumber", nPlayerNumber);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SavePlayerInfo();
                }
            }
        });
        Dictionary<Integer, Integer> mapRadio = new Hashtable<Integer, Integer>();
        mapRadio.put(1, R.id.home_radioButton1);
        mapRadio.put(2, R.id.home_radioButton2);
        mapRadio.put(3, R.id.home_radioButton3);
        mapRadio.put(4, R.id.home_radioButton4);
        mapRadio.put(5, R.id.home_radioButton5);
        int number = 0;
        try {
            number = gameInfo.getInt("PlayerNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RadioButton rb = (RadioButton) findViewById(mapRadio.get(number));
        if (rb != null) {
            rb.setChecked(true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("LastGameInfo", gameInfo.toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LoadPlayerInfoOnStart(savedInstanceState);
    }

    private void SavePlayerInfo() {
        PrintStream outputStream = null;
        try {
            outputStream = new PrintStream(openFileOutput("Save.json", MODE_PRIVATE));
            outputStream.print(gameInfo.toString());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadPlayerInfoOnStart(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String strGameInfoJson = savedInstanceState.getString("LastGameInfo");
            try {
                gameInfo = new JSONObject(strGameInfoJson);
            } catch (JSONException e) {
                InitPlayerInfo();
                return;
            }
        }

        FileInputStream stream = null;
        try {
            stream = openFileInput("Save.json");
        } catch (FileNotFoundException e) {
            InitPlayerInfo();
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            gameInfo = new JSONObject(sb.toString());
        } catch (JSONException e) {
            InitPlayerInfo();
        }


    }
    // String s = String.format("{\n        \"name\":\"Player%%%%d\",\n        \"color\":\"%%%%s\",\n        \"room_type\":\"Wood\",\n        \"fields\":[\n        0,\n        -1\n        ],\n        \"pastures\":[\n        0,\n        -1\n        ],\n        \"grain\":[\n        0,\n        -1\n        ],\n        \"vegetables\":[\n        0,\n        -1\n        ],\n        \"sheep\":[\n        0,\n        -1\n        ],\n        \"boar\":[\n        0,\n        -1\n        ],\n        \"cattle\":[\n        0,\n        -1\n        ],\n        \"unused_space\":[\n        0,\n        0\n        ],\n        \"fenced_stables\":[\n        0,\n        0\n        ],\n        \"rooms\":[\n        5,\n        0\n        ],\n        \"family_members\":[\n        2,\n        6\n        ],\n        \"victory_points\":0,\n        \"bonus_points\":0,\n        \"total_score\":-1\n        }")

    private void InitPlayerInfo() {
        String color[] = new String[] {"red", "blue", "yellow", "purple", "white"};
        gameInfo = new JSONObject();
        JSONArray players = new JSONArray();
        for (int i = 0; i < 5; i++) {
            players.put(CreatePlayerJsonObject(i, color[i]));
        }
        try {
            gameInfo.put("PlayerNumber", players.length());
            gameInfo.put("Player", players);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
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
        ListView lv = (ListView)findViewById(R.id.listView);
        ArrayList<PlayerItem> myList = new ArrayList<PlayerItem>();
        myList.add(new PlayerItem("Player 1", 10));
        myList.add(new PlayerItem("Player 2", 20));
        myList.add(new PlayerItem("Player 3", 30));
        lv.setAdapter(new PlayerItemAdapter(this, R.layout.listviewitem_playerinfo, myList));
        int height = getItemHeightofListView(lv, myList.size());
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.height = height;
        lv.setLayoutParams(lp);
    }
}
