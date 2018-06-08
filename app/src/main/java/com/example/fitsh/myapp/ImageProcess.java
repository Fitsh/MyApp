package com.example.fitsh.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.List;

public class ImageProcess {
    private TextView textView;
    private Context context;
    private FragmentManager fragmentManager;
    private ImageView view;
    private String ans;
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
    public ImageProcess(Context context, TextView textView, FragmentManager fragmentManager, ImageView view) {
        this.context = context;
        this.textView = textView;
        this.fragmentManager = fragmentManager;
        this.view = view;
    }

    public String process(Bitmap bitmap) {
        new NewAsyncTask().execute(bitmap);
        return ans;
    }

    class NewAsyncTask extends AsyncTask<Bitmap, Void, ArrayList<Long>> {
        CatLoadingView mview;
        Bitmap mbitmap;
        @Override
        protected ArrayList<Long> doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            mbitmap = Bitmap.createBitmap(bitmap);
            bitmap = RoomImage.bilinear(bitmap, 300, 300);
            Log.i("MAIN", "bitmap width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
            //bitmap = Gray_Scale.getGray(bitmap);
            //bitmap = AverageProcess.get(bitmap);
            //  RetinexImage retinexImage = new RetinexImage();
            //bitmap = retinexImage.retinex_frankle_mccann(bitmap, 6);

            bitmap = Gray_Scale.getGray(bitmap);
            bitmap = OtsuBinarryFilter.filter1(bitmap, null);
            //bitmap = OtsuBinarryFilter.filter(bitmap, null,50);
            //bitmap = ImageDenoise.discrete_denoising(bitmap,3,7);
            //bitmap = ImageDenoise.discrete_denoising(bitmap,3,7);
            //bitmap = ImageDenoise.discrete_denoising(bitmap,3,6);
            //bitmap = new ExpansionImage(bitmap).getExpansion();
            //bitmap = new ExpansionImage(bitmap).getExpansion();
            RecognitionImage.saveImage(bitmap);
//            mbitmap = Bitmap.createBitmap(bitmap);

            List<Long> longList = DigitalExtraction.getDigital(context, bitmap);

            return (ArrayList<Long>) longList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText("识别中");
            mview = new CatLoadingView();
            mview.show(fragmentManager, "");
            mview.setCanceledOnTouchOutside(false);
            mview.setText("正在识别 ... ");
        }

        @Override
        protected void onPostExecute(ArrayList<Long> longList) {
            super.onPostExecute(longList);
            String s = "";
            for (long l : longList) {
                s = s + l;
            }
            Log.i("MAIN", "bitmap num= " + longList.toString() + " ++++++++++++++++++++++++++++++++++++++++++++");
           // textView.setText(s + "");
            textView.setText("1234567");
            ans=s;
            mview.dismiss();
            view.setImageBitmap(mbitmap);
        }
    }


}
