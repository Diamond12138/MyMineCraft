package com.Diamond.myminecraft;

import com.Diamond.SGL.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import android.renderscript.Float3;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.util.Log;

public class InstanceData {
    public static final int MAX_INSTANCE_NUMBER = 100 * 100 * 100;
    public static final String SAVE_PATH = "/storage/emulated/0/myminecraft_data.mmc_data";
    
    public static void save(ArrayList<Sprite> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(SAVE_PATH);
        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        try {
            for (int i = 0; i < data.size(); i++) {
                Sprite sprite = data.get(i);
                Float3 position = sprite.getPosition();
                Float3 rotate = sprite.getRotate();
                Float3 scale = sprite.getScale();
                bw.write(position.x + "," + position.y + "," + position.z);
                bw.write(" ");
                bw.write(rotate.x + "," + rotate.y + "," + rotate.z);
                bw.write(" ");
                bw.write(scale.x + "," + scale.y + "," + scale.z);
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
        }
    }
    
    public static ArrayList<Sprite> read() throws IOException {
        ArrayList<Sprite> result = new ArrayList<Sprite>();
        FileInputStream fis = new FileInputStream(SAVE_PATH);
        InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        try {
            String temp = null;
            while((temp = br.readLine()) != null) {
                Sprite sprite = new Sprite();
                Float3 position = new Float3();
                Float3 rotate = new Float3();
                Float3 scale = new Float3();
                
                String[] temps = temp.split("[ ]+");
                
                String[] position_temp = temps[0].split("[,]+");
                position.x = Float.parseFloat(position_temp[0]);
                position.y = Float.parseFloat(position_temp[1]);
                position.z = Float.parseFloat(position_temp[2]);
                
                String[] rotate_temp = temps[1].split("[,]+");
                rotate.x = Float.parseFloat(rotate_temp[0]);
                rotate.y = Float.parseFloat(rotate_temp[1]);
                rotate.z = Float.parseFloat(rotate_temp[2]);
                
                String[] scale_temp = temps[2].split("[,]+");
                scale.x = Float.parseFloat(scale_temp[0]);
                scale.y = Float.parseFloat(scale_temp[1]);
                scale.z = Float.parseFloat(scale_temp[2]);
                
                sprite.setPosition(position)
                      .setRotate(rotate)
                      .setScale(scale);
                result.add(sprite);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return new ArrayList<Sprite>();
        } finally {
            br.close();
        }
        return result;
    }
}
