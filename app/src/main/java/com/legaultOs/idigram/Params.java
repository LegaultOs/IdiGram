package com.legaultOs.idigram;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Legault on 27/04/2015.
 */
public class Params {
    Bitmap bm;
    String tipo,label;
    double r,g,b;
    double valor;
    float percent;
    int id,contraste,brillo,saturacion;
    boolean procesa;


    public Params( boolean procesa,String tipo, String label,Bitmap bm, double r, double g, double b, double valor, float percent, int id, int contraste, int brillo, int saturacion) {
        this.bm = bm;
        this.tipo = tipo;
        this.label = label;
        this.r = r;
        this.g = g;
        this.b = b;
        this.valor = valor;
        this.percent = percent;
        this.id = id;
        this.contraste = contraste;
        this.brillo = brillo;
        this.saturacion = saturacion;
        this.procesa = procesa;
    }

    public int getContraste() {
        return contraste;
    }

    public int getBrillo() {
        return brillo;
    }

    public int getSaturacion() {
        return saturacion;
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
    public boolean isProcesa(){return procesa;}

}
