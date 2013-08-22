package apps.baozishan.agricola_score;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import apps.baozishan.agricola_score.Views.ScoreBoardSurfaceView;

public class ScoreboardActivity extends Activity {
    public ScoreBoardSurfaceView scoreBoardView = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showscore);

        String strPlayerInfo = getIntent().getStringExtra("game_info");
        Log.v("Score", strPlayerInfo);
        scoreBoardView = (ScoreBoardSurfaceView)findViewById(R.id.surfaceView);
        try {
            scoreBoardView.SetGameInfoObject(new JSONObject(strPlayerInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scoreBoardView.setLayoutParams(new RelativeLayout.LayoutParams(500, 500));
        scoreBoardView.Draw();

        Button btnShare = (Button)findViewById(R.id.score_btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rect rect = new Rect();
                scoreBoardView.getDrawingRect(rect);
                Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                scoreBoardView.DrawToCanvas(canvas);

                String strStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
                // /mnt/sdcard/Android/data/apps.baozishan.agricola_score/cache
                File directory = new File(strStorageDirectory + "/Android/data/apps.baozishan.agricola_score/cache/");
                FileOutputStream fo = null;
                File imageFile = null;
                try {
                    if (directory.exists() == false) {
                        directory.mkdirs();
                    }
                    imageFile = File.createTempFile("sharedImage", ".jpg",  directory);
                    imageFile.createNewFile();
                    fo = new FileOutputStream(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean bSucc = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fo);
                if (bSucc == false)
                    return;

                // 分享图片
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });
        //getIntent().get;
        //getPlayerScoreInfoMap
    }
}