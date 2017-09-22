package mdp14.mdp14app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import mdp14.mdp14app.model.Map;

public class MapCanvas extends View implements View.OnTouchListener {
    final float scale = getResources().getDisplayMetrics().density;

    Paint grayPaint = new Paint();

    public MapCanvas(Context context) {
        super(context);
        grayPaint.setColor(Color.LTGRAY);
    }

    float padding = 50;
    protected void onDraw(Canvas canvas) {
        float x = padding;
        float y = 0;
        float w = this.getWidth()-2*padding;
        float h = this.getHeight()-padding;

        //draw background
        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setColor(Color.RED);
        mDrawable.setBounds((int)x, (int)y, (int)x + (int)w, (int)y + (int)h);
        mDrawable.draw(canvas);

        //draw line
        drawExploredTile(canvas);

        //draw obstacle

        //draw robot

        //drawlines
        for(int i = 0;i<15;i++){
            canvas.drawLine(i*(w/15)+x, y, i*(w/15)+x, y+h, grayPaint);
        }
        for(int i = 0;i<20;i++){
            canvas.drawLine(x, i*(h/20)+y,x+w,i*(h/20)+y, grayPaint);
        }
    }

    private void drawExploredTile(Canvas canvas) {
        //draw explored
        float sx = padding;
        float sy = 0;
        float w = this.getWidth()-2*padding;
        float h = this.getHeight()-padding;
        float cellWidth = w/15f;
        float cellHeight = h/20f;
        int[][]explored = Map.getInstance().getExploredTiles();
        int[][]obstacles = Map.getInstance().getObstacles();
        for(int x =0;x<15;x++){
            for(int y =0;y<20;y++){
              if( explored[y][x] == 1){
                  int posX = (int)(sx+x*cellWidth);
                  int posY = (int)(sy+y*cellHeight);
                  ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
                  mDrawable.getPaint().setColor(Color.WHITE);
                  if(obstacles[y][x]==1){
                      mDrawable.getPaint().setColor(Color.BLACK);
                  }
                  mDrawable.setBounds(posX, posY, posX+(int)cellWidth, (int)posY+(int)cellHeight);
                  mDrawable.draw(canvas);
              }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        me.getSource();
        switch(me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float inX = me.getX();
                float inY = me.getY();
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;

                float x = padding;
                float y = 0;
                float w = this.getWidth()-2*padding;
                float h = this.getHeight()-padding;
                float selectedX = inX-x;
                float selectedY = inY-y;
                float cellWidth = w/15f;
                float cellHeight = h/20f;
                int posX = (int)(selectedX/cellWidth);
                int posY = 19-(int)(selectedY/cellHeight);
                Toast toast = Toast.makeText(context,"tapped: "+posX+","+posY, duration);
                toast.show();
                break;
        }
        return true;
    }


}
