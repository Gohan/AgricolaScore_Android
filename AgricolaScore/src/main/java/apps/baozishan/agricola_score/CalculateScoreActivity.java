package apps.baozishan.agricola_score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by gohan on 7/12/13.
 */
public class CalculateScoreActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("new_variable_name");
        }
    }
}