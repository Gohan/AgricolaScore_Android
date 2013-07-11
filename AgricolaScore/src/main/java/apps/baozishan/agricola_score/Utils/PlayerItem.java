package apps.baozishan.agricola_score.Utils;

/**
 * Created by gohan on 7/11/13.
 */
public class PlayerItem {
    private String name;
    private int score;

    public PlayerItem(){

    }

    public PlayerItem(String name, int score){
        this.name = name;
        this.score = score;
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
}
