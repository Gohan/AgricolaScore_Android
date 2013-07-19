package apps.baozishan.agricola_score;

import android.app.Activity;
import android.os.Bundle;

import apps.baozishan.agricola_score.Views.ScoreboardView;

public class ScoreboardActivity extends Activity {
    public ScoreboardView view = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ScoreboardView(this);
        setContentView(view);

        String strPlayerInfo = getIntent().getStringExtra("scoreboard");
    }
}