package apps.baozishan.agricola_score;

import apps.baozishan.agricola_score.Utils.PlayerItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gohan on 7/11/13.
 */
public class PlayerItemAdapter extends ArrayAdapter<PlayerItem>{

    private ArrayList<PlayerItem> objects;
    private Context context;
    private int layoutResourceId;
    private JSONObject gameInfo;

    public PlayerItemAdapter(Context context, int layoutResourceId, ArrayList<PlayerItem> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.objects = objects;
    }

    public void AttachGameInfo(JSONObject gameInfo) {
        this.gameInfo = gameInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerScoreHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerScoreHolder();
            holder.txtName = (TextView)row.findViewById(R.id.playername);
            holder.txtScore = (TextView)row.findViewById(R.id.playerscore);

            row.setTag(holder);
        }
        else
        {
            holder = (PlayerScoreHolder)row.getTag();
        }

        PlayerItem item = this.objects.get(position);
        holder.txtName.setText(item.getName());
        holder.txtName.setTextColor(item.getColorCode());
        holder.txtScore.setText(Integer.toString(item.getScore()));
        return row;
    }

    static class PlayerScoreHolder
    {
        TextView txtName;
        TextView txtScore;
    }
}
