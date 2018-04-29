package com.example.fitsh.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageProcess {
    private TextView textView;
    private Context context;

    //    public static List pre(Context context, Bitmap bitmap) {
//
////        bitmap = RoomImage.bilinear(bitmap, 500, 500);
////        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
////        bitmap = Gray_Scale.getGray(bitmap);
////        RetinexImage retinexImage = new RetinexImage();
////        bitmap = retinexImage.retinex_frankle_mccann(bitmap, 6);
////        bitmap = Gray_Scale.getGray(bitmap);
////        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
////        bitmap = OtsuBinarryFilter.filter(bitmap, null);
//        //ExpansionImage expansionImage = new ExpansionImage(bitmap);
//        //bitmap = expansionImage.getExpansion();
//        Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
//        List<Long> longList = DigitalExtraction.getDigital(context, bitmap);
//        textView
//        return longList;
//    }
    public ImageProcess(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    public void process(Bitmap bitmap) {
        new NewAsyncTask().execute(bitmap);
    }

    class NewAsyncTask extends AsyncTask<Bitmap, Void, ArrayList<Long>> {
        @Override
        protected ArrayList<Long> doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            bitmap = RoomImage.bilinear(bitmap, 500, 500);
            Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
            bitmap = Gray_Scale.getGray(bitmap);
            bitmap = AverageProcess.get(bitmap);
            //  RetinexImage retinexImage = new RetinexImage();
            //bitmap = retinexImage.retinex_frankle_mccann(bitmap, 6);
            bitmap = Gray_Scale.getGray(bitmap);
            bitmap = OtsuBinarryFilter.filter(bitmap, null);
            List<Long> longList = DigitalExtraction.getDigital(context, bitmap);

            return (ArrayList<Long>) longList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText("识别中");
        }

        @Override
        protected void onPostExecute(ArrayList<Long> longList) {
            super.onPostExecute(longList);
            String s = "";
            for (long l : longList) {
                s = s + l;
            }
            Log.i("MAIN", "bitmap num= " + longList.toString() + " ++++++++++++++++++++++++++++++++++++++++++++");
            textView.setText(s + "");
        }
    }

}
