package com.Diamond.myminecraft;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.opengl.GLES32;
import com.Diamond.SGL.ObjSprite;
import com.Diamond.SGL.Program;
import com.Diamond.SGL.Texture;
import java.io.InputStream;
import java.io.IOException;

public class SkyBox extends ObjSprite {
    public Texture[] textures;
    public GLES32 gl;

    public SkyBox(InputStream is,AssetManager am) {
        super(is);
        Bitmap[] bitmaps = new Bitmap[]{
            Texture.loadBitmap("skybox/bottom.jpg", am),
            Texture.loadBitmap("skybox/top.jpg", am),
            Texture.loadBitmap("skybox/back.jpg", am),
            Texture.loadBitmap("skybox/front.jpg", am),
            Texture.loadBitmap("skybox/left.jpg", am),
            Texture.loadBitmap("skybox/right.jpg", am)
        };
        textures = new Texture[6];
        for (int i = 0;i < 6;i++) {
            textures[i] = new Texture(gl.GL_TEXTURE_2D);
            textures[i].bind(Texture.default_parameters, bitmaps[i]);
        }
    }

    @Override
    public ObjSprite draw(Program program) {
        program.setUniform("u_model", getMatrixArray());
        gl.glBindVertexArray(VAO);
        for (int i = 0;i < 6;i++) {
            textures[i].enable();
            gl.glDrawArrays(gl.GL_TRIANGLES, i * 6, 6);
        }
        gl.glBindVertexArray(0);
        return this;
    }
}
