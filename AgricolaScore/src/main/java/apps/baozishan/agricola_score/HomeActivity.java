package apps.baozishan.agricola_score;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import apps.baozishan.agricola_score.Utils.PlayerItem;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RadioGroup rg = (RadioGroup)findViewById(R.id.home_rg_playernumber);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String strPlayerNumber = ((RadioButton) findViewById(i)).getText().toString();
                ShowMessageBox(strPlayerNumber);
                SetListViewByPlayerNumber(Integer.parseInt(strPlayerNumber));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void ShowMessageBox(String choose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Your choose:")
                .setMessage("Are you sure?" + choose)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static int getItemHeightofListView(ListView listView, int items) {

        ListAdapter mAdapter = listView.getAdapter();

        int listviewElementsHeight = 0;
        // for listview total item height
        // items = mAdapter.getCount();


        for (int i = 0; i < items; i++) {

            View childView = mAdapter.getView(i, null, listView);
            childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            listviewElementsHeight+= childView.getMeasuredHeight();
        }

        return listviewElementsHeight;
    }

    private void SetListViewByPlayerNumber(int i) {
        ListView lv = (ListView)findViewById(R.id.listView);
        ArrayList<PlayerItem> myList = new ArrayList<PlayerItem>();
        myList.add(new PlayerItem("Player 1", 10));
        myList.add(new PlayerItem("Player 2", 20));
        myList.add(new PlayerItem("Player 3", 30));
        lv.setAdapter(new PlayerItemAdapter(this, R.layout.listviewitem_playerinfo, myList));
        int height = getItemHeightofListView(lv, myList.size());
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.height = height;
        lv.setLayoutParams(lp);
    }
}
