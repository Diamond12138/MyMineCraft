package com.Diamond.myminecraft;

import com.Diamond.SGL.*;
import java.util.ArrayList;
import android.renderscript.Float3;
import android.opengl.GLES32;

public class CubeInstance {
    public ArrayList<Float3> positions;
    public int[] VBOs;
    public int vCount;

    public CubeInstance(ObjLoader loader) {
        vCount = loader.vertices.length / 3;

        VBOs = new int[4];
        VBOs[0] = BufferUtil.bindVBO(Program.VertexAttribLocation.a_position, 3, loader.vertices, GLES32.GL_STATIC_DRAW);
        VBOs[1] = BufferUtil.bindVBO(Program.VertexAttribLocation.a_normal, 3, loader.normals, GLES32.GL_STATIC_DRAW);
        VBOs[2] = BufferUtil.bindVBO(Program.VertexAttribLocation.a_texCoord, 3, loader.texCoords, GLES32.GL_STATIC_DRAW);
        VBOs[3] = BufferUtil.bindVBO(4, 3, getPositionArray(), GLES32.GL_DYNAMIC_DRAW);
        GLES32.glVertexAttribDivisor(4, 1);
    }

    public float[] getPositionArray() {
        float[] array = new float[positions.size() * 3];
        for (int i = 0; i < positions.size(); i++) {
            float[] vec = VectorUtil.toArray(positions.get(i));
            for (int j = 0; j < 16; j++) {
                array[i * 16 + j] = vec[j];
            }
        }
        return array;
    }

    public CubeInstance update() {
        float[] array = getPositionArray();

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBOs[3]);
        GLES32.glBufferSubData(GLES32.GL_ARRAY_BUFFER, 0, array.length * 4, BufferUtil.toFloatBuffer(array));
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        return this;
    }

    public CubeInstance add(Float3 position) {
        if (search(position) == -1) {
            positions.add(position);}
        return this;
    }

    public int search(Float3 position) {
    	int i = 0;
        for (i = 0; i < positions.size(); i++) {
            if (VectorUtil.equal(position, positions.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public CubeInstance remove(Float3 position) {
        int i = search(position);
        if (i != -1) {
            positions.remove(i);
        }
        return this;
    }

    public CubeInstance draw(Program program) {
    	for (int i = 0; i < VBOs.length; i++) {
            GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBOs[i]);
        }
        GLES32.glDrawArraysInstanced(GLES32.GL_TRIANGLES, 0, vCount, positions.size());
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        return this;
    }
}
