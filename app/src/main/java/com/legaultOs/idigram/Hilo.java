package com.legaultOs.idigram;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Legault on 04/05/2015.
 */
public class Hilo implements Runnable {


   Params p;
   Bitmap aProcesar,r;
   int xIni,xFin,yIni,yFin,cuad;
   SecuenciaHilos ord;

    public Hilo(Params parametros,Bitmap proc,Bitmap result,int cuadrante,SecuenciaHilos orden) {
        this.p = parametros;
        this.aProcesar = proc;
        this.r=result;
        cuad=cuadrante;
        ord=orden;
        switch (cuadrante)
        {
            case 1: xIni=0;xFin=(parametros.getBm().getWidth()/2)+4;yIni=0;yFin=(parametros.getBm().getHeight()/2)+4;
                    break;
            case 2: xIni=(parametros.getBm().getWidth()/2)-4;xFin=parametros.getBm().getWidth();yIni=0;yFin=parametros.getBm().getHeight()/2;
                    break;
            case 3: xIni=0;xFin=(parametros.getBm().getWidth()/2)+4;yIni=(parametros.getBm().getHeight()/2)-4;yFin=parametros.getBm().getHeight();
                     break;
            case 4: xIni=(parametros.getBm().getWidth()/2)-4;xFin=parametros.getBm().getWidth();yIni=(parametros.getBm().getHeight()/2)-4;yFin=parametros.getBm().getHeight();
                     break;

        }
    }

    @Override
    public void run() {
        ImageFilters imgFilter = new ImageFilters();
        Bitmap result=null;
        switch (p.getTipo()) {

            case "sepia":
                result = imgFilter.applySepiaToningEffect(aProcesar, p.getValor(),p.getR(),p.getG(),p.getB());
                break;
            case "gauss":
                result = imgFilter.applyGaussianBlurEffect(aProcesar,p.getValor());
                break;
            case "halo":
                result = imgFilter.applyGaussianBlurEffect(aProcesar,p.getValor());
                break;
            case "inv":
                result = imgFilter.applyInvertEffect(aProcesar);
                break;
            case "grey":
                result = imgFilter.applyGreyscaleEffect(aProcesar);
                break;
            case "sharp":
                result = imgFilter.applySharpenEffect(aProcesar);
                break;
            case "edetect":
                result = imgFilter.applyEdgeDetectionEffect(aProcesar);
                break;
            case "smooth":
                result = imgFilter.applySmoothEffect(aProcesar,p.getValor());
                break;
            case "emboss":
                result = imgFilter.applyEmbossEffect(aProcesar);
                break;

            default:
                result=aProcesar;

        }
        if(p.getContraste()!=1 || p.getBrillo()!=1 || p.getSaturacion()!=1)
        {
            result = imgFilter.applySaturationFilter(result, p.getSaturacion());
            result = imgFilter.applyContrastEffect(result, p.getContraste());
            result = imgFilter.applyBrightnessEffect(result, p.getBrillo());



        }





        int pixelColor,A,R,G,B;


        for(int x=xIni;x<xFin;x++)
        {
            for(int y=yIni;y<yFin;y++)
            {
                pixelColor = result.getPixel(x-xIni, y-yIni);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                r.setPixel(x,y,Color.argb(A, R, G, B));

            }

        }







        if(p.getTipo().equals("sharp")) System.out.println("Acabo el filtro " + p.getTipo()+" el hilo "+cuad);
        //2,4,1,3
        //4,2,3,1

    }
}
