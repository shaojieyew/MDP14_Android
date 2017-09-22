package mdp14.mdp14app;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import mdp14.mdp14app.bluetooth.BluetoothChatFragment;
import mdp14.mdp14app.model.Map;

public class MainActivity extends AppCompatActivity {
    View map_grid;
    GridLayout base_layout;
    BluetoothChatFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //map_grid = (View) findViewById(R.id.mapview);
        base_layout = (GridLayout) findViewById(R.id.base_layout);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        loadGraphic();
    }


    private void loadGraphic(){
        MapCanvas mCustomDrawableView = new MapCanvas(this);
        mCustomDrawableView.setOnTouchListener(mCustomDrawableView);
        base_layout.removeAllViews();
        base_layout.addView(mCustomDrawableView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void incomingMessage(String readMsg) {
        outgoingMessage(readMsg);

        //update map
        JSONObject obj = null;
        try {
            obj = new JSONObject(readMsg);
            if(obj.has("grid")){
                Map.getInstance().setMapJson(readMsg);
                //Map.getInstance().setMap();
                loadGraphic();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //
    }

    public void outgoingMessage(String sendMsg) {
        fragment.sendMsg(sendMsg);
    }
}
