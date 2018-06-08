package com.example.fitsh.myapp;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by fitsh on 18-5-27.
 */

public class ImageDenoise {
    public static Bitmap median_denoising(Bitmap src, int n){
        int width =  src.getWidth();
        int height = src.getHeight();
        Bitmap dest = Bitmap.createBitmap(src);
        int mid = n>>1;
        int size = n*n;
        int[] buf= new int[n*n];
        for (int px = 0; px < width - n; ++px)
        {
            for (int py = 0; py < height - n; ++py)
            {
                // gray image denoising
                for (int k = 0; k < size; ++k)
                    buf[k] = src.getPixel(px + k % n, py + k / n)&0xff;

                Arrays.sort(buf);
                if ((size % 2)!=0) dest.setPixel(px + mid, py + mid,buf[size >> 1]);
                else dest.setPixel(px + mid, py + mid,(buf[size >> 1] + buf[(size + 1) >> 1]) / 2);

                // convert to binary image
                if ((dest.getPixel(px + mid, py + mid)&0xff) < 128) dest.setPixel(px + mid, py + mid,(255<<24)|(255<<16)|(255<<8)|255);
                else dest.setPixel(px + mid, py + mid,(255<<24)|(0<<16)|(0<<8)|0);
            }
        }
        return dest;
    }
    public static Bitmap discrete_denoising(Bitmap src, int n , int hreshold )
    {
        RecognitionImage.saveImage(src);
        int mid = n >> 1;
        Bitmap dest = Bitmap.createBitmap(src);
        RecognitionImage.saveImage(dest);
        for (int px = 0; px < src.getWidth() - n; ++px)
        {
            for (int py = 0; py < src.getHeight() - n; ++py)
            {
                int color = src.getPixel(px+mid,py+mid)&0xff;
                if (color == 0)
                {
                    int sum = 0;
                    for (int k = 0; k < n * n; ++k)
                    {
                        sum += ((src.getPixel(px + k % n, py + k / n)&0xff)==0)? 1 : 0;
                    }
                   // Log.i("sum=====",sum+"");
                    int gray = 255;
                    gray = (255<<24)|(gray<<16)|(gray<<8)|gray;
                    if (sum < hreshold) {    // exclude self
                        dest.setPixel(px + mid, py + mid, gray);
                       // Log.i("sum=====", sum + "");
                    }
                }
            }
        }
        RecognitionImage.saveImage(dest);
        return dest;
    }
}
