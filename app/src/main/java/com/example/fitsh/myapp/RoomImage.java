package com.example.fitsh.myapp;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RoomImage {
    public static Bitmap bilinear(Bitmap src, int dheight, int dwidth) {
        int width = src.getWidth();
        int height = src.getHeight();
        float xRatio = (float) width / dwidth;
        float yRatio = (float) height / dheight;
        Matrix matrix = new Matrix();
        matrix.postScale(1 / xRatio, 1 / yRatio);
        Bitmap dest = Bitmap.createBitmap(dwidth, dheight, src.getConfig());
        for (int i = 0; i < dheight; i++) {
            float srcY = i * yRatio;
            int IntY = (int) srcY;
            float v = srcY - IntY;
            float v1 = (float) 1.0 - v;
            for (int j = 0; j < dwidth; j++) {
                float srcX = j * xRatio;//源图像“虚”坐标的x值
                int IntX = (int) srcX;//向下取整
                float u = srcX - IntX;//获取小数部分
                float u1 = (float) 1.0 - u;

                int pixel00 = src.getPixel(IntX, IntY);
                int pixel10;
                if (IntY < height - 1) {
                    pixel10 = src.getPixel(IntX, IntY + 1);
                } else {
                    pixel10 = pixel00;
                }
                int pixel01;
                int pixel11;
                if (IntX < width - 1) {
                    pixel01 = src.getPixel(IntX + 1, IntY);
                } else {
                    pixel01 = pixel00;
                }
                if (IntX < width - 1 && IntY < height - 1) {
                    pixel11 = src.getPixel(IntX + 1, IntY + 1);
                } else if (IntX < width - 1) {
                    pixel11 = pixel01;
                } else {
                    pixel11 = pixel10;
                }
                int r00, r01, r10, r11;
                int g00, g01, g10, g11;
                int b00, b01, b10, b11;
                int a00, a01, a10, a11;
                a00 = (pixel00 >> 24) & 0xFF;
                r00 = (pixel00 >> 16) & 0xFF;
                g00 = (pixel00 >> 8) & 0xFF;
                b00 = pixel00 & 0xFF;
                a01 = (pixel01 >> 24) & 0xFF;
                r01 = (pixel01 >> 16) & 0xFF;
                g01 = (pixel01 >> 8) & 0xFF;
                b01 = pixel01 & 0xFF;
                a10 = (pixel10 >> 24) & 0xFF;
                r10 = (pixel10 >> 16) & 0xFF;
                g10 = (pixel10 >> 8) & 0xFF;
                b10 = pixel10 & 0xFF;
                a11 = (pixel11 >> 24) & 0xFF;
                r11 = (pixel11 >> 16) & 0xFF;
                g11 = (pixel11 >> 8) & 0xFF;
                b11 = pixel11 & 0xFF;
                int r = (int) (v1 * (u * r01 + u1 * r00) +
                        v * (u * r11 + u1 * r10));
                int g = (int) (v1 * (u * g01 + u1 * g00) +
                        v * (u * g11 + u1 * g10));
                int b = (int) (v1 * (u * b01 + u1 * b00) +
                        v * (u * b11 + u1 * b10));
                int a = (int) (v1 * (u * a01 + u1 * a00) +
                        v * (u * a11 + u1 * a10));
                int pixel = (a << 24) | (r << 16) | (g << 8) | b;
                dest.setPixel(j, i, pixel);
            }

        }
        return dest;
    }
}
