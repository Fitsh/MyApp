package com.example.fitsh.myapp;

import android.graphics.Bitmap;

public class OtsuBinarryFilter {
    public static Bitmap filter(Bitmap src, Bitmap dest){
        int width = src.getWidth();
        int height = src.getHeight();
        if(dest == null){
            dest = src.copy(Bitmap.Config.ARGB_8888, true);
        }
        int[] histogram = new int[256];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int color = src.getPixel(j,i);
                int tr = (color>>16) & 0xff;
                histogram[tr]++;
            }
        }
        double total = width * height;
        double[] variances = new double[256];
        for(int i = 0; i < variances.length; i++){
            double bw = 0;
            double bmean = 0;
            double bvariance = 0;
            double count  = 0;
            for(int j = 0; j < i; j++){
                count += histogram[j];
                bmean += histogram[j] * j;
            }
            bw = count / total;
            bmean = (count ==0) ? 0 :(bmean / count);
            for(int j = 0; j < i; j++){
                bvariance += (j - bmean)* (j - bmean) * histogram[j];
            }
            bvariance =(count ==0) ? 0: (bvariance/count);
            double fw = 0;
            double fmean = 0;
            double fvariance = 0;
            count = 0;
            for(int j = i; j < variances.length; j++){
                count += histogram[j];
                fmean += histogram[j] * j;
            }
            fw = count / total;
            fmean = (count == 0) ? 0:(fmean / count);
            for(int j = i; j < variances.length; j++){
                fvariance += (j - fmean)* (j - fmean) * histogram[j];
            }
            fvariance = (count == 0) ? 0:(fvariance/count);
            variances[i] = bvariance * bw + fvariance * fw;
        }
        int threshold = 0;
        for(int i = 1; i < variances.length; i++){
            if(variances[i] < variances[threshold]) threshold = i;
        }
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int gray = src.getPixel(j, i) & 0xff;
                if(gray < threshold){
                    gray =255;

                    gray = (0xff << 24)| (gray << 16)| (gray <<8)| gray;
                    dest.setPixel(j,i, gray);
                }
                else{
                    gray = 0;
                    gray = (0xff << 24)| (gray << 16)| (gray <<8)| gray;
                    dest.setPixel(j,i, gray);
                }
            }
        }
        return dest;
    }
}
