package dev.ttetris;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Resources;

public class GLImage {
    public static Bitmap[] bitmap = new Bitmap[8]; 

    public static void load(Resources resources) {
        bitmap[0] = BitmapFactory.decodeResource(resources, R.drawable.cubeamethyst);
        bitmap[1] = BitmapFactory.decodeResource(resources, R.drawable.cubeanchient);
        bitmap[2] = BitmapFactory.decodeResource(resources, R.drawable.cubebrass);   
        bitmap[3] = BitmapFactory.decodeResource(resources, R.drawable.cubelapislazuli);   
        bitmap[4] = BitmapFactory.decodeResource(resources, R.drawable.cubemarble);
        bitmap[5] = BitmapFactory.decodeResource(resources, R.drawable.cubemarblerough); 
        bitmap[6] = BitmapFactory.decodeResource(resources, R.drawable.cubeoak);
        bitmap[7] = BitmapFactory.decodeResource(resources, R.drawable.cubewhitemarble); 
    }  
}
