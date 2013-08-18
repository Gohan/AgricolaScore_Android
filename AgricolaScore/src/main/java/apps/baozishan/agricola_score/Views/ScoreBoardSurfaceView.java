package apps.baozishan.agricola_score.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

/**
 * Created by gohan on 8/17/13.
 */
public class ScoreBoardSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder = null;

    public ScoreBoardSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public ScoreBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public ScoreBoardSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        setLayoutParams(new RelativeLayout.LayoutParams(800, 800));
        Draw(null);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = null;
    }

    public void Draw(JSONObject jsonScore) {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null)
        {
            return;
        }
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(new Rect(0, 0, 100, 100), paint);
        getHolder().unlockCanvasAndPost(canvas);
    }

}
