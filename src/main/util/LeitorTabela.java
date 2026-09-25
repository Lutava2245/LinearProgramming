package main.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import main.models.Campo;
import main.models.Tabela;

public class LeitorTabela {
    public static Tabela lerRestricoes(String pathMatriz, String pathVariaveis) throws IOException {
        /*
         * Deve seguir um padrão:
         * matriz.txt -> Apenas os valores da matriz.
         * 1 -1 -1 -1 0 0 M M 0
         * 0 2 1 -1 1 0 0 0 10
         * 0 1 1 2 0 -1 1 0 20
         * 0 2 1 3 0 0 0 1 60
         *
         * variaveis.txt -> 1ª linha = cada coluna, 2ª linha, cada linha.
         * Z x1 x2 x3 y1 y2 a2 a3
         * Z y1 a2 a3
         */
        BufferedReader bf = new BufferedReader(new FileReader(pathMatriz));
        List<String[]> dados = new ArrayList<>();

        while (bf.ready()) {
            dados.add(bf.readLine().split(" "));
        }
        bf.close();

        bf = new BufferedReader(new FileReader(pathVariaveis));
        String[] variaveisString  = bf.readLine().split(" ");
        String[] linhasString = bf.readLine().split(" ");
        bf.close();

        return getTabela(dados, variaveisString, linhasString);
    }

    private static Tabela getTabela(List<String[]> dados, String[] variaveisString, String[] linhasString) {
        Tabela tabela = new Tabela(new Campo[dados.size()][dados.getFirst().length]);
        for (int i = 0; i < dados.size(); i++) {
            for (int j = 0; j < dados.getFirst().length; j++) {
                String variavel = j == dados.getLast().length - 1 ? "" : variaveisString[j];
                if (dados.get(i)[j].equals("M")) {
                    tabela.adicionarCampo(i, j, variavel, 0, 1);
                } else {
                    tabela.adicionarCampo(i, j, variavel, Double.parseDouble(dados.get(i)[j]), 0);
                }
            }
        }

        Map<String, Campo> variaveis = new TreeMap<>();
        for (String variavel : variaveisString) {
            variaveis.put(variavel, null);
        }
        for (int i = 0; i < linhasString.length; i++) {
            variaveis.replace(linhasString[i], tabela.getConstantes().get(i));
        }
        tabela.setVariaveis(variaveis);

        Map<String, Campo[]> linhas = new TreeMap<>();
        for (int i = 0; i < linhasString.length; i++) {
            linhas.put(linhasString[i], tabela.getLinha(i));
        }
        tabela.setLinhas(linhas);

        return tabela;
    }
}
