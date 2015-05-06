package com.legaultOs.idigram;

import android.graphics.Bitmap;

/**
 * Created by Legault on 27/04/2015.
 */
public class Params {
    Bitmap bm;
    String tipo, label;
    double r, g, b;
    double valor;
    float percent;
    int id, contraste, brillo, saturacion;
    boolean procesa;


    public Params(boolean procesa, String tipo, String label, Bitmap bm, double r, double g, double b, double valor, float percent, int id, int contraste, int brillo, int saturacion) {
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

    public void setContraste(int contraste) {
        this.contraste = contraste;
    }

    public int getBrillo() {
        return brillo;
    }

    public void setBrillo(int brillo) {
        this.brillo = brillo;
    }

    public int getSaturacion() {
        return saturacion;
    }

    public void setSaturacion(int saturacion) {
        this.saturacion = saturacion;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isProcesa() {
        return procesa;
    }

    public void setProcesa(boolean procesa) {

        this.procesa = procesa;
    }
}
