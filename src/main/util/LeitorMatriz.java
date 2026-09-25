package main.util;

import main.models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LeitorMatriz {
    public static Matriz lerValores(String path) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(path));
        List<String[]> dados = new ArrayList<>();

        while (bf.ready()) {
            dados.add(bf.readLine().split(" "));
        }
        bf.close();

        Custo[][] custos = new Custo[dados.size()-2][dados.getFirst().length-1];
        List<Oferta> ofertas = new ArrayList<>();
        List<Demanda> demandas = new ArrayList<>();
        Integer[] u = new Integer[dados.size()-2];
        Integer[] v = new Integer[dados.getFirst().length-1];

        for (int i = 0; i < dados.size()-1; i++) {
            for (int j = 0; j < dados.get(i).length; j++) {
                Integer valor =  Integer.parseInt(dados.get(i)[j]);

                if (j == dados.getFirst().length - 1) {
                    ofertas.add(new Oferta(valor, i));
                } else if (i == dados.size() - 2) {
                    demandas.add(new Demanda(valor, i));
                } else {
                    custos[i][j] = new Custo(valor, i, j);
                    custos[i][j].setBasica(true);
                }
            }
        }

        boolean maximizar = dados.getLast()[0].equals("Max");

        return new Matriz(custos, ofertas, demandas, u, v, maximizar);
    }
}
