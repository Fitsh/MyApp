package com.example.fitsh.myapp;

import android.graphics.Bitmap;

public class AverageProcess {
    public static Bitmap get(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
        int[] dy = {-1, -1, 0, 1, 1, -1, 0, 1};
        Bitmap dest = Bitmap.createBitmap(src, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int sum = src.getPixel(j, i);
                if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                    dest.setPixel(j, i, sum);
                    continue;
                }
                sum = sum & 0xFF;
                for (int k = 0; k < 8; k++) {
                    int tx = i + dx[k];
                    int ty = j + dy[k];
                    int pixel = src.getPixel(ty, tx);
                    int alpha = (pixel >> 24) & 0xFF;
                    pixel = pixel & 0xFF;
                    pixel = (alpha << 24) | (pixel << 16) | (pixel << 8) | pixel;
                    dest.setPixel(ty, tx, pixel);
                }
            }
        }
        return dest;
    }
}
