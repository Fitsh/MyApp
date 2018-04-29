package com.example.fitsh.myapp;

import android.graphics.Bitmap;

public class ExpansionImage {
    private int WHITE = 255;
    private int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
    private int[] dy = {-1, -1, 0, 1, 1, -1, 0, 1};
    private Bitmap src;
    private int width;
    private int height;

    public ExpansionImage() {
    }

    public ExpansionImage(Bitmap src) {
        this.src = src;
        this.width = src.getWidth();
        this.height = src.getHeight();
    }

    public Bitmap getExpansion() {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap dest = Bitmap.createBitmap(src, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = src.getPixel(j, i);
                pixel = pixel & 0xFF;
            }
        }

        for (int i = 0; i < height; i++) {
            String s = "";
            for (int j = 0; j < width; j++) {
                int pixel;
                if (hasWhiteInWindow(i, j)) {
                    pixel = src.getPixel(j, i);
                    int alpha = (pixel >> 24) & 0xFF;
                    pixel = (alpha << 24) | (WHITE << 16) | (WHITE << 8) | WHITE;
                    dest.setPixel(j, i, pixel);
                } else {
                    pixel = src.getPixel(j, i);
                    int alpha = (pixel >> 24) & 0xFF;
                    pixel = 255 - WHITE;
                    pixel = (alpha << 24) | (pixel << 16) | (pixel << 8) | pixel;
                    dest.setPixel(j, i, pixel);
                }
                s = s + (pixel == WHITE ? 1 : 0) + " ";
            }
            LogToFile.i("Main" + i, s);
        }
        return dest;
    }

    private boolean hasWhiteInWindow(int i, int j) {
        for (int k = 0; k < 8; k++) {
            int tx = dx[k] + i;
            int ty = dy[k] + j;
            if (!inRange(tx, ty)) continue;
            int pixel = src.getPixel(ty, tx);
            pixel = pixel & 0xFF;
            if (pixel == WHITE)
                return true;
        }
        return false;
    }

    private boolean inRange(int i, int j) {
        if (i >= 0 && i < height && j >= 0 && j < width)
            return true;
        return false;
    }
}
