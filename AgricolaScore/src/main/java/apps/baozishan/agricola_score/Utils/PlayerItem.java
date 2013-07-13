package apps.baozishan.agricola_score.Utils;

import android.graphics.Color;

/**
 * Created by gohan on 7/11/13.
 */
public class PlayerItem {
    private String name;
    private int score;
    private String color;

    public PlayerItem(){

    }

    public PlayerItem(String name, String color, int score){
        this.name = name;
        this.score = score;
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {

        return color;
    }

    public int getColorCode() {
        return JsonHelper.GetColorFromString(color);
    }

    public void setColor(String color) {
        this.color = color;
    }
}
