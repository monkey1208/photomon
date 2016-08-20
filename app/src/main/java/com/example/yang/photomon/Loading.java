package com.example.yang.photomon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Timer timer = new Timer(true);
        timer.schedule(new timertask(), 3000, 2000);
        loading_anim();
    }
    private void loading_anim(){
        /*ImageView img = (ImageView) findViewById(R.id.image_loading_ic);
        View parentView = (View)img.getParent();
        TranslateAnimation anim = new TranslateAnimation(0.0f, 800.0f, 0.0f,0.0f);
        System.out.println("parentwidth="+parentView.getWidth()+" imgwidth="+img.getWidth());
        System.out.println("paddingleft="+parentView.getPaddingLeft()+" paddingright"+parentView.getPaddingRight());
        anim.setDuration(1000);
        anim.setStartOffset(500);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        img.startAnimation(anim);*/
    }
    public class timertask extends TimerTask{

        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(Loading.this, MainActivity.class);
            startActivity(intent);
            this.cancel();
            Loading.this.finish();
        }
    }
}
