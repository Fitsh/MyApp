package com.example.fitsh.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Trace;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class RecognitionImage {

    {
        System.loadLibrary("tensorflow_inference");
    }

    private static final String MODEL_FILE = "file:///android_asset/mnist.pb";
    private static final String INPUT_NODE = "input";
    private static final String OUTPUT_NODE = "output";

    private static final int HEIGHT = 28;
    private static final int WIDTH = 28;
    private static final int CHANNEL = 1;
    private float[] inputs ;
    private long[] outputs = new long[10];
    TensorFlowInferenceInterface inferenceInterface;

    public RecognitionImage(Context context) {
        Log.i("MAIN","asafasfaaaaaaaaaaaaaa");
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(), MODEL_FILE);
    }

    public long recognitionSingle(float[] img){
        System.out.println(img.length+"length");
        System.out.println(img[10]);
        inputs = img;
        Trace.beginSection("feed");
        inferenceInterface.feed(INPUT_NODE, inputs,1,784);
        Trace.endSection();
        Trace.beginSection("run");
        inferenceInterface.run(new String[]{OUTPUT_NODE});
        Trace.endSection();
        Trace.beginSection("fetch");
        inferenceInterface.fetch(OUTPUT_NODE, outputs);
        Trace.endSection();
        for(long i:outputs)
            System.out.println(i);
        System.out.println("output"+outputs[0]);
        return outputs[0];
    }
    public long recognition(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width : "+width);
        System.out.println("height"+height);
        int by = bitmap.getPixel(0, 0);
        int[] pixels = new int[width * height];
        float[] fPixels = new float[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for(int i = 0; i < pixels.length; i++){
            fPixels[i] = (float)(pixels[i]&0xff);
            fPixels[i] = (float) (fPixels[i]*1.0/256.0);
        }
        System.out.println("pixels"+pixels[10]);
        System.out.println("fPixels"+fPixels[10]);
        return recognitionSingle(fPixels);
    }
}
