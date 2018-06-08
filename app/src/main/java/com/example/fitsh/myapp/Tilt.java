package com.example.fitsh.myapp;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fitsh on 18-5-26.
 */

public class Tilt {
    public static Bitmap tilt(Bitmap bitmap){
        double theta = 3.1415926/6;
        int white = 255;
        int m = bitmap.getHeight();
        int n = bitmap.getWidth();
        int row = (int) (m + Math.round((double)m/2));
        int col = (int) (m + Math.round((double)n/2));
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        int minw,minh,maxw,maxh;
        minh = minw = 999999999;
        maxh = maxw = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                int pixel = bitmap.getPixel(j, i);
                pixel = pixel & 0xFF;
                if(pixel == white){
                    Pair<Integer, Integer> pair = new Pair<>();
                    pair.setFirst(i);
                    pair.setSecond(j);
                    list.add(pair);
                    minw = Math.min(minw, pair.getSecond());
                    minh = Math.min(minh, pair.getFirst());
                    maxw = Math.max(maxw, pair.getSecond());
                    maxh = Math.max(maxh, pair.getFirst());
                }
            }
        }
        double u = (double)(maxh - minh + 1)/ (maxw - minw + 1);
        int width = maxw - minw + 1;
        int height = maxh - minh + 1;
        while (theta >=3.1415927/90){
            double a = Math.sin(theta);
            double b = Math.cos(theta);
            List<Pair<Integer, Integer>> list0 = new ArrayList<>();
            List<Pair<Integer, Integer>> list1 = new ArrayList<>();
            int minw0,minw1;
            int minh0,minh1,maxw0,maxw1,maxh1,maxh0;
            minw0 = minw1 = minh1 = minh0 = 999999999;
            maxh0 = maxh1 = maxw0 = maxw1 = 0;
            for(Pair<Integer, Integer> pair: list){
                double x0 = pair.getFirst() - Math.round((double)m/2);
                double y0 = pair.getSecond() - Math.round((double)n/2);
                Pair<Integer, Integer> pair0 = new Pair<>();
                Pair<Integer, Integer> pair1 = new Pair<>();
                //pair0.setFirst((int) Math.ceil( x0*Math.cos(theta) + y0 *Math.sin(theta) + Math.round((double)row/2)));
                pair0.setFirst(pair.getFirst());
                pair0.setSecond((int) Math.ceil( -x0*Math.sin(theta) + y0 *Math.cos(theta) + Math.round((double)col/2)));
                //pair1.setFirst((int) Math.ceil( x0*Math.cos(theta) - y0 *Math.sin(theta) + Math.round((double)row/2)));
                pair1.setFirst(pair.getFirst());
                pair1.setSecond((int) Math.ceil( x0*Math.sin(theta) + y0 *Math.cos(theta) + Math.round((double)col/2)));
                list0.add(pair0);
                list1.add(pair1);
                minw0 = Math.min(minw0, pair0.getSecond());
                minh0 = Math.min(minh0, pair0.getFirst());
                maxw0 = Math.max(maxw0, pair0.getSecond());
                maxh0 = Math.max(maxh0, pair0.getFirst());
                minw1 = Math.min(minw1, pair1.getSecond());
                minh1 = Math.min(minh1, pair1.getFirst());
                maxw1 = Math.max(maxw1, pair1.getSecond());
                maxh1 = Math.max(maxh1, pair1.getFirst());
            }
            double u0 = (double)(maxh0 - minh0 + 1)/ (maxw0 - minw0 + 1);
            double u1 = (double)(maxh1 - minh1 + 1)/ (maxw1 - minw1 + 1);
            if(u0 > u1 && u0 > u){
                list.clear();
                list.addAll(list0);
                u=u0;
                width = maxw0 - minw0 + 1;
                height = maxh0 - minh0 + 1;
                minh = minh0;
                minw = minw0;
            }
            else if(u1>u0 && u1 > u){
                list.clear();
                list.addAll(list1);
                u=u1;
                width = maxw1 - minw1 + 1;
                height = maxh1 - minh1 + 1;
                minh = minh1;
                minw = minw1;
            }
            theta /=2;
        }
        int Max = Math.max(width,height)+3;
        Bitmap dest = Bitmap.createBitmap(Max,Max, Bitmap.Config.ARGB_8888);
        for(int i = 0; i < Max; i++)
        {
            for(int j = 0; j < Max; j++){
                int gray  = 0;
                gray = (255<<24)|(gray<<16)|(gray<<8)|gray;
                dest.setPixel(j,i,gray);
            }
        }
        for(Pair<Integer, Integer> pair: list){
            int gray = white;
            gray = (255<<24)|(gray<<16)|(gray<<8)|gray;
            dest.setPixel((Max-width)/2+pair.getSecond()-minw,(Max-height)/2+pair.getFirst()-minh,gray);
        }
        for(int i = 1; i < Max; i++){
            for(int j = 2; j < Max-1; j++){
                int gray = white;
                gray = (255<<24)|(gray<<16)|(gray<<8)|gray;
                if(dest.getPixel(j,i) !=gray  &&dest.getPixel(j-1,i) == gray &&dest.getPixel(j+1,i) ==gray){
                    dest.setPixel(j,i,dest.getPixel(j-1,i));
                }
            }
        }
        return dest;
    }
}
