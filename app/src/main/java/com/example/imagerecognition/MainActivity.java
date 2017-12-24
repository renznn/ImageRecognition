package com.example.imagerecognition;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Util.HttpCallback;
import Util.URL;

public class MainActivity extends AppCompatActivity implements HttpCallback {
    private Button TakePhoto;
    private Button Scanner;
    private ImageView photo;
    private Button Album;
    private static final int IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //x.view().inject(this);
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(true);
        init();
    }

    /*初始化*/
    void init() {
        TakePhoto = (Button) findViewById(R.id.take);
        Scanner = (Button) findViewById(R.id.scanner);
        photo = (ImageView) findViewById(R.id.photo);
        Album = (Button) findViewById(R.id.album);

        TakePhoto.setOnClickListener(new View.OnClickListener() {//打开相机
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        Album.setOnClickListener(new View.OnClickListener() {//打开相册
            @Override
            public void onClick(View v) {
                OpenAlbum();
            }
        });
    }

    //回去intent返回参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Toast.makeText(this, imagePath, Toast.LENGTH_LONG);
            Log.e("图片", imagePath);
            c.close();
            UploadImage(imagePath);
        }
    }

    //打开相册
    public void OpenAlbum() {
        /*Intent i = new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*//**//*");*/
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMAGE_CODE);
       /* RequestParams requestParams = new RequestParams(URL.test);
        URL.PostData(requestParams, this);*/
    }

    public void UploadImage(String path){
        //现在是单图片测试，以后要改成多图
        List<String> list =new ArrayList<>();
        list.add(path);
        RequestParams requestParams = new RequestParams(URL.UpLoadImageUrl);
        try {
            for (String p : list) {
                Bitmap bitmap = BitmapFactory.decodeFile(p);
                photo.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();
                byte[] encode = Base64.encode(datas, Base64.DEFAULT);
                String photo = new String(encode);
                Log.e("图片", photo);
                requestParams.addBodyParameter("image", photo);
                requestParams.addBodyParameter("name", p.substring(p.lastIndexOf("/")+1,p.lastIndexOf(".")));
                baos.flush();
                baos.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        URL.PostData(requestParams, this);
    }


    /*网络请求成功*/
    @Override
    public void onSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    /*网络请求失败*/
    @Override
    public void onError(Throwable ex) {
        /*Log.e("网络", ex.toString());*/
        System.out.print(ex);
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /*网络请求取消*/
    @Override
    public void onCancelled(Callback.CancelledException cex) {
    }

    /*网络请求完成*/
    @Override
    public void onFinished() {
    }
}