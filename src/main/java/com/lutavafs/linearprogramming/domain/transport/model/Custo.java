package com.lutavafs.linearprogramming.domain.transport.model;

public class Custo extends CampoTransporte {
    private final int ORIGEM;
    private final int DESTINO;
    private boolean basica = false;

    public Custo(Integer valor, int origem, int destino) {
        super(valor);
        this.ORIGEM = origem;
        this.DESTINO = destino;
    }

    public int getORIGEM() {
        return ORIGEM;
    }

    public int getDESTINO() {
        return DESTINO;
    }

    public boolean isBasica() {
        return basica;
    }

    public void setBasica(boolean basica) {
        this.basica = basica;
    }

    @Override
    public String toString() {
        return "[" + ((basica && valor == 0) ? "A" : valor) + "]";
    }
}