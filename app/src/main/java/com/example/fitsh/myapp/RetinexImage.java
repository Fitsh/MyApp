package com.example.fitsh.myapp;

import android.graphics.Bitmap;

public class RetinexImage {
    private int[][] OP, RR, IP, NP;
    private int width;
    private int height;
    private int max;

    public Bitmap retinex_frankle_mccann(Bitmap src, int nIteration) {
        max = 0;
        width = src.getWidth();
        height = src.getHeight();
        OP = new int[height][width];
        IP = new int[height][width];
        RR = new int[height][width];
        NP = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = src.getPixel(j, i);
                pixel = pixel & 0xFF;
                RR[i][j] = pixel;
                max = Math.max(pixel, max);
            }
        }

        int shift = (int) Math.pow(2, (double) (Math.floor(Math.log(Math.min(width, height)) / Math.log(2.0)) - 1));

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                OP[i][j] = 1;
        while (Math.abs(shift) >= 1) {
            for (int i = 0; i < nIteration; i++) {
                CompareWith(0, shift);
                CompareWith(shift, 0);
            }
            shift = -shift / 2;
        }
        Bitmap dest = Bitmap.createBitmap(src, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int alpha = src.getPixel(j, i);
                alpha = (alpha >> 24) & 0xFF;
                int pixel = NP[i][j];
                pixel = (alpha << 24) | (pixel << 16) | (pixel << 8) | pixel;
                dest.setPixel(j, i, pixel);
            }
        }
        return dest;
    }

    private void CompareWith(int s_width, int s_height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                IP[i][j] = OP[i][j];
            }
        }
        if (s_width + s_height > 0) {
            for (int i = s_height; i < height; i++) {
                for (int j = s_width; j < width; j++) {
                    IP[i][j] = OP[i - s_height][j - s_width] + RR[i][j] - RR[i - s_height][j - s_width];
                }
            }
        } else {
            for (int i = 0; i < height + s_height; i++) {
                for (int j = 0; j < width + s_width; j++) {
                    IP[i][j] = OP[i - s_height][j - s_width] + RR[i][j] - RR[i - s_height][j - s_width];
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (IP[i][j] > max) IP[i][j] = max;
                NP[i][j] = (IP[i][j] + OP[i][j]) / 2;
                OP[i][j] = NP[i][j];
            }
        }
    }
}
