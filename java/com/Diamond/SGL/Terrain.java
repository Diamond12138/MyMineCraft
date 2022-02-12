package com.Diamond.SGL;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import java.util.ArrayList;
import android.renderscript.Float3;
import android.opengl.GLES32;
import android.renderscript.Float4;
import android.util.Log;

public class Terrain extends Sprite {
    public int VAO;
    public int vCount;

    public Terrain(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        float xUnit = 1.0f / width;
        float yUnit = 1.0f / 255;
        float zUnit = 1.0f / height;
        
        ArrayList<Float> ys = new ArrayList<Float>();
        for (int i = 0;i < height;i++) {
            for (int j = 0;j < width;j++) {
                int color = bitmap.getPixel(j,i);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int h = (r + g + b) / 3;
                ys.add(h * yUnit);
            }
        }

        ArrayList<Float> vers = new ArrayList<Float>();
        for (int i = 0;i < height - 1;i++) {
            for (int j = 0;j < width - 1;j++) {
                int index0 = i * width + j;
                int index1 = (i + 1) * width + j;
                int index2 = (i + 1) * width + (j + 1);
                int index3 = i * width + (j + 1);
                
                //第一个三角形
                vers.add(j * xUnit);vers.add(ys.get(index0));vers.add(i * zUnit);
                vers.add(j * xUnit);vers.add(ys.get(index1));vers.add((i + 1) * zUnit);
                vers.add((j + 1) * xUnit);vers.add(ys.get(index2));vers.add((i + 1) * zUnit);
                
                //第二个三角形
                vers.add((j + 1) * xUnit);vers.add(ys.get(index2));vers.add((i + 1) * zUnit);
                vers.add((j + 1) * xUnit);vers.add(ys.get(index3));vers.add(i * zUnit);
                vers.add(j * xUnit);vers.add(ys.get(index0));vers.add(i * zUnit);
            }
        }
        
        float[] vertices = new float[vers.size()];
        for(int i = 0;i < vers.size();i++) {
            vertices[i] = vers.get(i) - 0.5f;
        }
        
        ArrayList<Float> nors = new ArrayList<Float>();
        for(int i = 0;i < vertices.length;i += 18) {
            Float3 A = new Float3(vertices[i +  0],vertices[i +  1],vertices[i +  2]);
            Float3 B = new Float3(vertices[i +  3],vertices[i +  4],vertices[i +  4]);
            Float3 C = new Float3(vertices[i +  6],vertices[i +  7],vertices[i +  8]);
            
            Float3 D = new Float3(vertices[i +  9],vertices[i + 10],vertices[i + 11]);
            Float3 E = new Float3(vertices[i + 12],vertices[i + 13],vertices[i + 14]);
            Float3 F = new Float3(vertices[i + 15],vertices[i + 16],vertices[i + 17]);
            
            Float3 V1 = VectorUtil.calculateNormal(A,B,C);V1.y *= V1.y;
            Float3 V2 = VectorUtil.calculateNormal(B,C,A);V2.y *= V2.y;
            Float3 V3 = VectorUtil.calculateNormal(C,A,B);V3.y *= V3.y;
            
            Float3 V4 = VectorUtil.calculateNormal(D,E,F);V4.y *= V4.y;
            Float3 V5 = VectorUtil.calculateNormal(E,F,D);V5.y *= V5.y;
            Float3 V6 = VectorUtil.calculateNormal(F,D,E);V6.y *= V6.y;
            
            float x1 = V1.x + V2.x + V3.x;
            float y1 = V1.y + V2.y + V3.y;
            float z1 = V1.z + V2.z + V3.z;
            Float3 N1 = VectorUtil.normalize(new Float3(x1,y1,z1));
            
            float x2 = V4.x + V5.x + V6.x;
            float y2 = V4.y + V5.y + V6.y;
            float z2 = V4.z + V5.z + V3.z;
            Float3 N2 = VectorUtil.normalize(new Float3(x2,y2,z2));
            
            float x = N1.x + N2.x;
            float y = N1.y + N2.y;
            y *= y;
            float z = N1.z + N2.z;
            Float3 N = VectorUtil.normalize(new Float3(x,y,z));
            
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
            
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
            nors.add(N.x);nors.add(N.y);nors.add(N.z);
        }
        
        float[] normals = new float[nors.size()];
        for(int i = 0;i < nors.size();i++) {
            normals[i] = nors.get(i);
        }
        
        float zero = 0;
        float one = 1;
        ArrayList<Float> texs = new ArrayList<Float>();
        for(int i = 0;i < vertices.length;i += 18) {
            texs.add(zero);texs.add(zero);texs.add(zero);
            texs.add(zero);texs.add(one);texs.add(zero);
            texs.add(one);texs.add(one);texs.add(zero);
            
            texs.add(one);texs.add(one);texs.add(zero);
            texs.add(one);texs.add(zero);texs.add(zero);
            texs.add(zero);texs.add(zero);texs.add(zero);
        }
        
        float[] texCoords = new float[texs.size()];
        for(int i = 0;i < texs.size();i++) {
            texCoords[i] = texs.get(i);
        }
        
        VAO = BufferUtil.genVertexArray();
        GLES32.glBindVertexArray(VAO);
        int vbo1 = BufferUtil.bindVBO(Program.VertexAttribLocation.a_position,3,vertices,GLES32.GL_STATIC_DRAW);
        int vbo2 = BufferUtil.bindVBO(Program.VertexAttribLocation.a_normal,3,normals,GLES32.GL_STATIC_DRAW);
        int vbo3 = BufferUtil.bindVBO(Program.VertexAttribLocation.a_texCoord,3,texCoords,GLES32.GL_STATIC_DRAW);
        GLES32.glBindVertexArray(0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        BufferUtil.deleteBuffers(new int[]{vbo1,vbo2,vbo3});
        
        vCount = vertices.length / 3;
    }
    
    public Terrain draw(Program program) {
        program.setUniform("u_model",getMatrixArray());
        GLES32.glBindVertexArray(VAO);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES,0,vCount);
        GLES32.glBindVertexArray(0);
        return this;
    }
}
