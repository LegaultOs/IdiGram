package com.legaultOs.idigram;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Legault on 04/05/2015.
 */
public class PreparaBitmap {

    Bitmap bm, bm1, bm2, bm3, bm4;

    public PreparaBitmap(Bitmap bm) {
        this.bm = bm;
        divideBm();

    }

    public PreparaBitmap(Bitmap bm, Bitmap bm1, Bitmap bm2, Bitmap bm3, Bitmap bm4) {
        this.bm1 = bm1;
        this.bm2 = bm2;
        this.bm3 = bm3;
        this.bm4 = bm4;
        this.bm = bm;
    }

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

    public Bitmap joinBm() {

        int ancho = bm.getWidth();
        int alto = bm.getHeight();
        int ancho1 = ancho / 2;
        int alto1 = alto / 2;
        int pixelColor, A, R, G, B;
        int xIni, xFin, yIni, yFin;
        Bitmap resultado = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);

        xIni = (bm.getWidth() / 2) - 4;
        xFin = bm.getWidth();
        yIni = (bm.getHeight() / 2) - 4;
        yFin = bm.getHeight();

        for (int x = xIni; x < xFin; x++) {
            for (int y = yIni; y < yFin; y++) {
                pixelColor = bm4.getPixel(x - xIni, y - yIni);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        xIni = (bm.getWidth() / 2) - 4;
        xFin = bm.getWidth();
        yIni = 0;
        yFin = bm.getHeight() / 2;

        for (int x = xIni; x < xFin; x++) {
            for (int y = yIni; y < yFin; y++) {
                pixelColor = bm2.getPixel(x - xIni, y - yIni);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        xIni = 0;
        xFin = (bm.getWidth() / 2) + 4;
        yIni = (bm.getHeight() / 2) - 4;
        yFin = bm.getHeight();

        for (int x = xIni; x < xFin; x++) {
            for (int y = yIni; y < yFin; y++) {
                pixelColor = bm3.getPixel(x - xIni, y - yIni);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        xIni = 0;
        xFin = (bm.getWidth() / 2) + 4;
        yIni = 0;
        yFin = (bm.getHeight() / 2) + 4;

        for (int x = xIni; x < xFin; x++) {
            for (int y = yIni; y < yFin; y++) {
                pixelColor = bm1.getPixel(x - xIni, y - yIni);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                resultado.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }


        return resultado;

    }


    private void divideBm() {
        int ancho = bm.getWidth();
        int alto = bm.getHeight();
        int ancho1 = ancho / 2;
        int alto1 = alto / 2;
        int pixelColor, A, R, G, B;
        bm1 = Bitmap.createBitmap(ancho1 + 5, alto1 + 5, Bitmap.Config.ARGB_8888);
        bm2 = Bitmap.createBitmap(ancho - ancho1 + 5, alto1 + 5, Bitmap.Config.ARGB_8888);
        bm3 = Bitmap.createBitmap(ancho1 + 5, alto - alto1 + 5, Bitmap.Config.ARGB_8888);
        bm4 = Bitmap.createBitmap(ancho - ancho1 + 5, alto - alto1 + 5, Bitmap.Config.ARGB_8888);


        for (int i = 0; i <= ancho1 + 4; i++) {
            for (int j = 0; j <= alto1 + 4; j++) {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm1.setPixel(i, j, Color.argb(A, R, G, B));

            }

        }

        for (int i = ancho1 - 4; i < ancho; i++) {
            for (int j = 0; j <= alto1 + 4; j++) {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm2.setPixel(i - ancho1 + 4, j, Color.argb(A, R, G, B));


            }

        }

        for (int i = 0; i <= ancho1 + 4; i++) {
            for (int j = alto1 - 4; j < alto; j++) {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm3.setPixel(i, j - alto1 + 4, Color.argb(A, R, G, B));

            }

        }

        for (int i = ancho1 - 4; i < ancho; i++) {
            for (int j = alto1 - 4; j < alto; j++) {
                pixelColor = bm.getPixel(i, j);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                bm4.setPixel(i - ancho1 + 4, j - alto1 + 4, Color.argb(A, R, G, B));

            }

        }

    }
}
