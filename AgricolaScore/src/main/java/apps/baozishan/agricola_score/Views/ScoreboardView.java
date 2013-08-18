package apps.baozishan.agricola_score.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import org.json.JSONObject;

import apps.baozishan.agricola_score.GameInfoData;

/**
 * Created by gohan on 7/19/13.
 */
public class ScoreboardView extends View {

    private GameInfoData gameInfo;

    public ScoreboardView(Context context) {
        super(context);
    }

    public void SetGameInfo(GameInfoData gameInfo) {
        this.gameInfo = gameInfo;
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint pt = new Paint();
        pt.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 100, 100, pt);

        int nPlayerCount = gameInfo.getPlayerNumber();
        for (int i = 0; i < nPlayerCount; i++) {

        }


    }



}
