package com.Diamond.myminecraft;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.view.WindowManager;
import android.view.Window;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.renderscript.Float3;
import com.Diamond.SGL.*;
import android.util.Log;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public class MainActivity extends Activity {
    public MySurfaceView surfaceView;
    public RelativeLayout relativeLayout1;
    public Button goButton;
    public Button backButton;
    public Button leftButton;
    public Button rightButton;
    public Button addButton;
    public Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        surfaceView = new MySurfaceView(this);
        surfaceView.requestFocus();
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setClickable(true);

        relativeLayout1 = (RelativeLayout)findViewById(R.id.activitymainRelativeLayout1);
        relativeLayout1.addView(surfaceView);

        goButton = new Button(this);
        goButton.setText("go");
        goButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams gop = new RelativeLayout.LayoutParams(goButton.getLayoutParams());
        gop.setMargins(0, 0, 200, 100);
        goButton.setLayoutParams(gop);
        goButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Float3 distance = VectorUtil.normalize(VectorUtil.sub(surfaceView.renderer.camera.getCenter(), surfaceView.renderer.camera.getPosition()));
                    surfaceView.renderer.camera.move(distance);
                }
            });
        relativeLayout1.addView(goButton);

        backButton = new Button(this);
        backButton.setText("back");
        backButton.setLeft(0);
        backButton.setTop(100);
        backButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams backp = new RelativeLayout.LayoutParams(backButton.getLayoutParams());
        backp.setMargins(0, 100, 200, 200);
        backButton.setLayoutParams(backp);
        backButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Float3 distance = VectorUtil.normalize(VectorUtil.sub(surfaceView.renderer.camera.getPosition(), surfaceView.renderer.camera.getCenter()));
                    surfaceView.renderer.camera.move(distance);
                }
            });
        relativeLayout1.addView(backButton);

        leftButton = new Button(this);
        leftButton.setText("left");
        leftButton.setLeft(0);
        leftButton.setTop(200);
        leftButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams leftp = new RelativeLayout.LayoutParams(leftButton.getLayoutParams());
        leftp.setMargins(0, 200, 200, 300);
        leftButton.setLayoutParams(leftp);
        leftButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Float3 center = surfaceView.renderer.camera.getCenter();
                    Float3 position = surfaceView.renderer.camera.getPosition();
                    Float3 up = VectorUtil.add(position, surfaceView.renderer.camera.getUp());
                    Float3 A = VectorUtil.sub(up, position);
                    Float3 B = VectorUtil.sub(center, position);
                    Float3 distance = VectorUtil.normalize(VectorUtil.cross(A, B));
                    surfaceView.renderer.camera.move(distance);
                }
            });
        relativeLayout1.addView(leftButton);

        rightButton = new Button(this);
        rightButton.setText("right");
        rightButton.setLeft(0);
        rightButton.setTop(300);
        rightButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams rightp = new RelativeLayout.LayoutParams(rightButton.getLayoutParams());
        rightp.setMargins(0, 300, 200, 400);
        rightButton.setLayoutParams(rightp);
        rightButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                	Float3 center = surfaceView.renderer.camera.getCenter();
                    Float3 position = surfaceView.renderer.camera.getPosition();
                    Float3 up = VectorUtil.add(position, surfaceView.renderer.camera.getUp());
                    Float3 A = VectorUtil.sub(center, position);
                    Float3 B = VectorUtil.sub(up, position);
                    Float3 distance = VectorUtil.normalize(VectorUtil.cross(A, B));
                    surfaceView.renderer.camera.move(distance);
                }
            });
        relativeLayout1.addView(rightButton);

        addButton = new Button(this);
        addButton.setText("add");
        addButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams addp = new RelativeLayout.LayoutParams(addButton.getLayoutParams());
        addp.setMargins(0, 400, 200, 500);
        addButton.setLayoutParams(addp);
        addButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Sprite sprite = new Sprite();
                    sprite.setPosition(surfaceView.renderer.targetCube.getPosition());
                    surfaceView.renderer.cubes.add(sprite);
                    surfaceView.renderer.needUpdate = true;
                }
            });
        relativeLayout1.addView(addButton);

        removeButton = new Button(this);
        removeButton.setText("remove");
        removeButton.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        RelativeLayout.LayoutParams removep = new RelativeLayout.LayoutParams(removeButton.getLayoutParams());
        removep.setMargins(0, 500, 200, 600);
        removeButton.setLayoutParams(removep);
        removeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    surfaceView.renderer.cubes.remove(new Sprite().setPosition(surfaceView.renderer.targetCube.getPosition()));
                    surfaceView.renderer.needUpdate = true;
                }
            });
        relativeLayout1.addView(removeButton);
    }

    @Override
    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"create new world:10*10");
        menu.add(0,2,0,"create new world:100*100");
        menu.add(0,0,0,"exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                finish();
                break;
            case 1:
                surfaceView.renderer.createNewWorld(10);
                break;
            case 2:
                surfaceView.renderer.createNewWorld(100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
} 
