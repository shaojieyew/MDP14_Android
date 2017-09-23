package mdp14.mdp14app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mdp14.mdp14app.bluetooth.BluetoothChatFragment;
import mdp14.mdp14app.model.Map;
import mdp14.mdp14app.model.Position;
import mdp14.mdp14app.model.Robot;
import mdp14.mdp14app.model.WayPoint;

public class MainActivity extends AppCompatActivity {
    GridLayout base_layout;
    BluetoothChatFragment fragment;
    MenuItem menu_set_robot_position;
    MenuItem menu_set_waypoint;
    MenuItem menu_remove_waypoint;

    MenuItem menu_update_map;
    MenuItem menu_auto_update_map;
    MenuItem menu_set_config1;
    MenuItem menu_set_config2;
    MenuItem menu_enable_swipe_input;
    MenuItem menu_show_bluetooth_chat;

    TextView tv_status;
    Button btn_forward;
    Button btn_left;
    Button btn_right;
    Button btn_terminate;
    Button btn_explr;
    Button btn_fastest;
    Button btn_config1;
    Button btn_config2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_status = (TextView) findViewById(R.id.tv_status);
        btn_forward = (Button) findViewById(R.id.btn_forward);
        btn_left= (Button) findViewById(R.id.btn_left);
        btn_right= (Button) findViewById(R.id.btn_right);
        btn_terminate= (Button) findViewById(R.id.btn_terminate);
        btn_explr= (Button) findViewById(R.id.btn_explr);
        btn_fastest= (Button) findViewById(R.id.btn_fastest);
        btn_config1= (Button) findViewById(R.id.btn_config1);
        btn_config2= (Button) findViewById(R.id.btn_config2);
        //map_grid = (View) findViewById(R.id.mapview);
        base_layout = (GridLayout) findViewById(R.id.base_layout);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        setBtnListener();
        loadGrid();
    }

    private void setBtnListener(){
        btn_forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().moveForward();
                loadGrid();
            }
        });
        btn_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().rotateLeft();
                loadGrid();
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().rotateRight();
                loadGrid();
            }
        });
        btn_terminate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_explr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_fastest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_config1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_config2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu_set_robot_position = menu.findItem(R.id.action_set_robot_position);
        menu_set_waypoint = menu.findItem(R.id.action_set_waypoint);
        menu_remove_waypoint = menu.findItem(R.id.action_remove_waypoint);
        menu_update_map = menu.findItem(R.id.action_update_map);
        menu_auto_update_map = menu.findItem(R.id.action_auto_update_map);
        menu_set_config1 = menu.findItem(R.id.action_set_config_string1);
        menu_set_config2 = menu.findItem(R.id.action_set_config_string2);
        menu_enable_swipe_input = menu.findItem(R.id.action_enable_swipe_input);
        menu_show_bluetooth_chat = menu.findItem(R.id.action_show_bluetooth_chat);
        return true;
    }

    private void loadGrid(){
        MapCanvas mCustomDrawableView = new MapCanvas(this);
        if(menu_enable_swipe_input!=null&&menu_enable_swipe_input.isChecked()){
            mCustomDrawableView.setGesture(this);
        }else{
            mCustomDrawableView.setOnTouchListener(mCustomDrawableView);
        }
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
        if (id == R.id.action_set_robot_position) {
            boolean checked = item.isChecked();
            clearAllEditableCheckbox();
            item.setChecked(!checked);
            return true;
        }
        if (id == R.id.action_set_waypoint) {
            boolean checked = item.isChecked();
            clearAllEditableCheckbox();
            item.setChecked(!checked);
            return true;
        }
        if (id == R.id.action_remove_waypoint) {
            removeWaypoint();
            return true;
        }
        if (id == R.id.action_update_map) {
            loadGrid();
            return true;
        }
        if (id == R.id.action_auto_update_map) {
            boolean checked = item.isChecked();
            item.setChecked(!checked);
            if(item.isChecked()){
                loadGrid();
            }
            return true;
        }
        if (id == R.id.action_set_config_string1) {
            setConfiguredString(1);
            return true;
        }
        if (id == R.id.action_set_config_string2) {
            setConfiguredString(2);
            return true;
        }

        if (id == R.id.action_enable_swipe_input) {
            boolean checked = item.isChecked();
            clearAllEditableCheckbox();
            item.setChecked(!checked);
            loadGrid();
            return true;
        }
        if (id == R.id.action_show_bluetooth_chat) {
            boolean checked = item.isChecked();
            item.setChecked(!checked);
            if(fragment!=null){
                fragment.showChat(!checked);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setConfiguredString(final int index){

        final EditText txtField = new EditText(this);
        SharedPreferences prefs = getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String retrievedText = prefs.getString("string"+index, null);
        if (retrievedText != null) {
            txtField.setText(retrievedText);
        }

        new AlertDialog.Builder(this)
                .setTitle("Configure String "+index)
                .setView(txtField)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String input = txtField.getText().toString();
                        SharedPreferences.Editor editor = getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
                        editor.putString("string"+index, input);
                        editor.apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void clearAllEditableCheckbox(){
        menu_set_robot_position.setChecked(false);
        menu_set_waypoint.setChecked(false);
        menu_enable_swipe_input.setChecked(false);
    }


    public void incomingMessage(String readMsg) {
        //outgoingMessage(readMsg);
        //update map
        JSONObject obj = null;
        try {
            obj = new JSONObject(readMsg);
            if(obj.has("grid")){
                Map.getInstance().setMapJson(obj.getString("grid"));
            }

            if(obj.has("robotPosition")){
                JSONArray arr = obj.getJSONArray("robotPosition");
                double x = arr.getDouble(0);
                double y = arr.getDouble(1);
                double dir = arr.getDouble(2);
                Robot r = Robot.getInstance();
                r.setDirection((float) dir);
                r.setPosX((float) x+1);
                r.setPosY(19-(float) y-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(menu_auto_update_map!=null&&menu_auto_update_map.isChecked()){
            loadGrid();
        }
        //
    }

    public void outgoingMessage(String sendMsg) {
        fragment.sendMsg(sendMsg);
    }

    //set waypoint
    //set robot
    public void onGridTapped(int posX, int posY) {
        if(menu_set_robot_position.isChecked()){
            Robot r = Robot.getInstance();
            r.setPosX(posX);
            r.setPosY(posY);
            r.setDirection(0);
            menu_set_robot_position.setChecked(false);
        }
        if(menu_set_waypoint.isChecked()){
            Position p = new Position(posX,posY);
            WayPoint.getInstance().setPosition(p);
            menu_set_waypoint.setChecked(false);
        }
        loadGrid();
    }

    private void removeWaypoint(){
        WayPoint.getInstance().setPosition(null);
        loadGrid();
    }

    public void onSwipeTop() {
        if(!Robot.getInstance().rotateToNorth()){
            Robot.getInstance().moveForward();
        }
        loadGrid();
    }

    public void onSwipeLeft() {
        if(!Robot.getInstance().rotateToWest()){
            Robot.getInstance().moveForward();
        }
        loadGrid();
    }

    public void onSwipeRight() {
        if(!Robot.getInstance().rotateToEast()){
            Robot.getInstance().moveForward();
        }
        loadGrid();
    }

    public void onSwipeBottom() {
        if(!Robot.getInstance().rotateToSouth()){
            Robot.getInstance().moveForward();
        }
        loadGrid();
    }
}
