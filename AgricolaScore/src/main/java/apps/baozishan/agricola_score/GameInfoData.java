package apps.baozishan.agricola_score;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import apps.baozishan.agricola_score.Utils.JsonHelper;
import apps.baozishan.agricola_score.Utils.PlayerItem;
import apps.baozishan.agricola_score.Utils.ScoreNumberItem;
import apps.baozishan.agricola_score.Utils.ScoreRadioItem;

/**
 * Created by gohan on 7/12/13.
 */
public class GameInfoData {

    private JSONObject gameInfoData = new JSONObject();
    private Context context = null;
    private ArrayList<PlayerItem> playerItemList;

    GameInfoData() {
    }

    public void AttachContext(Context context) {
        this.context = context;
    }

    public void RebuildGameInfo() {
        String color[] = new String[] {"red", "blue", "yellow", "purple", "white"};
        gameInfoData = new JSONObject();
        JSONArray players = new JSONArray();
        for (int i = 0; i < 5; i++) {
            players.put(CreatePlayerJsonObject(i, color[i]));
        }
        try {
            gameInfoData.put("PlayerNumber", players.length());
            gameInfoData.put("Player", players);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private JSONObject CreatePlayerJsonObject(int i, String s) {
        String strInitInfo = context.getString(R.string.home_static_jsonstring, i, s);
        JSONObject ret = null;
        try {
            ret = new JSONObject(strInitInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
        return ret;
    }

    public JSONObject getGameInfo() {
        return gameInfoData;
    }

    public void UpdatePlayerNumber(int nPlayerNumber) {
        try {
            gameInfoData.put("PlayerNumber", nPlayerNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Save(Context homeActivity) {
        PrintStream outputStream = null;
        try {
            outputStream = new PrintStream(homeActivity.openFileOutput("Save.json", homeActivity.MODE_PRIVATE));
            outputStream.print(gameInfoData.toString());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load() {
        FileInputStream stream = null;
        try {
            stream = context.openFileInput("Save.json");
        } catch (FileNotFoundException e) {
            RebuildGameInfo();
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
            gameInfoData = new JSONObject(sb.toString());
        } catch (JSONException e) {
            RebuildGameInfo();
        }
    }

    public void LoadFromJsonString(String json) {
        try {
            gameInfoData = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            RebuildGameInfo();
        }
    }

    public String GetJsonString() {
        return gameInfoData.toString();
    }

    public int getPlayerNumber() {
        int ret = 0;
        try {
            ret = gameInfoData.getInt("PlayerNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void setPlayerNumber(int num) {
        try {
            gameInfoData.put("PlayerNumber", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PlayerItem> getPlayerItemList() {
        ArrayList<PlayerItem> itemList = new ArrayList<PlayerItem>();
        for (int i = 0; i < getPlayerNumber(); i++) {
            itemList.add(new PlayerItem(getPlayerName(i), getPlayerColor(i), getPlayerScore(i)));
        }
        return itemList;
    }

    private int getPlayerScore(int i) {
        int str = 0;
        try {
            str = gameInfoData.getJSONArray("Player").getJSONObject(i).getInt("total_score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getPlayerColor(int i) {
        String str = "";
        try {
            str = gameInfoData.getJSONArray("Player").getJSONObject(i).getString("color");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getPlayerName(int i) {
        String str = "";
        try {
            str = gameInfoData.getJSONArray("Player").getJSONObject(i).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void UpdatePlayerInfo(int nPlayerIndex, JSONObject result) {
        try {
            gameInfoData.getJSONArray("Player").put(nPlayerIndex, result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerInfoJsonString(int index) {
        String ret = null;
        try {
            ret = gameInfoData.getJSONArray("Player").getJSONObject(index).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getPlayerInfoHumanReadString(Context context, int index) {
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject playerInfo = gameInfoData.getJSONArray("Player").getJSONObject(index);

            JSONObject jsonItem = JsonHelper.GetJSONObjectFromStream(
                    context.getResources().openRawResource(R.raw.raw_scoretable));

            sb.append(String.format("总分: %d\n", playerInfo.optInt("total_score")));
            JSONArray array = jsonItem.optJSONArray("controls");
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.optJSONObject(i);
                String key = row.optString("key");
                String name = row.optString("name");
                Object obj = playerInfo.opt(key);
                if (obj instanceof JSONArray) {
                    JSONArray value = (JSONArray)obj;
                    sb.append(String.format("%s: %s, %d\n", name, value.optString(0), value.optInt(1)));
                } else if (obj instanceof String) {
                    sb.append(String.format("%s: %s\n", name, (String)obj));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
