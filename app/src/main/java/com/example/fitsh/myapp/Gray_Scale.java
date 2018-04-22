package com.example.fitsh.myapp;

import android.graphics.Bitmap;
import android.util.Log;


public class Gray_Scale {
    private static int colorToRGB(int alpha, int red, int green, int blue){
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel <<=8;
        newPixel +=blue;
        return newPixel;
    }
    public static Bitmap getGray(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap image = null;
        image = bitmap.copy(Bitmap.Config.ARGB_8888,true );
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = bitmap.getPixel(i, j);
                int red= (pixel >> 16) & 0xFF;
                int green = (pixel >>8) & 0xFF;
                int blue = pixel & 0xFF;
                int gray = (int)(0.3 * red + 0.59*green + 0.11*blue);
                int newPixel = colorToRGB(255, gray, gray, gray);
                image.setPixel(i,j,newPixel );
            }
        }
        return image;
    }
}
