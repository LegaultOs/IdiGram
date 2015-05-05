package com.legaultOs.idigram;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Legault on 04/05/2015.
 */
public class PreparaBitmap {

    Bitmap bm,bm1,bm2,bm3,bm4;

    public Bitmap getBm1() {
        return bm1;
    }

    public Bitmap getBm2() {
        return bm2;
    }

    public Bitmap getBm3() {
        return bm3;
    }

    public Bitmap getBm4() {
        return bm4;
    }


    public PreparaBitmap(Bitmap bm) {
        this.bm = bm;
        divideBm();

    }

    public Bitmap joinBm(){

        int ancho= bm.getWidth();
        int alto= bm.getHeight();
        int ancho1= ancho/2;
        int alto1= alto/2;
        int pixelColor,A,R,G,B;
        Bitmap resultado = Bitmap.createBitmap(bm1.getWidth()+bm2.getWidth(),bm1.getHeight()+bm3.getHeight(),Bitmap.Config.ARGB_8888);
//1
        for(int i=0 ; i<ancho1 ; i++)
        {
            for(int j=0 ; j<alto1 ; j++)
            {
                pixelColor = bm1.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(i,j,Color.argb(A, R, G, B));

            }

        }
//2
        for(int i=ancho1 ; i<ancho ; i++)
        {
            for(int j=0 ; j<alto1 ; j++)
            {
                pixelColor = bm2.getPixel(i-ancho1, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(i,j,Color.argb(A, R, G, B));


            }

        }
//3
        for(int i=0 ; i<ancho1 ; i++)
        {
            for(int j=alto1 ; j<alto ; j++)
            {
                pixelColor = bm3.getPixel(i, j-alto1);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(i,j,Color.argb(A, R, G, B));

            }

        }
//4
        for(int i=ancho1 ; i<ancho ; i++)
        {
            for(int j=alto1 ; j<alto ; j++)
            {
                pixelColor = bm4.getPixel(i-ancho1, j-alto1);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(i,j,Color.argb(A, R, G, B));

            }

        }



        return resultado;

    }


    private void divideBm()
    {
        int ancho= bm.getWidth();
        int alto= bm.getHeight();
        int ancho1= ancho/2;
        int alto1= alto/2;
        int pixelColor,A,R,G,B;
        bm1= Bitmap.createBitmap(ancho1+5, alto1+5, Bitmap.Config.ARGB_8888);
        bm2=Bitmap.createBitmap(ancho-ancho1+5,alto1+5,Bitmap.Config.ARGB_8888);
        bm3=Bitmap.createBitmap(ancho1+5,alto-alto1+5,Bitmap.Config.ARGB_8888);
        bm4=Bitmap.createBitmap(ancho-ancho1+5,alto-alto1+5,Bitmap.Config.ARGB_8888);


        for(int i=0 ; i<=ancho1+4 ; i++)
        {
            for(int j=0 ; j<=alto1+4 ; j++)
            {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm1.setPixel(i,j,Color.argb(A, R, G, B));

            }

        }

        for(int i=ancho1-4 ; i<ancho ; i++)
        {
            for(int j=0 ; j<=alto1+4 ; j++)
            {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm2.setPixel(i-ancho1+4,j,Color.argb(A, R, G, B));


            }

        }

        for(int i=0 ; i<=ancho1+4 ; i++)
        {
            for(int j=alto1-4 ; j<alto ; j++)
            {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm3.setPixel(i,j-alto1+4,Color.argb(A, R, G, B));

            }

        }

        for(int i=ancho1-4 ; i<ancho ; i++)
        {
            for(int j=alto1-4 ; j<alto ; j++)
            {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm4.setPixel(i-ancho1+4,j-alto1+4,Color.argb(A, R, G, B));

            }

        }

    }
}
