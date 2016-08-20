package com.example.yang.photomon;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * Created by Yang on 2016/8/19.
 */
public class Image_process {
    public int [] image_analyze(Bitmap bitmap){
        int width, height;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        int [] pix = new int[width*height];
        bitmap.getPixels(pix,0,width,0,0,width,height);
        int r,b,g,a;
        long r_sum, b_sum, g_sum, a_sum;
        long count = 0;
        r_sum = b_sum = g_sum = a_sum = 0;
        for(int i=0; i<width*height;i++){
            r = Color.red(pix[i]);
            g = Color.green(pix[i]);
            b = Color.blue(pix[i]);
            a = Color.alpha(pix[i]);
            if(r!=0 && g!=0 && b!=0 && a!=0){
                r_sum += r;
                g_sum += g;
                b_sum += b;
                a_sum += a;
                count++;
            }
        }
        int [] rgba = new int[4];
        rgba[0] = (int)(r_sum/count);
        rgba[1] = (int)(g_sum/count);
        rgba[2] = (int)(b_sum/count);
        rgba[3] = (int)(a_sum/count);
        return rgba;
    }
    public Bitmap ScalePic(Bitmap bitmap,int phone)
    {
        float mScale = 1;//比例

        //圖片寬度大於手機寬度進行縮放，否則直接放入ImageView
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            return mScaleBitmap;
        }
        else{
            return bitmap;
        }
    }
}
