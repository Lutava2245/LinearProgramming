package com.lutavafs.linearprogramming.domain.transport.model;

public class Demanda extends CampoTransporte {
    private final int DESTINO;

    public Demanda(Integer valor, int destino) {
        super(valor);
        this.DESTINO = destino;
    }

    public int getDESTINO() {
        return DESTINO;
    }

    @Override
    public String toString() {
        return "(" + valor + ")";
    }
}
