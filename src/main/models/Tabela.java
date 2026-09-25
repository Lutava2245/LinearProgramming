package main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tabela {
    private final Campo[][] valores;
    private Map<String, Campo> variaveis;
    private Map<String, Campo[]> linhas;

    public Tabela(Campo[][] variaveis) {
        this.valores = variaveis;
    }

    public Map<String, Campo> getVariaveis() {
        return variaveis;
    }

    public void setVariaveis(Map<String, Campo> variaveis) {
        this.variaveis = variaveis;
    }

    public Map<String, Campo[]> getLinhas() {
        return linhas;
    }

    public void setLinhas(Map<String, Campo[]> linhas) {
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

    public Campo[] getLinha(int horizontal) {
        return valores[horizontal];
    }

    public Campo getCampo(int horizontal, int vertical) {
        return valores[horizontal][vertical];
    }

    public List<Campo> getConstantes() {
        List<Campo> constantes = new ArrayList<>();
        for (Campo[] valore : valores) {
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
        valores[horizontal][vertical] = new Campo(horizontal, vertical, variavel, valor, constanteM);
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
        for (Campo[] linha : valores) {
            for (Campo campo : linha) {
                matriz.append(campo).append(" ");
            }
            matriz.append("\n");
        }
        return matriz.toString();
    }
}