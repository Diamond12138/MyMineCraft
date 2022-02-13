package com.Diamond.myminecraft;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import android.content.Context;
import android.renderscript.Float3;
import android.opengl.GLES32;
import android.view.MotionEvent;
import java.io.IOException;
import android.content.res.AssetManager;
import android.graphics.RectF;
import android.renderscript.Float4;
import com.Diamond.SGL.*;
import android.util.Log;

public class MySurfaceView extends GLSurfaceView {
    public class MyRenderer implements GLSurfaceView.Renderer {
        public Program usualProgram;
        public Program lightingProgram;
        
        public Axis axis;
        public Camera camera;
        public GLES32 gl;
        
        public SkyBox skybox;
        
        public ObjLoader cube;
        public ObjInstance cubes;
        public TargetCube targetCube;
        
        public Texture grassTexture;
        
        public boolean needUpdate;
        
        @Override
        public void onSurfaceCreated(GL10 p1, EGLConfig p2) {
            usualProgram = new Program("shader/v.vert","shader/usual.frag",null,getResources());
            lightingProgram = new Program("shader/instance.vert","shader/lighting.frag",null,getResources());
            
            axis = new Axis();
            axis.setScale(0.001f);
            
            targetCube = new TargetCube();
            
            camera = new Camera();
            camera.setView(new Float3(0,1,0),new Float3(0,0,0),new Float3(0,1,0));
            camera.setPerspective(120.0f,0.5f,0.1f,100.0f);
            //camera.setOrbitMode(Camera.ORBIT_CENTER);
            camera.setOrbit(0,0,2);
            
            try {
                AssetManager am = getResources().getAssets();
                
                skybox = new SkyBox(am.open("model/cube.obj"),am);
                skybox.setScale(100);
                
                cube = new ObjLoader(am.open("model/cube.obj"));
                cubes = new ObjInstance(cube,InstanceData.MAX_INSTANCE_NUMBER);
                createNewWorld(100);
                cubes.update();
                
                grassTexture = new Texture(gl.GL_TEXTURE_2D);
                grassTexture.bind(Texture.default_parameters,Texture.loadBitmap(R.raw.grass,getResources()));
            } catch(IOException e) {
                e.printStackTrace();
            }
            
            gl.glEnable(gl.GL_DEPTH_TEST);
            gl.glEnable(gl.GL_CULL_FACE);
        }

        @Override
        public void onSurfaceChanged(GL10 p1, int p2, int p3) {
            gl.glViewport(0,0,p2,p3);
            camera.setPerspective(120.0f,(float)p2 / (float)p3,0.1f,100.0f);
        }

        @Override
        public void onDrawFrame(GL10 p1) {
            gl.glClearColor(0.1f,0.1f,0.1f,1.0f);
            gl.glClear(gl.GL_COLOR_BUFFER_BIT|gl.GL_DEPTH_BUFFER_BIT);
            
            usualProgram.useProgram();
            camera.draw(usualProgram);
            
            usualProgram.setUniform("u_enableTexture",false);
            gl.glLineWidth(10);
            axis.setPosition(camera.getCenter());
            axis.draw(usualProgram);
            
            Float3 position = camera.getPosition();
            position = VectorUtil.add(position,VectorUtil.mult(2,VectorUtil.sub(camera.getCenter(),camera.getPosition())));
            position.x = (int)position.x;
            position.y = (int)position.y;
            position.z = (int)position.z;
            targetCube.setPosition(position);
            targetCube.draw(usualProgram);
            gl.glLineWidth(1);
            
            usualProgram.setUniform("u_enableTexture",true);
            skybox.setPosition(camera.getPosition());
            gl.glFrontFace(gl.GL_CW);
            skybox.draw(usualProgram);
            
            if(needUpdate) {
                cubes.update();
                needUpdate = false;
            }
            
            lightingProgram.useProgram();
            lightingProgram.setUniform("u_lightdirection",new Float3(1.0f,-0.25f,0.5f));
            lightingProgram.setUniform("u_cameraposition",camera.getPosition());
            camera.draw(lightingProgram);
            gl.glFrontFace(gl.GL_CCW);
            lightingProgram.setUniform("u_enableTexture",true);
            grassTexture.enable();
            cubes.draw(lightingProgram);
        }
        
        public void createNewWorld(int size) {
            cubes.sprites.clear();
            for (int i = 0; i < size * size; i++) {
                    cubes.add(new Sprite().setPosition(new Float3(i / size - size / 2,0,i % size - size / 2)));
                }
                needUpdate = true;
        }
    }
    
    
    
    public MyRenderer renderer;
    public float lastX,lastY;
    public MySurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        renderer = new MyRenderer();
        setRenderer(renderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = event.getX();
            lastY = event.getY();
            
            int width = getWidth();
            int height = getHeight();
            
            if(new RectF(0,0,100,100).contains(lastX,lastY)) {
                
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float nowX = event.getX();
            float nowY = event.getY();
            
            float dx = nowX - lastX;
            float dy = nowY - lastY;
            
            dx *= 0.1f;
            dy *= 0.1f;
            
            renderer.camera.rotate(-dx,-dy);
            
            /*if(renderer.camera.getAngleY() < 0) {
                renderer.camera.setAngleY(0);
            }*/
            
            lastX = nowX;
            lastY = nowY;
        }
        return super.onTouchEvent(event);
    }
}
