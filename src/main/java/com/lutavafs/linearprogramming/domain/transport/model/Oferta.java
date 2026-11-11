package com.lutavafs.linearprogramming.domain.transport.model;

public class Oferta extends CampoTransporte {
    private final int ORIGEM;

    public Oferta(Integer valor, int origem) {
        super(valor);
        this.ORIGEM = origem;
    }

    public int getORIGEM() {
        return ORIGEM;
    }

    @Override
    public String toString() {
        return "|" + valor + "|";
    }
}
