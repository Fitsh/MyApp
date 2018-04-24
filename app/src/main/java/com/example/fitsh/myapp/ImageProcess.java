package com.example.fitsh.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageProcess {
    public static List pre(Context context, Bitmap bitmap) {

        bitmap = RoomImage.bilinear(bitmap, 500, 500);
        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
        bitmap = Gray_Scale.getGray(bitmap);
        RetinexImage retinexImage = new RetinexImage();
        bitmap = retinexImage.retinex_frankle_mccann(bitmap, 6);
        bitmap = Gray_Scale.getGray(bitmap);
        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
        bitmap = OtsuBinarryFilter.filter(bitmap, null);
        //ExpansionImage expansionImage = new ExpansionImage(bitmap);
        //bitmap = expansionImage.getExpansion();
        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
        List<Long> longList = DigitalExtraction.getDigital(context, bitmap);
        return longList;
    }

}
