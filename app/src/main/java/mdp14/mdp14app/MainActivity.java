package mdp14.mdp14app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    String status = "Idle";

    /*/For robot fastest-path animation
    int i = 0;
    String movement[] = new String[0]; //if there are multiple movements
    int movement_size = movement.length;
    Handler handler = new Handler();
    Handler handler1 = new Handler();
    int j = 0;
    String step [] = new String [0];
    int noOfSteps = 0;
    /*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize all the buttons
        tv_status = (TextView) findViewById(R.id.tv_status);
        btn_forward = (Button) findViewById(R.id.btn_forward);
        btn_left= (Button) findViewById(R.id.btn_left);
        btn_right= (Button) findViewById(R.id.btn_right);
        btn_terminate= (Button) findViewById(R.id.btn_terminate);
        btn_explr= (Button) findViewById(R.id.btn_explr);
        btn_fastest= (Button) findViewById(R.id.btn_fastest);
        btn_config1= (Button) findViewById(R.id.btn_config1);
        btn_config2= (Button) findViewById(R.id.btn_config2);
        base_layout = (GridLayout) findViewById(R.id.base_layout);

        //initialize the bluetooth service component
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        //initialize default status "idle"
        updateStatus(status);

        //initialize listener for all the buttons
        setBtnListener();

        //initialize the grid
        loadGrid();
    }

    //method to update the label textview
    public void updateStatus(String status){
        this.status = status;
        tv_status.setText(status);
    }

    //method to set all the event for buttons
    private void setBtnListener(){
        btn_forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().moveForward(10);


                outgoingMessage("MR||F10|");
                loadGrid();
            }
        });
        btn_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().rotateLeft();
                outgoingMessage("MR||L90|");
                loadGrid();
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Robot.getInstance().rotateRight();
                outgoingMessage("MR||R90|");
                loadGrid();
            }
        });
        btn_terminate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outgoingMessage(STATUS_TERMINATE_HEADER);
                updateStatus(STATUS_TERMINATE_DESC);
            }
        });
        btn_explr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String positionX = String.valueOf(Robot.getInstance().getPosX()) ;
                String positionY = String.valueOf(Robot.getInstance().getPosY()) ;
                String direction = String.valueOf(Robot.getInstance().getDirection());
                String exploringMsg = STATUS_EX_HEADER.concat("|").concat(positionX).concat(",").concat(positionY).concat(",").concat(direction).concat("||");
                outgoingMessage(exploringMsg);
                updateStatus(STATUS_EX_DESC);
            }
        });
        btn_fastest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (WayPoint.wp.getPosition() == null) {
                    status = "Setting WayPoint";
                    updateStatus(status);

                    menu_set_robot_position.setChecked(false);
                    menu_set_waypoint.setChecked(true);
                    Toast toast=Toast.makeText(getApplicationContext(),"Tap the Grid to set WayPoint",Toast.LENGTH_LONG);
                    toast.show();

                }else{
                    String positionX = String.valueOf(Robot.getInstance().getPosX()) ;
                    String positionY = String.valueOf(Robot.getInstance().getPosY()) ;
                    String direction = String.valueOf(Robot.getInstance().getDirection());
                    String wpX = String.valueOf(WayPoint.getInstance().getPosition().getPosX());
                    String wpY = String.valueOf(WayPoint.getInstance().getPosition().getPosY());
                    String FPMsg = STATUS_FP_HEADER.concat("|").concat(positionX).concat(",").concat(positionY).concat(",").concat(direction).concat("||").concat(wpX).concat(",").concat(wpY);
                    outgoingMessage(FPMsg);
                    //outgoingMessage(STATUS_FP_HEADER);
                    updateStatus(STATUS_FP_DESC);
                }


            }
        });
        btn_config1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
                String retrievedText = prefs.getString("string1", null);
                if (retrievedText != null) {
                    outgoingMessage(retrievedText);
                }
            }
        });
        btn_config2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
                String retrievedText = prefs.getString("string2", null);
                if (retrievedText != null) {
                    outgoingMessage(retrievedText);
                }
            }
        });
    }

    //initalize all the menu item button
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

    //load/reload the Grid View into the page
    private void loadGrid(){
        MapCanvas mCustomDrawableView = new MapCanvas(this);
        //set touch event and swipe event
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

    //this is the event when menu item is clicked
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

    //this method opens up a dialog to save input string on the phone
    //index is the key of the string.
    //eg string 1 is stored in 'string1', string 2 in 'string2'
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

    //this is to uncheck all the checkable menuitem
    private void clearAllEditableCheckbox(){
        menu_set_robot_position.setChecked(false);
        menu_set_waypoint.setChecked(false);
        menu_enable_swipe_input.setChecked(false);
    }

    public static final String STATUS_EX_DESC = "Moving (Exploring)";
    public static final String STATUS_EX_HEADER = "EX";
    public static final String STATUS_FP_DESC = "Moving (Fastest Path)";
    public static final String STATUS_FP_HEADER = "FP";
    public static final String STATUS_DONE_DESC = "Stopped";
    public static final String STATUS_DONE_HEADER = "DONE";
    public static final String STATUS_TERMINATE_HEADER = "TE|||";
    public static final String STATUS_TERMINATE_DESC = "Terminated";

    //method is ran when new message comes in
    public void incomingMessage(String readMsg) {
        //outgoingMessage(readMsg);
        //update map

        final Robot r = Robot.getInstance();

        if(readMsg.length()>0){
            menu_show_bluetooth_chat.setChecked(true);
            fragment.showChat(true);
            final String delimiterPattern = "\\|";
            String message []= readMsg.split(delimiterPattern);
            if(message[0].equals(STATUS_EX_HEADER)){ //explore

                updateStatus(STATUS_EX_DESC);
                Map.getInstance().setMap(message[1],"",message[2]);

                String posAndDirect[] = message[3].split(",");
                r.setPosX(Float.parseFloat(posAndDirect[0]));
                r.setPosY(Float.parseFloat(posAndDirect[1]));
                r.setDirection(Float.parseFloat(posAndDirect[2]));
                //EX|[explored map]|[explored obstacles]|[robot position & direction]|
            }

            //fastest path
            if(message[0].equals(STATUS_FP_HEADER)){


                updateStatus(STATUS_FP_DESC);

                /*/ New Animation
                int i = 0;
                String movement[] = message[4].split(",");
                int movement_size = movement.length;
                final Handler handlerL = new Handler();
                final Handler handlerR = new Handler();
                for (i = 0; i < movement_size; i++){
                    if (movement[i].contains("L")){
                        handlerL.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Robot.getInstance().rotateLeft();
                                loadGrid();
                            }
                        },1000);
                    }
                    else if (movement[i].contains("R")){
                        handlerR.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Robot.getInstance().rotateRight();
                                loadGrid();
                            }
                        },2000);
                    }
                }
                /*/


                /*/ Old Animation
                i=0;
                movement = new String[0];
                movement = (message[4].split(",")); //if there are multiple movements
                movement_size = movement.length;

                while (i < movement_size) {
                    handler.postDelayed(new Runnable() { //move the robot after every movement
                        @Override
                        public void run() {

                            //handler1 = new Handler();
                            if (movement[i].contains("F")) {
                                Robot.getInstance().moveForward(10);

                            } else if (movement[i].contains("L")) {
                                Robot.getInstance().rotateLeft();

                            } else if (movement[i].contains("R")) {
                                Robot.getInstance().rotateRight();
                            }

                            loadGrid();

                        }


                    }, 1000);
                    i++;

                }/*/


                // Without animation

                String movement[] = message[4].split(","); //if there are multiple movements
                int movement_size = movement.length;

                for (int i = 0; i < movement_size; i++){
                    if (movement[i].contains("F")) {
                        int j = 0;
                        String step[] = movement[i].split("F");
                        int noOfSteps = (Integer.parseInt(step[1])/10);
                        while (j < noOfSteps){
                            Robot.getInstance().moveForward(10);
                            j++;
                        }

                    }
                    if (movement[i].contains("L")){
                        Robot.getInstance().rotateLeft();
                    }
                    if (movement[i].contains("R")){
                        Robot.getInstance().rotateRight();
                        }

                } //


            }
            if(message[0].equals(STATUS_DONE_HEADER)){ //done
                updateStatus(STATUS_DONE_DESC);
            }



        }
        //setMap is for the actual; setMapJson is for AMD

        /*
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
        */

        if(menu_auto_update_map!=null&&menu_auto_update_map.isChecked()){
            loadGrid();
        }

    }



    //method to send out message to rpi thru bluetooth
    public boolean outgoingMessage(String sendMsg) {
        return fragment.sendMsg(sendMsg);
    }

    //on the coordinate tapped
    //set waypoint, if menuitem is checked
    //set robot, if menuitem is checked
    public void onGridTapped(int posX, int posY) {
        if(menu_set_robot_position.isChecked()){
            Robot r = Robot.getInstance();
            r.setPosX(posX);
            r.setPosY(posY);
            r.setDirection(0);
            //outgoingMessage("robot position : ("+(int)posX+","+(int)posY+","+0+")");

            //Make a prompt here to confirm
            //menu_set_robot_position.setChecked(false);
        }
        if(menu_set_waypoint.isChecked()){
            Position p = new Position(posX,posY);
            WayPoint.getInstance().setPosition(p);
            //outgoingMessage("waypoint position : ("+(int)posX+","+(int)posY+")");

            //Make a prompt here to confirm
            //menu_set_waypoint.setChecked(false);
        }
        loadGrid();
    }

    //clear waypoint
    private void removeWaypoint(){
        WayPoint.getInstance().setPosition(null);
        loadGrid();
    }

    //swipe gesture input
    public void onSwipeTop() {
        if(!Robot.getInstance().rotateToNorth()){
            Robot.getInstance().moveForward(10);
        }
        loadGrid();
    }

    public void onSwipeLeft() {
        if(!Robot.getInstance().rotateToWest()){
            Robot.getInstance().moveForward(10);
        }
        loadGrid();
    }

    public void onSwipeRight() {
        if(!Robot.getInstance().rotateToEast()){
            Robot.getInstance().moveForward(10);
        }
        loadGrid();
    }

    public void onSwipeBottom() {
        if(!Robot.getInstance().rotateToSouth()){
            Robot.getInstance().moveForward(10);
        }
        loadGrid();
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

