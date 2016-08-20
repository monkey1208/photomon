package com.example.yang.photomon;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    final int WRITE_PERMISSION = 70;
    final int READ_PERMISSION = 71;
    final int CAMERA = 100;
    final int ALBUM = 101;
    private DisplayMetrics myphone;
    private String photo_path;
    private ImageButton mon;
    private ImageButton camera_button;
    private ImageButton album_button;
    private TextView text_analyze;
    private ImageView image_photo;
    private RelativeLayout back_ground;
    public String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askfor_permission();
        myphone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(myphone);
        mon = (ImageButton) findViewById(R.id.imageButton_mon);
        camera_button = (ImageButton) findViewById(R.id.imageButton_camera);
        album_button = (ImageButton) findViewById(R.id.imageButton_album);
        text_analyze = (TextView) findViewById(R.id.text_analyze);
        image_photo = (ImageView) findViewById(R.id.image_photo);
        back_ground = (RelativeLayout) findViewById(R.id.background);
        status = "egg";
        Drawable icon = getResources().getDrawable(R.drawable.egg);
        ColorFilter filter = new LightingColorFilter(Color.RED, Color.YELLOW);
        icon.setColorFilter(filter);
        //makeanimation();
        mon.setImageDrawable(icon);

        mon.setOnClickListener(mon_click);
        camera_button.setOnClickListener(camera_click);
        album_button.setOnClickListener(album_click);

    }
    private ImageButton.OnClickListener mon_click = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_cycle);
            mon.startAnimation(anim);
        }
    };
    private ImageButton.OnClickListener camera_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String temp_path = Environment.getExternalStorageDirectory().getPath();
            photo_path = temp_path + "/" + "photo.png";
            Uri uri = Uri.fromFile(new File(photo_path));
            System.out.println(uri.getPath());
            Toast.makeText(MainActivity.this, "camera", Toast.LENGTH_LONG).show();
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA);
        }
    };
    private ImageButton.OnClickListener album_click = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, ALBUM);
        }
    };

    private void makeanimation(int id){
        System.out.println("anim");
        AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(id);
        mon.setBackgroundDrawable(ad);
        ad.start();
        //thread = new Thread(anim_thread);
        //thread.start();
    }
    private Runnable anim_thread = new Runnable() {
        @Override
        public void run() {
            AnimationDrawable ad = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_mini);
            mon.setBackgroundDrawable(ad);
            ad.start();

        }
    };
    private void change(int []rgb, int color){
        if(status.equals("egg")) {
            mon.setImageResource(0);

            if (rgb[0] >= rgb[1]+10 && rgb[0] >= rgb[2]) {
                System.out.println("R");
                status = "mini";
                makeanimation(frame_id("mini"));

            } else if (rgb[1] > rgb[0]-10 && rgb[1] >= rgb[2]) {
                System.out.println("G");
                status = "chi";
                makeanimation(frame_id("chi"));

            } else {
                Drawable icon = getResources().getDrawable(R.drawable.egg);
                ColorFilter filter = new LightingColorFilter(color, color);
                icon.setColorFilter(filter);
                mon.setImageDrawable(icon);

            }
        }


    }
    private int frame_id(String input){
        switch (input){
            case "mini":
                return R.drawable.frame_mini;
            case "chi":
                return R.drawable.frame_chi;
        }
        return R.drawable.frame_chi;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA && resultCode == Activity.RESULT_OK){
            Bitmap image;
            FileInputStream fin = null;

            try {
                fin = new FileInputStream(photo_path);
                image = BitmapFactory.decodeStream(fin);
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT);
                Image_process ip = new Image_process();
                int [] rgb = ip.image_analyze(image);
                text_analyze.setText("r:"+rgb[0]+" g:"+rgb[1]+" b:"+rgb[2]+" a:"+rgb[3]);
                //image_photo.setImageBitmap(image);
                image.recycle();
                int color = Color.argb(rgb[3] ,rgb[0], rgb[1], rgb[2]);
                //back_ground.setBackgroundColor(color);
                change(rgb, color);
            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "fail_to_get _photo", Toast.LENGTH_LONG);
                e.printStackTrace();
            }
        }else if(requestCode == ALBUM && data!= null){
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            System.out.println(uri.getPath());
            try {
                Bitmap image = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Bitmap fixed_image;
                Image_process ip = new Image_process();
                if(image.getWidth()>image.getHeight()){
                    fixed_image = ip.ScalePic(image, myphone.heightPixels);
                }else{
                    fixed_image = ip.ScalePic(image,myphone.widthPixels);
                }
                image_photo.setImageBitmap(fixed_image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }



    private void askfor_permission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
            }
        }
    }
}
