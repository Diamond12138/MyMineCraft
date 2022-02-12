package com.Diamond.myminecraft;

import com.Diamond.SGL.*;
import android.opengl.GLES32;

public class TargetCube extends Sprite {
    public int VAO;
    public int vCount;
    
    public TargetCube() {
        float[] vertices = new float[]{
            0.5f,0.5f,0.5f,
            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f
        };
        
        float[] colors = new float[8 * 4];
        for (int i = 0; i < 8 * 4; i += 4) {
            colors[i + 0] = 0.5f;
            colors[i + 1] = 0.5f;
            colors[i + 2] = 0.5f;
            colors[i + 3] = 1.0f;
        }
        
        int[] indices = new int[]{
            0,1,
            1,2,
            2,3,
            3,0,
            
            4,5,
            5,6,
            6,7,
            7,4,
            
            0,4,
            1,5,
            2,6,
            3,7
        };
        
        vCount = indices.length;
        
        VAO = BufferUtil.genVertexArray();
        GLES32.glBindVertexArray(VAO);
        int vbo1 = BufferUtil.bindVBO(Program.VertexAttribLocation.a_position,3,vertices,GLES32.GL_STATIC_DRAW);
        int vbo2 = BufferUtil.bindVBO(Program.VertexAttribLocation.a_color,4,colors,GLES32.GL_STATIC_DRAW);
        int ebo = BufferUtil.bindEBO(indices,GLES32.GL_STATIC_DRAW);
        GLES32.glBindVertexArray(0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0);
        BufferUtil.deleteBuffers(new int[]{vbo1,vbo2,ebo});
    }
    
    public TargetCube draw(Program program) {
        program.setUniform("u_model",getMatrixArray());
        GLES32.glBindVertexArray(VAO);
        GLES32.glDrawElements(GLES32.GL_LINES,vCount,GLES32.GL_UNSIGNED_INT,0);
        GLES32.glBindVertexArray(0);
        return this;
    }
}
