package com.example.min.medcamera;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


import com.example.min.MedCamera.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static CameraPreview surfaceView;
    private SurfaceHolder holder;
    private static Camera mCamera;
    private int RESULT_PERMISSIONS = 100;
    public static MainActivity getInstance;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    Bitmap bitmapPic = null;

    byte[] data = null;

    Button bt = null;
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestPermissionCamera();
    }

    public void mOnClick(View view){
        mCamera.takePicture(mShutterCallback,mPictureCallback_RAW,mPictureCallback_JPG);
    }

    public static Camera getCamera(){
        return mCamera;
    }

    private void setInit(){
        getInstance = this;

        // 카메라 객체를 R.layout.activity_main의 레이아웃에 선언한 SurfaceView에서 먼저 정의해야 함으로 setContentView 보다 먼저 정의한다.
        mCamera = Camera.open();


        setContentView(R.layout.activity_main);

        bt = (Button)findViewById(R.id.button);
        imageView = (ImageView)findViewById(R.id.imageView);

        surfaceView = (CameraPreview)findViewById(R.id.preview);

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {

                    }
                });
            }
        });

        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public boolean requestPermissionCamera(){
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_PERMISSIONS);

            }else {
                setInit();
            }
        }else{
            setInit();
            return true;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (RESULT_PERMISSIONS == requestCode) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setInit();
            } else {
            }
            return;
        }

    }

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback mPictureCallback_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

        }
    };

    Camera.PictureCallback mPictureCallback_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] bytes, Camera camera) {

            bitmapPic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmapPic = Bitmap.createBitmap(bitmapPic, 0, 0, bitmapPic.getWidth(), bitmapPic.getHeight(), matrix, true);

//            int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getBaseContext().getResources().getDisplayMetrics());
//            int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getBaseContext().getResources().getDisplayMetrics());
//
//            int a = (bitmapPic.getWidth()/2) - (int)(widthPixel*1.4);
//            int b =(bitmapPic.getHeight()/2) - (int)(heightPixel*1.4);
//
//            bitmapPic = Bitmap.createBitmap(bitmapPic, a,b ,(int)(widthPixel*2.8),(int)(heightPixel*2.8));

            Bitmap bitmapCmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            bitmapPic = cropBitmap(bitmapPic, bitmapCmp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapPic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            new SaveImageTask().execute(currentData);


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setInit();
    }

    public static Bitmap cropBitmap(Bitmap src, Bitmap cmpsrc){
        if(src == null)
            return null;
        int width = src.getWidth();
        int height = src.getHeight();

        int w = cmpsrc.getWidth();
        int h = cmpsrc.getHeight();

        if(width < w && height < h)
            return src;

        int x = 0;
        int y = 0;

        if(width > w)
            x = (width-w)/2;

        if(height > h)
            y = (height-h)/2;

        int cw = w; // crop width
        int ch = h; // crop height

        if(w > width)
            cw = width;

        if(h > height)
            ch = height;

        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                refreshGallery(outFile);
                uploadImage(outFile);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
            return null;
        }

    }


    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ///////////////////////////서버 송신단//////////////////////////////////////////////

    public void uploadImage(File file){

        UploadService service = MyRetrofit2.getRetrofit2().create(UploadService.class);

        MultipartBody.Part body1 = prepareFilePart("image", file);

        RequestBody description = createPartFromString("hello, this is description speaking");

        Call<ResponseBody> call = service.uploadFile(description, body1);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String r1 = response.body().string();
                    int idx1 = r1.indexOf("{");
                    int idx2 = r1.indexOf("%");
                    if(idx1<0||idx2<0){
                        call.cancel();
                        setInit();
                    }else {
                        String result = r1.substring(idx1, idx2 - 1);
                        Intent intent = new Intent(getApplicationContext(), ResultView.class);
                        intent.putExtra("response", result);
                        startActivity(intent);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                    Log.d("ErrorX", "onResponse: "+response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
