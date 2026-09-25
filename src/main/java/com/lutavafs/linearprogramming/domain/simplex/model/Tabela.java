package com.lutavafs.linearprogramming.domain.simplex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tabela {
    private final CampoSimplex[][] valores;
    private Map<String, CampoSimplex> variaveis;
    private Map<String, CampoSimplex[]> linhas;

    public Tabela(CampoSimplex[][] variaveis) {
        this.valores = variaveis;
    }

    public Map<String, CampoSimplex> getVariaveis() {
        return variaveis;
    }

    public void setVariaveis(Map<String, CampoSimplex> variaveis) {
        this.variaveis = variaveis;
    }

    public Map<String, CampoSimplex[]> getLinhas() {
        return linhas;
    }

    public void setLinhas(Map<String, CampoSimplex[]> linhas) {
        this.linhas = linhas;
    }

    public Tabela criarDual() {
        Tabela dual = this;
        int totalX = 0;
        int totalY = 0;
        for (String variavel : getVariaveis().keySet()) {
            if (variavel.contains("x")) {
                totalX++;
            }
            if (variavel.contains("y")) {
                totalY++;
            }
        }

        double[][] matriz = new double[totalY][totalX];
        dual.adicionarCampo(0, 0, "Z", getCampo(0, 0).getValor(), getCampo(0, 0).getConstanteM());
        for (int i = 0; i < totalY; i++) {
            for (int j = 0; j < totalX; j++) {
                matriz[i][j] = getCampo(i, (j + 1)).getValor();
            }
        }

        return dual;
    }

    public CampoSimplex[] getLinha(int horizontal) {
        return valores[horizontal];
    }

    public CampoSimplex getCampo(int horizontal, int vertical) {
        return valores[horizontal][vertical];
    }

    public List<CampoSimplex> getConstantes() {
        List<CampoSimplex> constantes = new ArrayList<>();
        for (CampoSimplex[] valore : valores) {
            constantes.add(valore[valore.length - 1]);
        }
        return constantes;
    }

    public int totalLinhas() {
        return valores.length;
    }

    public int totalColunas() {
        return valores[0].length;
    }

    public void adicionarCampo(int horizontal, int vertical, String variavel, double valor, double constanteM) {
        valores[horizontal][vertical] = new CampoSimplex(horizontal, vertical, variavel, valor, constanteM);
    }

    public void editarValor(int horizontal, int vertical, double valor) {
        valores[horizontal][vertical].setValor(valor);
    }

    public void editarConstantesM(int horizontal, int vertical, double constanteM) {
        valores[horizontal][vertical].setConstanteM(constanteM);
    }

    @Override
    public String toString() {
        StringBuilder matriz = new StringBuilder();
        for (CampoSimplex[] linha : valores) {
            for (CampoSimplex campo : linha) {
                matriz.append(campo).append(" ");
            }
            matriz.append("\n");
        }
        return matriz.toString();
    }
}