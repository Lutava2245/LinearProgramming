package com.lutavafs.linearprogramming.domain.transport.model;

public abstract class CampoTransporte {
    protected Integer valor;

    public CampoTransporte(Integer valor) {
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "[" + valor + "]";
    }
}
