package apps.baozishan.agricola_score.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import apps.baozishan.agricola_score.R;
import apps.baozishan.agricola_score.Utils.JsonHelper;

/**
 * Created by gohan on 8/17/13.
 */
public class ScoreBoardSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int FIRST_COLUME_WIDTH = 140;
    private static final int VALUE_COLUME_WIDTH = 100;
    private static final int ROW_HEIGHT = 40;
    private static final int STRING_SIZE = 28;

    private SurfaceHolder surfaceHolder = null;
    private JSONObject jsonScoreBoard = null;
    private JSONObject jsonGameInfo = null;

    private void Init() {
        jsonScoreBoard = JsonHelper.GetJSONObjectFromStream(
                getResources().openRawResource(R.raw.raw_scoreboard));
        getHolder().addCallback(this);
    }
    public void DrawToCanvas(Canvas canvas) {
        int nRowCount = jsonScoreBoard.optJSONArray("draws").length();
        int nPlayerCount = jsonGameInfo.optInt("PlayerNumber");
        JSONArray arrDraws = jsonScoreBoard.optJSONArray("draws");
        int Height = (nRowCount+2) * ROW_HEIGHT; // 最后一行写一个日期, cppgohan
        int Width = (FIRST_COLUME_WIDTH) + ((nPlayerCount+1) * VALUE_COLUME_WIDTH);

        float fY = VALUE_COLUME_WIDTH / 2;
        JSONArray jsonPlayersInfo = jsonGameInfo.optJSONArray("Player");
        for (int i = 0; i < nRowCount; i++) {
            float fX = 10;
            JSONObject jsonRowInfo = arrDraws.optJSONObject(i);
            SimpleDrawText(canvas,
                    jsonRowInfo.optString("name"),
                    STRING_SIZE,
                    new RectF(fX, fY, fX + FIRST_COLUME_WIDTH, fY + ROW_HEIGHT),
                    Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(255, 0, 0));

            String type = jsonRowInfo.optString("type");
            String key = jsonRowInfo.optString("key");
            fX += FIRST_COLUME_WIDTH;

            for (int p = 0; p < nPlayerCount; p++) {
                String value = GetValueFromJsonObjectByType(jsonPlayersInfo.optJSONObject(p), key, type);
                SimpleDrawText(canvas,
                        value,
                        STRING_SIZE,
                        new RectF(fX, fY, fX + VALUE_COLUME_WIDTH, fY + ROW_HEIGHT),
                        Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(255, 0, 0));
                fX += VALUE_COLUME_WIDTH;
            }

            //canvas.drawText(arrDraws.optJSONObject(i).optString("name"), fX+FIRST_COLUME_WIDTH/2, fY+ROW_HEIGHT/2, paint);
            fY += ROW_HEIGHT;
        }
        // DrawTextInRect
        Time now = new Time();
        now.setToNow();
        String strTime = String.format("%s       cppgohan@2013", now.format("%Y/%m/%d %H:%M:%S"));
        DrawTextWithoutRect(canvas,
                strTime,
                STRING_SIZE,
                new RectF(20, fY, Width - 20, fY + ROW_HEIGHT),
                Color.rgb(0, 0, 255), Paint.Align.LEFT);
    }

    public void SetGameInfoObject(JSONObject jsonGameInfo) {
        this.jsonGameInfo = jsonGameInfo;
    }

    public ScoreBoardSurfaceView(Context context) {
        super(context);
        Init();
    }

    public ScoreBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public ScoreBoardSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        setLayoutParams(new RelativeLayout.LayoutParams(800, 800));
        Draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = null;
    }

    public void SimpleDrawText(Canvas canvas, String text, int size, RectF rect, int background, int foreground, int border) {
        DrawTextInRect(canvas, text, size, rect, background, foreground, border, Paint.Align.CENTER);
    }

    public void DrawTextInRect(Canvas canvas, String text, int size, RectF rect, int background, int foreground, int border, Paint.Align align) {
        Paint paint = new Paint();

        paint.setColor(background);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        paint.setColor(border);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(rect, paint);

        paint.setColor(foreground);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(size);
        paint.setTextAlign(align);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, rect.centerX(), rect.centerY()+bounds.height()/2, paint);
    }

    public void DrawTextWithoutRect(Canvas canvas, String text, int size, RectF rect, int foreground, Paint.Align align) {
        Paint paint = new Paint();

        paint.setColor(foreground);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(size);
        paint.setTextAlign(align);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float fX = 0, fY = 0;
        switch (align) {
            case LEFT:
                fX = rect.left;
                fY = rect.centerY()+bounds.height()/2;
                break;
            case CENTER:
                fX = rect.centerX();
                fY = rect.centerY()+bounds.height()/2;
                break;
            case RIGHT:
                fX = rect.width() - bounds.width();
                fY = rect.centerY()+bounds.height()/2;
                break;
        }

        canvas.drawText(text, fX, fY, paint);
    }

    private String GetValueFromJsonObjectByType(JSONObject obj, String key, String type) {
        String text = null;
        if (type.equalsIgnoreCase("number")) {
            text = obj.optString(key);
        } else if (type.equalsIgnoreCase("row")) {
            text = obj.optJSONArray(key).optString(1);
        } else if (type.equalsIgnoreCase("map_string")) {
            String map_key = obj.optString(key);
            text = jsonScoreBoard.optJSONObject("map_string").optString(map_key);
        } else if (type.equalsIgnoreCase("string")) {
            text = obj.optString(key);
        }
        if (text == null) {
            return "text";
        }
        return text;
    }

    public void Draw() {
        setLayoutParams(new RelativeLayout.LayoutParams(800, 800));
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null)
        {
            return;
        }
        DrawToCanvas(canvas);
        // canvas.drawRect(new Rect(0, 0, 100, 100), paint);
        getHolder().unlockCanvasAndPost(canvas);
        Log.v("SurfaceView", jsonScoreBoard.toString());
        Log.v("SurfaceViwe", jsonGameInfo.toString());
    }

}
