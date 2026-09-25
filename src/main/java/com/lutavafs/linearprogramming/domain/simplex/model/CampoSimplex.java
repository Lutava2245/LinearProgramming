package com.lutavafs.linearprogramming.domain.simplex.model;

public class CampoSimplex {
    private final int HORIZONTAL;
    private final int VERTICAL;
    private final String VARIAVEL;
    private double valor;
    private double constanteM;

    public CampoSimplex(int horizontal, int vertical, String variavel, double valor, double constanteM) {
        this.HORIZONTAL = horizontal;
        this.VERTICAL = vertical;
        this.VARIAVEL = variavel;
        this.valor = valor;
        this.constanteM = constanteM;
    }
    
    public int getHorizontal() {
        return HORIZONTAL;
    }

    public int getVertical() {
        return VERTICAL;
    }

    public String getVariavel() {
        return VARIAVEL;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getConstanteM() {
        return constanteM;
    }

    public void setConstanteM(double constanteM) {
        this.constanteM = constanteM;
    }

    @Override
    public String toString() {
        if (constanteM != 0) {
            return "[" + valor + VARIAVEL + " " + constanteM + "]";
        }
        return "[" + valor + VARIAVEL + "]";
    }
}