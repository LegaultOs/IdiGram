package com.legaultOs.idigram;

import android.graphics.Bitmap;

/**
 * Created by Legault on 27/04/2015.
 */
public class Params {
    Bitmap bm;
    String tipo,label;
    double r,g,b;
    double valor;
    float percent;
    int id;



    public Params(String tipo,String label,Bitmap bm,double r, double g, double b, double valor,
                  float percent,int id) {
        super();
        this.label=label;
        this.tipo=tipo;
        this.bm = bm;
        this.r = r;
        this.g = g;
        this.b = b;
        this.valor = valor;
        this.percent = percent;
        this.id=id;
    }

    public Bitmap getBm() {
        return bm;
    }
    public double getR() {
        return r;
    }
    public double getG() {
        return g;
    }
    public double getB() {
        return b;
    }
    public double getValor() {
        return valor;
    }
    public float getPercent() {
        return percent;
    }
    public String getTipo() {
        return tipo;
    }
    public String getLabel() {
        return label;
    }
    public int getId() {
        return id;
    }

}
