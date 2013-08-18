package apps.baozishan.agricola_score.Utils;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



/**
 * Created by gohan on 7/13/13.
 */
public class JsonHelper {
    public static JSONObject GetJSONObjectFromStream(InputStream is) {
        if (is == null)
            return null;
        String strJson = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            strJson = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonItem = null;
        try {
            jsonItem = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonItem;
    }

    public static int GetColorFromString(String color) {
        if (color.equalsIgnoreCase("white"))
            return Color.LTGRAY;
        if (color.equalsIgnoreCase("blue"))
            return Color.BLUE;
        if (color.equalsIgnoreCase("yellow"))
            return Color.YELLOW;
        if (color.equalsIgnoreCase("purple"))
            return Color.rgb(255, 0, 255);
        if (color.equalsIgnoreCase("red"))
            return Color.RED;
        if (color.equalsIgnoreCase("green"))
            return Color.GREEN;

        return Color.BLACK;
    }
}
