package com.legaultOs.idigram;

/**
 * Created by Legault on 04/05/2015.
 */
public class SecuenciaHilos
{
    boolean primero,segundo,tercero,cuarto;

    public SecuenciaHilos()
    {
        primero=segundo=tercero=cuarto=false;

    }

    public boolean isPrimero() {
        return primero;
    }

    public void setPrimero(boolean primero) {
        this.primero = primero;
    }

    public boolean isSegundo() {
        return segundo;
    }

    public void setSegundo(boolean segundo) {
        this.segundo = segundo;
    }

    public boolean isTercero() {
        return tercero;
    }

    public void setTercero(boolean tercero) {
        this.tercero = tercero;
    }

    public boolean isCuarto() {
        return cuarto;
    }

    public void setCuarto(boolean cuarto) {
        this.cuarto = cuarto;
    }
}
