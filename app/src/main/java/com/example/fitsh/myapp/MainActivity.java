package com.example.fitsh.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrinterId;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private static final int NONE = 0;
    private static final int FLAG_TAKE_PHOTO = 1; // take picture
    private static final int FLAG_CHOOSE_IMG = 2; //
    private static final int REQUEST_CODE_CROP = 3;
    private static final int REQUEST_CODE_PERMISSION = 12;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String change_path = "/MyApp/Image";
    private Uri photoUri;
    private ImageView imageView=null;
    private TextView textView = null;
    private List<String> needPermission;
    private String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
           // Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        imageView=findViewById(R.id.pic);
        textView = findViewById(R.id.textView);
        askMultiPermission();
        LogToFile.init(this);
    }
    public int askMultiPermission(){
        needPermission = new ArrayList<>();
        for(String permission: permissionArray){
            if(ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED)
                needPermission.add(permission);
        }
        if(!needPermission.isEmpty()){
            ActivityCompat.requestPermissions(MainActivity.this, needPermission.toArray(new String[(needPermission.size())]),REQUEST_CODE_PERMISSION );
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_PERMISSION:
                Map<String, Integer> permissionMap = new HashMap<>();
                for(String name: needPermission){
                    permissionMap.put(name, PackageManager.PERMISSION_GRANTED);
                }
                for(int i = 0; i < permissions.length; i++){
                    permissionMap.put(permissions[i],grantResults[i] );
                }
                if (!checkIsAskPermissionState(permissionMap, permissions)) {
                    Toast.makeText(MainActivity.this, "权限获取不完成，可能有的功能不能使用", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public  boolean checkIsAskPermissionState(Map<String, Integer> maps, String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (maps.get(list[i]) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("failed + "+ list[i].toString()+"+++++++++++++++++++++++++++++++++");
                return false;
            }
        }
        return true;
    }
    public void doClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                Log.i("Main", "button 1 ..................................................................................................");
                startActivityForResult(intent, FLAG_CHOOSE_IMG);
                break;
            case R.id.bt2:
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                    File dir = new File(Environment.getExternalStorageDirectory()+"/MyApp/Images");
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    File file = new File(dir, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg");
                    photoUri = Uri.parse(file.getPath());
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(MainActivity.this, file));
                    startActivityForResult(intent1, FLAG_TAKE_PHOTO);
                } else{
                    Toast.makeText(MainActivity.this, "请检查SDCard！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Main", "requestCode"+requestCode+"resultCode"+resultCode+" ..............................................................");
        Uri uri = null;
        String path = "";
        Cursor cursor = null;
        Log.i("Main", "requestCode"+requestCode+"resultCode"+resultCode+" ..............................................................");
        if(requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK){
            Log.i("Main", "requestCode"+requestCode+"resultCode"+resultCode+" ......................");
          if(data != null && data.getData() != null){
              Log.i("Main", " not null ..............................................................");
              uri = data.getData();
              if( !TextUtils.isEmpty(uri.getAuthority())){
                  Log.i("Main", "First ..............................................................");
                  String[] proj = {MediaStore.Images.Media.DATA};
                  cursor = managedQuery(uri, proj, null, null, null);
                  Log.i("Main", "cursor ..............................................................");
                  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                  Log.i("Main", column_index+" ..............................................................");
                  cursor.moveToFirst();
                  Log.i("Main", "xixi ..............................................................");
                  path = cursor.getString(column_index);
                  Log.i("Main", path+"path ..............................................................");
              }
              else{
                  Log.i("Main", "path ..............................................................");
                  path = uri.getPath();
                  Log.i("Main", path+"path ..............................................................");
              }
              cropPic(path);
//              Bitmap photo = BitmapFactory.decodeFile(path);
//              //ByteArrayOutputStream stream = new ByteArrayOutputStream();
//              //photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
//              imageView.setImageBitmap(photo);
//              RecognitionImage recognitionImage = new RecognitionImage(MainActivity.this);
//              long text = recognitionImage.recognition(photo);
//              Log.i("Main", "Text"+text+" ......................");
//              textView.setText(text+"");
          }

      }
      else if(requestCode ==FLAG_TAKE_PHOTO && resultCode == RESULT_OK){
            if(photoUri != null)
                path = photoUri.toString();
            cropPic(path);
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
//            bitmap = Gray_Scale.getGray(bitmap);
//            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
////            bitmap = OtsuBinarryFilter.filter(bitmap, null);
////            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
//            imageView.setImageBitmap(bitmap);
//            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
        }
        else if(requestCode ==REQUEST_CODE_CROP ){
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                imageView.setImageBitmap(photo);
//            }
            path = Environment.getExternalStorageDirectory()+"/MyApp/Cache/temp.jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap photo = bitmap;
            bitmap = RoomImage.bilinear(bitmap, 600, 600);
            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
            bitmap = Gray_Scale.getGray(bitmap);
            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
            bitmap = OtsuBinarryFilter.filter(bitmap, null);
            Log.i("MAIN","bitmap width="+bitmap.getWidth() +" height="+bitmap.getHeight());
            imageView.setImageBitmap(bitmap);
            List<Long> longList = DigitalExtraction.getDigital(MainActivity.this, bitmap);
            Log.i("MAIN", "bitmap num= " + longList.toString() + " ++++++++++++++++++++++++++++++++++++++++++++");
            textView.setText(longList.toString() + "");
        }
    }
    public static Uri getUriForFile(Context context, File file){
        if (context == null || file == null) {//简单地拦截一下
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, "com.example.fitsh.myapp", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    protected void cropPic(String path){
        Log.i("Main", "CropPic"+" ......................");
        Uri uri=null;
        File file = new File(path);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//重要的，，，
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            uri = FileProvider.getUriForFile(MainActivity.this, "com.example.fitsh.myapp", file);
        }else{
            uri = Uri.fromFile(file);
        }
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        // intent.putExtra("outputX", 60);// 输出图片大小
        //intent.putExtra("outputY", 60);
        //裁剪时是否保留图片的比例，这里的比例是1:1
        intent.putExtra("scale", true);
        //是否是圆形裁剪区域，设置了也不一定有效
        intent.putExtra("circleCrop", true);

        String cachePath = Environment.getExternalStorageDirectory()+"/MyApp/Cache";
        File parentPath = new File(cachePath);
        if(!parentPath.exists())
            parentPath.mkdirs();
        File cacheFile = new File(parentPath,"temp.jpg");
        if(!cacheFile.exists()){
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String cacheFilePath = cacheFile.getAbsolutePath();
        Uri uriPath = Uri.parse("file://" +cacheFilePath);
        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        //设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否将数据保留在Bitmap中返回
        //注意：此处应设置return-data为false，如果设置为true，是直接返回bitmap格式的数据，耗费内存。设置为false，然后，设置裁剪完之后保存的路径，即：intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        //intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        Log.i("Main", "intent"+" ......................");
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }
    //    public void startPhotoZoom(Uri uri){
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 500);
//        intent.putExtra("return-data", true);
//        Log.i("Main", "startPhotoZoom ............................................................");
//        startActivityForResult(intent, PHOTO_RESOULT);
//
//    }

}
