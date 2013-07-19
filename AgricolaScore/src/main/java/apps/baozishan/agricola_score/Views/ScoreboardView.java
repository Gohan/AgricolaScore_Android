package apps.baozishan.agricola_score.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by gohan on 7/19/13.
 */
public class ScoreboardView extends View {

    public ScoreboardView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint pt = new Paint();
        pt.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 100, 100, pt);
    }



}
