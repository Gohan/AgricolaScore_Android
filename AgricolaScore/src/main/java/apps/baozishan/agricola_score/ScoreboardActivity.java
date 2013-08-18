package apps.baozishan.agricola_score;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import apps.baozishan.agricola_score.Utils.JsonHelper;
import apps.baozishan.agricola_score.Views.ScoreBoardSurfaceView;
import apps.baozishan.agricola_score.Views.ScoreboardView;

public class ScoreboardActivity extends Activity {
    public ScoreboardView view = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showscore);

        String strPlayerInfo = getIntent().getStringExtra("game_info");
        Log.v("Score", strPlayerInfo);
        ScoreBoardSurfaceView view = (ScoreBoardSurfaceView)findViewById(R.id.surfaceView);
        try {
            view.SetGameInfoObject(new JSONObject(strPlayerInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view.setLayoutParams(new RelativeLayout.LayoutParams(500, 500));
        view.Draw(null);
        //getIntent().get;
        //getPlayerScoreInfoMap
    }
}