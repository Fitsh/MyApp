package com.example.fitsh.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class DigitalExtraction {
    private static int Size = 600;
    private static int BLACK = 0;

//    public static int getDigital(Bitmap src){
//
//
//        int width = src.getWidth();
//        int height = src.getHeight();
//        Log.i("MAIN","src width="+src.getWidth() +" height="+src.getHeight());
//        int num = 0;
//        int fact = 0;
//        for(int i = 0; i < height; i++){
//            for(int j = 0; j < width; j++){
//                int pixel = src.getPixel(j, i);
//                pixel = pixel&0xff;
//                //System.out.println("i="+i+"   j="+j +" pixel="+pixel);
//                flag[i][j] = -1;
//                if(pixel != BLACK) continue;
//                int mark = num;
//                if((mark=checkIsNew(src,i,j)) != -1){
//                    maxWidth[mark] = Math.max(maxWidth[mark], j);
//                    maxHeight[mark] = Math.max(maxHeight[mark],i );
//                    minWidth[mark] = Math.min(minWidth[mark], j);
//                    minHeight[mark] = Math.min(minHeight[mark],i );
//                    flag[i][j] = mark;
//                    Pair<Integer, Integer> pair = new Pair<>(i,j);
//                    lists.get(mark).add(pair);
//
//                }else{
//                    List<Pair<Integer, Integer>> list = new ArrayList<>();
//                    lists.add(list);
//                    maxWidth[num] = j;
//                    maxHeight[num] = i;
//                    minWidth[num] = j;
//                    minHeight[num] = i;
//                    flag[i][j] = num;
//                    Pair<Integer, Integer> pair = new Pair<>(i,j);
//                    lists.get(num).add(pair);
//                    num++;
//                    System.out.println("+++1");
//                }
//            }
//        }
//        for(int i = 0; i < num; i++){
//            maxHeight[i] = maxHeight[i]-minHeight[i]+1;
//            maxWidth[i] = maxWidth[i] - minWidth[i] + 1;
//        }
//        for(List<Pair<Integer, Integer>> list:lists){
//            if(list.size() !=0) fact++;
//        }
//        System.out.println(num+"dddddddddddddddd");
//        return fact;
//    }
//    private static int checkIsNew(Bitmap src, int h, int w){
//        int[] dh={0 , -1, -1, -1};
//        int[] dw={-1, -1,  0, -1};
//        int width = src.getWidth();
//        int height = src.getHeight();
//        int min = 0x3f3f3f3f;
//        for(int i = 0; i < 4; i++){
//            int hh = h + dh[i];
//            int ww = w + dw[i];
//            if(hh>=0 && ww>=0 && ww<width && flag[hh][ww]!=-1){
//                min = Math.min(min, flag[hh][ww]);
//            }
//        }
//        if(min == 0x3f3f3f3f) return -1;
//        for(int i = 0; i < 4; i++){
//            int hh = h + dh[i];
//            int ww = w + dw[i];
//            if(hh>=0 && ww>=0 && ww<width && flag[hh][ww]!=-1 && flag[hh][ww]!=min ){
//               int f = flag[hh][ww];
//                ArrayList<Pair<Integer, Integer>> list = (ArrayList<Pair<Integer, Integer>>) lists.get(flag[hh][ww]);
//               for(Pair<Integer, Integer> pair: list){
//                   int hhh = pair.getFirst();
//                   int www = pair.getSecond();
//                   flag[hhh][www] = min;
//                   lists.get(min).add(pair);
//
//               }
//               lists.get(f).clear();
//                maxWidth[min] = Math.max(maxWidth[min], maxWidth[f]);
//                maxHeight[min] = Math.max(maxHeight[min], maxHeight[f]);
//                minWidth[min] = Math.min(minWidth[min], minWidth[f]);
//                minHeight[min] = Math.min(minHeight[min],minHeight[f] );
//            }
//        }
//        return min;
//    }
//}

    public static List<Long> getDigital(Context context, Bitmap src) {
        List<List<Pair<Integer, Integer>>> lists = new ArrayList<>();
        int[][] flag = new int[Size][Size];
        int[] maxWidth = new int[Size * Size];
        int[] maxHeight = new int[Size * Size];
        int[] minWidth = new int[Size * Size];
        int[] minHeight = new int[Size * Size];
        boolean[] check = new boolean[Size * Size];
        int[] eachWidth = new int[Size * Size];
        int[] eachHeight = new int[Size * Size];
        int width = src.getWidth();
        int height = src.getHeight();
        Log.i("MAIN", "src width=" + src.getWidth() + " height=" + src.getHeight());
        int num = 0;

        for (int i = 0; i < height; i++) {
            //String s = "";
            for (int j = 0; j < width; j++) {
                flag[i][j] = -1;
                int pixel = src.getPixel(j, i);
                pixel = pixel & 0xFF;
                // s = s + (pixel == BLACK ? 1 : 0) + " ";
            }
            // LogToFile.i("Main" + i, s);
        }
        int[] di = {0, -1, -1, -1, 0, 1, 1, 1};
        int[] dj = {-1, -1, 0, 1, 1, -1, 0, 1};
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = src.getPixel(j, i);
                pixel = pixel & 0xFF;
                if (flag[i][j] != -1 || pixel != BLACK) continue;
                List<Pair<Integer, Integer>> list = new ArrayList<>();
                maxWidth[num] = j;
                maxHeight[num] = i;
                minWidth[num] = j;
                minHeight[num] = i;
                flag[i][j] = num;
                Pair<Integer, Integer> pair = new Pair<>(i, j);
                list.add(pair);
                //   System.out.println("num="+num+" ii="+i+" jj"+j);
                Stack<Pair<Integer, Integer>> stack = new Stack<>();
                stack.push(pair);
                while (!stack.empty()) {
                    Pair<Integer, Integer> pair1 = stack.pop();
                    //   System.out.println(stack.size()+" stack size");
                    for (int k = 0; k < 8; k++) {
                        int ii = pair1.getFirst() + di[k];
                        int jj = pair1.getSecond() + dj[k];
                        if (ii >= 0 && ii < height && jj >= 0 && jj < width && flag[ii][jj] == -1) {
                            pixel = src.getPixel(jj, ii);
                            pixel = pixel & 0xFF;
                            if (pixel != BLACK) continue;
                            flag[ii][jj] = num;
                            maxWidth[num] = Math.max(maxWidth[num], jj);
                            maxHeight[num] = Math.max(maxHeight[num], ii);
                            minWidth[num] = Math.min(minWidth[num], jj);
                            minHeight[num] = Math.min(minHeight[num], ii);
                            Pair<Integer, Integer> pair2 = new Pair<>(ii, jj);
                            list.add(pair2);
                            stack.push(pair2);
                            //     System.out.println("num="+num+" ii="+ii+" jj"+jj);
                            //
                            //     System.out.println(stack.size()+" stack size");
                        }
                    }
                    // System.out.println(stack.size()+" stack size");
                }
                num++;
                lists.add(list);
            }
        }
        int aveHeight = 0;
        int aveWidth = 0;
        int tmp = num;
        int maxw = 0;
        int maxh = 0;
        //  System.out.println("num="+num);
        for(int i = 0; i < num; i++){

            eachHeight[i] = maxHeight[i] - minHeight[i] + 1;
            eachWidth[i] = maxWidth[i] - minWidth[i] + 1;
            maxh = Math.max(maxh, eachHeight[i]);
            maxw = Math.max(maxw, eachWidth[i]);
            // System.out.println("i="+i+" maxWidth="+maxWidth[i] + " minWidth="+minWidth[i]+" eachHeight="+eachHeight[i] + " eachWidth="+eachWidth[i]+"   "+lists.get(i).size());
            if (eachHeight[i] < 5 || eachWidth[i] < 5) {
                tmp--;
                continue;
            }
            aveHeight = aveHeight + eachHeight[i];
            aveWidth = aveWidth + eachWidth[i];

        }
        if (tmp != 0) {
            aveHeight /= tmp;
            aveWidth /= tmp;
        }
        // System.out.println("Max h= "+maxh+" MaxWidth="+maxw);
        // System.out.println("tmp="+tmp+" aveHeight="+aveHeight + " aveWidth="+aveWidth);
        List<Long> ansList = new ArrayList<>();
        for (int i = num - 1; i >= 0; i--) {
            if (eachHeight[i] < 5 || eachWidth[i] < 5) {
                num--;
                continue;
            }
            if (eachWidth[i] < 13 && eachHeight[i] < 13) {
                lists.get(i).clear();
                num--;
                continue;
            }
            if (eachWidth[i] > 20 * aveWidth || eachHeight[i] > 20 * aveHeight) {
                lists.get(i).clear();
                num--;
                continue;
            }
            if (eachWidth[i] < Math.max(aveWidth / 12, 3) || eachHeight[i] < Math.max(aveHeight / 12, 3)) {
                lists.get(i).clear();
                num--;
                continue;
            }
            int max = Math.max(Math.max(eachHeight[i], eachWidth[i]), 26) + 2;
            Bitmap bitmap = Bitmap.createBitmap(max, max, Bitmap.Config.ARGB_8888);
            int gray = BLACK;
            gray = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
            for (int j = 0; j < max; j++) {
                for (int k = 0; k < max; k++) {
                    bitmap.setPixel(k, j, gray);
                }
            }
            maxh = (max - eachHeight[i]) / 2;
            maxw = (max - eachWidth[i]) / 2;
            gray = 255 - BLACK;
            gray = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
            for (Pair<Integer, Integer> pair : lists.get(i)) {
                //     System.out.println("pair.getFirst()"+pair.getFirst() + " minHeight"+minHeight[i]+" maxh="+maxh);
                bitmap.setPixel(maxw + pair.getSecond() - minWidth[i], maxh + pair.getFirst() - minHeight[i], gray);
            }
            bitmap = RoomImage.bilinear(bitmap, 28, 28);
            bitmap = Gray_Scale.getGray(bitmap);
            bitmap = OtsuBinarryFilter.filter(bitmap, null);
            //ExpansionImage expansionImage = new ExpansionImage(bitmap);
            // bitmap = expansionImage.getExpansion();
            System.out.println("digital1 +" + i + "  -------------------");
            RecognitionImage recognitionImage = new RecognitionImage(context);
            ansList.add(recognitionImage.recognition(bitmap));
            System.out.println("digital2 +" + i + "  -------------------");
            for (int j = 0; j < bitmap.getHeight(); j++) {
                String s = "";
                for (int k = 0; k < bitmap.getWidth(); k++) {
                    int p = bitmap.getPixel(k, j);
                    p = p & 0xFF;
                    s = s + (p == 255 ? 1 : 0) + " ";
                }
                LogToFile.i("MAIN" + j, s);
            }
            System.out.println("digital3 +" + i + "  -------------------");

        }
        return ansList;
    }
}