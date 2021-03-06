package com.legaultOs.idigram;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Legault on 04/05/2015.
 */
public class Hilo implements Runnable {


    Params p;
    Bitmap aProcesar, r;
    int xIni, xFin, yIni, yFin, cuad;

    ProgressDialog barra;


    public Hilo(Params parametros, Bitmap proc, Bitmap result, int cuadrante, ProgressDialog barraProgreso) {
        this.p = parametros;
        this.aProcesar = proc;
        this.r = result;
        cuad = cuadrante;
        barra = barraProgreso;

        switch (cuadrante) {
            case 1:
                xIni = 0;
                xFin = (parametros.getBm().getWidth() / 2) + 4;
                yIni = 0;
                yFin = (parametros.getBm().getHeight() / 2) + 4;
                break;
            case 2:
                xIni = (parametros.getBm().getWidth() / 2) - 4;
                xFin = parametros.getBm().getWidth();
                yIni = 0;
                yFin = parametros.getBm().getHeight() / 2;
                break;
            case 3:
                xIni = 0;
                xFin = (parametros.getBm().getWidth() / 2) + 4;
                yIni = (parametros.getBm().getHeight() / 2) - 4;
                yFin = parametros.getBm().getHeight();
                break;
            case 4:
                xIni = (parametros.getBm().getWidth() / 2) - 4;
                xFin = parametros.getBm().getWidth();
                yIni = (parametros.getBm().getHeight() / 2) - 4;
                yFin = parametros.getBm().getHeight();
                break;

        }
    }

    @Override
    public void run() {
        ImageFilters imgFilter = new ImageFilters();
        Bitmap result = null;


        if (p.getTipo().equals("sepia")) {
            result = imgFilter.applySepiaToningEffect(aProcesar, p.getValor(), p.getR(), p.getG(), p.getB());
        } else if (p.getTipo().equals("gauss")) {
            result = imgFilter.applyGaussianBlurEffect(aProcesar, p.getValor());
        } else if (p.getTipo().equals("halo")) {
            result = imgFilter.applyGaussianBlurEffect(aProcesar, p.getValor());
        } else if (p.getTipo().equals("inv")) {
            result = imgFilter.applyInvertEffect(aProcesar);
        } else if (p.getTipo().equals("grey")) {
            result = imgFilter.applyGreyscaleEffect(aProcesar);
        } else if (p.getTipo().equals("sharp")) {
            result = imgFilter.applySharpenEffect(aProcesar);
        } else if (p.getTipo().equals("edetect")) {
            result = imgFilter.applyEdgeDetectionEffect(aProcesar);
        } else if (p.getTipo().equals("smooth")) {
            result = imgFilter.applySmoothEffect(aProcesar, p.getValor());
        } else if (p.getTipo().equals("emboss")) {
            result = imgFilter.applyEmbossEffect(aProcesar);
        }
        else if (p.getTipo().equals("filtroColor")) {
            result = imgFilter.applyColorFilterEffect(aProcesar, p.getR(), p.getG(), p.getB());
        } else if (p.getTipo().equals("engrave")) {
            result = imgFilter.applyEngraveEffect(aProcesar);
        } else {
            result = aProcesar;

        }
        if (barra != null) barra.incrementProgressBy(10);

        if (p.getSaturacion() != 10)
            result = imgFilter.applySaturationFilter(result, p.getSaturacion());
        if (p.getContraste() != 1) result = imgFilter.applyContrastEffect(result, p.getContraste());
        if (p.getBrillo() != 1) result = imgFilter.applyBrightnessEffect(result, p.getBrillo());


        if (barra != null) barra.incrementProgressBy(10);


        int pixelColor, A, R, G, B;


        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                pixelColor = result.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                r.setPixel(x, y, Color.argb(A, R, G, B));
            }

        }

        if (barra != null) {
            barra.incrementProgressBy(4);
            // if (barra.getProgress() == barra.getMax()) barra.dismiss();
        }

        //2,4,1,3
        //4,2,3,1

    }
}
