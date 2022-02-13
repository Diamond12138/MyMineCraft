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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import java.io.IOException;

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

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getRealMetrics(metrics);
        int SCREEN_WIDTH = metrics.widthPixels;
        int SCREEN_HEIGHT = metrics.heightPixels;
        final float min_speed = 0.1f;
        final int ready_time = 1000;

        surfaceView = new MySurfaceView(this);
        surfaceView.requestFocus();
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setClickable(true);

        relativeLayout1 = (RelativeLayout)findViewById(R.id.activitymainRelativeLayout1);
        relativeLayout1.addView(surfaceView);

        goButton = new Button(this);
        goButton.setText("go");
        goButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams gop = new RelativeLayout.LayoutParams(goButton.getLayoutParams());
        gop.setMargins(200, SCREEN_HEIGHT - 600, 400, SCREEN_HEIGHT - 400);
        goButton.setLayoutParams(gop);
        goButton.setClickable(true);
        goButton.setOnTouchListener(new View.OnTouchListener(){

                public long first;
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        first = System.currentTimeMillis();
                    }
                    float d = 1;
                    if ((System.currentTimeMillis() - first) < ready_time) {
                        d = min_speed;
                    }
                    Float3 distance = VectorUtil.mult(d, VectorUtil.normalize(VectorUtil.sub(surfaceView.renderer.camera.getCenter(), surfaceView.renderer.camera.getPosition())));
                    surfaceView.renderer.camera.move(distance);
                    return true;
                }
            });
        relativeLayout1.addView(goButton);

        backButton = new Button(this);
        backButton.setText("back");
        backButton.setLeft(0);
        backButton.setTop(100);
        backButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams backp = new RelativeLayout.LayoutParams(backButton.getLayoutParams());
        backp.setMargins(200, SCREEN_HEIGHT - 200, 400, SCREEN_HEIGHT);
        backButton.setLayoutParams(backp);
        backButton.setClickable(true);
        backButton.setOnTouchListener(new View.OnTouchListener(){

                public long first;
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        first = System.currentTimeMillis();
                    }
                    float d = 1;
                    if ((System.currentTimeMillis() - first) < ready_time) {
                        d = min_speed;
                    }
                    Float3 distance = VectorUtil.mult(-d, VectorUtil.normalize(VectorUtil.sub(surfaceView.renderer.camera.getCenter(), surfaceView.renderer.camera.getPosition())));
                    surfaceView.renderer.camera.move(distance);
                    return true;
                }
            });
        relativeLayout1.addView(backButton);

        leftButton = new Button(this);
        leftButton.setText("left");
        leftButton.setLeft(0);
        leftButton.setTop(200);
        leftButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams leftp = new RelativeLayout.LayoutParams(leftButton.getLayoutParams());
        leftp.setMargins(0, SCREEN_HEIGHT - 400, 200, SCREEN_HEIGHT - 200);
        leftButton.setLayoutParams(leftp);
        leftButton.setClickable(true);
        leftButton.setOnTouchListener(new View.OnTouchListener(){

                public long first;
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        first = System.currentTimeMillis();
                    }
                    float d = 1;
                    if ((System.currentTimeMillis() - first) < ready_time) {
                        d = min_speed;
                    }
                    Float3 center = surfaceView.renderer.camera.getCenter();
                    Float3 position = surfaceView.renderer.camera.getPosition();
                    Float3 up = VectorUtil.add(position, surfaceView.renderer.camera.getUp());
                    Float3 A = VectorUtil.sub(up, position);
                    Float3 B = VectorUtil.sub(center, position);
                    Float3 distance = VectorUtil.mult(d, VectorUtil.normalize((VectorUtil.cross(A, B))));
                    surfaceView.renderer.camera.move(distance);
                    return true;
                }
            });
        relativeLayout1.addView(leftButton);

        rightButton = new Button(this);
        rightButton.setText("right");
        rightButton.setLeft(0);
        rightButton.setTop(300);
        rightButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams rightp = new RelativeLayout.LayoutParams(rightButton.getLayoutParams());
        rightp.setMargins(400, SCREEN_HEIGHT - 400, 600, SCREEN_HEIGHT - 200);
        rightButton.setLayoutParams(rightp);
        rightButton.setClickable(true);
        rightButton.setOnTouchListener(new View.OnTouchListener(){

                public long first;
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        first = System.currentTimeMillis();
                    }
                    float d = 1;
                    if ((System.currentTimeMillis() - first) < ready_time) {
                        d = min_speed;
                    }
                    Float3 center = surfaceView.renderer.camera.getCenter();
                    Float3 position = surfaceView.renderer.camera.getPosition();
                    Float3 up = VectorUtil.add(position, surfaceView.renderer.camera.getUp());
                    Float3 A = VectorUtil.sub(center, position);
                    Float3 B = VectorUtil.sub(up, position);
                    Float3 distance = VectorUtil.mult(d, VectorUtil.normalize(VectorUtil.cross(A, B)));
                    surfaceView.renderer.camera.move(distance);
                    return true;
                }
            });
        relativeLayout1.addView(rightButton);

        addButton = new Button(this);
        addButton.setText("add");
        addButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams addp = new RelativeLayout.LayoutParams(addButton.getLayoutParams());
        addp.setMargins(SCREEN_WIDTH - 200, SCREEN_HEIGHT - 400, SCREEN_WIDTH, SCREEN_HEIGHT - 200);
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
        removeButton.setTextSize(12);
        removeButton.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        RelativeLayout.LayoutParams removep = new RelativeLayout.LayoutParams(removeButton.getLayoutParams());
        removep.setMargins(SCREEN_WIDTH - 200, SCREEN_HEIGHT - 200, SCREEN_WIDTH, SCREEN_HEIGHT);
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
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 2, 0, "create new world:none");
        menu.add(0, 3, 0, "create new world:tiny");
        menu.add(0, 4, 0, "create new world:medium");
        menu.add(0, 5, 0, "create new world:huge");
        menu.add(0, 6, 0, "save");
        menu.add(0, 7, 0, "load");
        menu.add(0, 1, 0, "help");
        menu.add(0, 0, 0, "exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                finish();
                break;
            case 1:
                AlertDialog help_dialog = new AlertDialog.Builder(this)
                    .setTitle("帮助")
                    .setMessage("go:向前移动\nback:向后移动\nleft:向左移动\nright:向右移动\nadd:增加方块\nremove:移除方块\n:create new world:创建新世界，从上到下分别为：无，小，中，大\nsave:把地图以文本格式保存到内存中，地址：" + InstanceData.SAVE_PATH + "，请确保已经给了权限\nload:把内存中的地图读取过来\nhelp:帮助\nexit:退出")
                    .setPositiveButton("确定", null)
                    .create();
                help_dialog.show();
                break;
            case 2:
                surfaceView.renderer.createNewWorld(0);
                break;
            case 3:
                surfaceView.renderer.createNewWorld(10);
                break;
            case 4:
                surfaceView.renderer.createNewWorld(50);
                break;
            case 5:
                surfaceView.renderer.createNewWorld(100);
                break;
            case 6:
                try {
                    InstanceData.save(surfaceView.renderer.cubes.sprites);
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertDialog fail_to_save_dialog = new AlertDialog.Builder(this)
                        .setTitle("保存失败")
                        .setMessage("请确保已经给了权限，可以在设置中找到")
                        .setPositiveButton("确定", null)
                        .create();
                    fail_to_save_dialog.show();
                }
                break;
            case 7:
                try {
                    surfaceView.renderer.cubes.sprites = InstanceData.read();
                    surfaceView.renderer.needUpdate = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertDialog fail_to_load_dialog = new AlertDialog.Builder(this)
                        .setTitle("加载失败")
                        .setMessage("请确保已经给了权限并且保存过地图")
                        .setPositiveButton("确定", null)
                        .create();
                    fail_to_load_dialog.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
} 
