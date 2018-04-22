package com.example.fitsh.myapp;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DigitalExtraction {
    private static int BLACK = 0;
    private  static List< List< Pair<Integer, Integer > > > lists = new ArrayList<>();
    private static int[][] flag = new int[500][500];
    private static int[] maxWidth = new int[500*500];
    private static int[] maxHeight = new int[500*500];
    private static int[] minWidth = new int[500*500];
    private static int[] minHeight = new int[500*500];
    public static int getDigital(Bitmap src){


        int width = src.getWidth();
        int height = src.getHeight();
        Log.i("MAIN","src width="+src.getWidth() +" height="+src.getHeight());
        int num = 0;
        int fact = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int pixel = src.getPixel(j, i);
                pixel = pixel&0xff;
                System.out.println("i="+i+"   j="+j +" pixel="+pixel);
                flag[i][j] = -1;
                if(pixel != BLACK) continue;
                int mark = num;
                if((mark=checkIsNew(src,i,j)) != -1){
                    maxWidth[mark] = Math.max(maxWidth[mark], j);
                    maxHeight[mark] = Math.max(maxHeight[mark],i );
                    minWidth[mark] = Math.min(minWidth[mark], j);
                    minHeight[mark] = Math.min(minHeight[mark],i );
                    flag[i][j] = mark;
                    Pair<Integer, Integer> pair = new Pair<>(i,j);
                    lists.get(mark).add(pair);

                }else{
                    List<Pair<Integer, Integer>> list = new ArrayList<>();
                    lists.add(list);
                    maxWidth[num] = j;
                    maxHeight[num] = i;
                    minWidth[num] = j;
                    minHeight[num] = i;
                    flag[i][j] = num;
                    Pair<Integer, Integer> pair = new Pair<>(i,j);
                    lists.get(num).add(pair);
                    num++;
                    System.out.println("+++1");
                }
            }
        }
        for(int i = 0; i < num; i++){
            maxHeight[i] = maxHeight[i]-minHeight[i]+1;
            maxWidth[i] = maxWidth[i] - minWidth[i] + 1;
        }
        for(List<Pair<Integer, Integer>> list:lists){
            if(list.size() !=0) fact++;
        }
        System.out.println(num+"dddddddddddddddd");
        return fact;
    }
    private static int checkIsNew(Bitmap src, int h, int w){
        int[] dh={0 , -1, -1, -1};
        int[] dw={-1, -1,  0, -1};
        int width = src.getWidth();
        int height = src.getHeight();
        int min = 0x3f3f3f3f;
        for(int i = 0; i < 4; i++){
            int hh = h + dh[i];
            int ww = w + dw[i];
            if(hh>=0 && ww>=0 && ww<width && flag[hh][ww]!=-1){
                min = Math.min(min, flag[hh][ww]);
            }
        }
        if(min == 0x3f3f3f3f) return -1;
        for(int i = 0; i < 4; i++){
            int hh = h + dh[i];
            int ww = w + dw[i];
            if(hh>=0 && ww>=0 && ww<width && flag[hh][ww]!=-1 && flag[hh][ww]!=min ){
               int f = flag[hh][ww];
                ArrayList<Pair<Integer, Integer>> list = (ArrayList<Pair<Integer, Integer>>) lists.get(flag[hh][ww]);
               for(Pair<Integer, Integer> pair: list){
                   int hhh = pair.getFirst();
                   int www = pair.getSecond();
                   flag[hhh][www] = min;
                   lists.get(min).add(pair);

               }
               lists.get(f).clear();
                maxWidth[min] = Math.max(maxWidth[min], maxWidth[f]);
                maxHeight[min] = Math.max(maxHeight[min], maxHeight[f]);
                minWidth[min] = Math.min(minWidth[min], minWidth[f]);
                minHeight[min] = Math.min(minHeight[min],minHeight[f] );
            }
        }
        return min;
    }
}
