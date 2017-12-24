package com.example.imagerecognition;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import Util.FileUtil;
import Util.PicUtil;

public class CameraActivity extends Activity {


    private String tag = "CameraActivity";
    private SurfaceView surfaceView;
    //Surface的控制器
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button saveButton;
    //拍照的回调接口
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SavePictureTask().execute(data);
            camera.startPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initViews();
    }

    private void initViews() {
        saveButton = (Button) findViewById(R.id.camera_save);
        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        surfaceHolder = surfaceView.getHolder();
        // 给SurfaceView当前的持有者  SurfaceHolder 一个回调对象。
        //用户可以实现此接口接收surface变化的消息。当用在一个SurfaceView 中时，
        //它只在SurfaceHolder.Callback.surfaceCreated()和SurfaceHolder.Callback.surfaceDestroyed()之间有效。
        //设置Callback的方法是SurfaceHolder.addCallback.
        //实现过程一般继承SurfaceView并实现SurfaceHolder.Callback接口
        surfaceHolder.addCallback(surfaceCallback);
        // 设置surface不需要自己的维护缓存区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
//              Camera.takePicture(shutterCallback,rawCallback,pictureCallback );
//                private ShutterCallback shutterCallback = new ShutterCallback(){
//                          public void onShutter(){
//                            /* 按快门瞬间会执行这里的代码 */
//                          }
//                        };
//                private PictureCallback rawCallback = new PictureCallback(){
//                          public void onPictureTaken(byte[] _data, Camera _camera){
//                            /* 如需要处理 raw 则在这里 写代码 */
//                          }
//                        };
//                   //当拍照后 存储JPG文件到 sd卡
//                  PictureCallback pictureCallback=new PictureCallback(){
//                      public void onPictureTaken(byte[] data,Camera camera) {
//                          FileOutputStream outSteam=null;
//                          try{
//                              SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
//                              String times=format.format((new Date()));
//                              outSteam=new FileOutputStream("/sdcard/"+times+"mhc.jpg");
//                              outSteam.write(data);
//                              outSteam.close();
//                          }catch(FileNotFoundException e){
//                              Log.d("Camera", "row");
//                          } catch (IOException e) {
//                              e.printStackTrace();
//                          }
//                      };
//                  };
            }
        });
    }


    //SurfaceView当前的持有者的回调接口的实现
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();
            Log.e(tag, "摄像头Open完成");
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                camera.release();
                camera = null;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    };

    class SavePictureTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... params) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            String date = simpleDateFormat.format(new Date());
            /*File picture = null;
            try {
                if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    File sdDir = Environment.getExternalStorageDirectory();
                    String path = sdDir.getPath() + "/TestPaper";
                    isExist(path);
                    picture = new File(path, date);
                    FileOutputStream fos = new FileOutputStream(picture.getPath());
                    fos.write(params[0]);

                    fos.close();
                } else {
                    *//*FileOutputStream outStream =getApplicationContext().openFileOutput(date + ".jpg", Context.MODE_WORLD_READABLE |Context.MODE_WORLD_WRITEABLE ); //模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
                    outStream.write(params[0]);
                    outStream.flush();
                    outStream.close();*//*
                    Log.e(tag,getApplicationContext().fileList().toString());
                    File file=new File(getApplicationContext().getFilesDir(),date );
                    FileOutputStream fos=new FileOutputStream(file);
                    fos.write(params[0]);
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            Bitmap bitmap=BitmapFactory.decodeByteArray(params[0],0,params[0].length);
            Matrix m = new Matrix();
            m.setRotate(90,(float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            final Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            String url = PicUtil.saveBitmapToSDCard(date,bm);
            Log.e(tag, "url"+url);
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{FileUtil.getRealFilePath(getApplicationContext(), Uri.parse(url))}, null, null);

            Log.e(tag, "照片保存完成");
            CameraActivity.this.finish();
            return null;
        }
    }

    public static void isExist(String path) {
        File file = new File(path);
//判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }
}