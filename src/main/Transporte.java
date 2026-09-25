package main;

import main.models.Custo;
import main.models.Matriz;
import main.util.LeitorMatriz;

import java.util.ArrayList;
import java.util.List;

public class Transporte {
    static Matriz matrizInicial;
    static Matriz solucao;

    public static void calcular() {
        try {
            matrizInicial = LeitorMatriz.lerValores("src/resources/matriz.txt");
        } catch (Exception e) {
            return;
        }
        matrizInicial.verificarBalanceamento();
        System.out.println(matrizInicial);

        solucao = cantoNoroeste();
        System.out.println(solucao);

        do {
            solucao.resetarUV();
            while (!solucao.valoresUPreenchidos() || !solucao.valoresVPreenchidos()) {
                for (Custo custo : solucao.getBasicas()) {
                    int valor = matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor();
                    Integer valorU = solucao.getU()[custo.getORIGEM()];
                    Integer valorV = solucao.getV()[custo.getDESTINO()];

                    if (valorU != null && valorV == null) {
                        solucao.getV()[custo.getDESTINO()] = valor - valorU;
                        System.out.println("V" + (custo.getDESTINO() + 1) + " = " + valor + " - " + (valorU < 0 ? ("(" + valorU + ")") : valorU) + " = " + solucao.getV()[custo.getDESTINO()]);
                    } else if (valorU == null && valorV != null) {
                        solucao.getU()[custo.getORIGEM()] = valor - valorV;
                        System.out.println("U" + (custo.getORIGEM() + 1) + " = " + valor + " - " + (valorV < 0 ? ("(" + valorV + ")") : valorV) + " = " + solucao.getU()[custo.getORIGEM()]);
                    }
                }
            }
            System.out.println();

            Custo custoEscolhido = null;

            if (matrizInicial.isMaximizar()) {
                int maiorValor = Integer.MIN_VALUE;
                for (Custo custo : solucao.getNaoBasicas()) {
                    int valor = matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor() - solucao.getU()[custo.getORIGEM()] - solucao.getV()[custo.getDESTINO()];
                    System.out.println("Valor = " + matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor() + " - " + (solucao.getU()[custo.getORIGEM()] < 0 ? ("(" + solucao.getU()[custo.getORIGEM()] + ")") : solucao.getU()[custo.getORIGEM()]) + " - " + (solucao.getV()[custo.getDESTINO()] < 0 ? ("(" + solucao.getV()[custo.getDESTINO()] + ")") : solucao.getV()[custo.getDESTINO()]) + " = " + valor);
                    if (valor > maiorValor) {
                        maiorValor = valor;
                        custoEscolhido = custo;
                    }
                }
                System.out.println("Maior Valor: " + maiorValor + "\n");

                if (maiorValor < 0) {
                    break;
                }
            } else {
                int menorValor = Integer.MAX_VALUE;
                for (Custo custo : solucao.getNaoBasicas()) {
                    int valor = matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor() - solucao.getU()[custo.getORIGEM()] - solucao.getV()[custo.getDESTINO()];
                    System.out.println("Valor = " + matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor() + " - " + (solucao.getU()[custo.getORIGEM()] < 0 ? ("(" + solucao.getU()[custo.getORIGEM()] + ")") : solucao.getU()[custo.getORIGEM()]) + " - " + (solucao.getV()[custo.getDESTINO()] < 0 ? ("(" + solucao.getV()[custo.getDESTINO()] + ")") : solucao.getV()[custo.getDESTINO()]) + " = " + valor);
                    if (valor < menorValor) {
                        menorValor = valor;
                        custoEscolhido = custo;
                    }
                }
                System.out.println("Menor Valor: " + menorValor + "\n");

                if (menorValor >= 0) {
                    break;
                }
            }

            circuitoCompensacao(custoEscolhido);

            System.out.println(solucao);
        } while(true);

        int custoTotal = 0;
        StringBuilder calculoFinal = new StringBuilder();
        calculoFinal.append("C = ");
        for (Custo custo : solucao.getBasicas()) {
            if (!solucao.getBasicas().getFirst().equals(custo))
                calculoFinal.append(" + ");
            int valor = matrizInicial.getValores()[custo.getORIGEM()][custo.getDESTINO()].getValor();
            custoTotal += (custo.getValor() * valor);
            calculoFinal.append("(").append(custo.getValor()).append(" * ").append(valor).append(")");
        }
        calculoFinal.append(" = ").append(custoTotal);
        System.out.println(calculoFinal);

        StringBuilder solucaoFinal = new StringBuilder();
        solucaoFinal.append("S = {");
        for (Custo[] custos : solucao.getValores()) {
            for (Custo custo : custos) {
                if (!custos[0].equals(custo))
                    solucaoFinal.append(", ");
                solucaoFinal.append("X").append((custo.getORIGEM() + 1)).append((custo.getDESTINO() + 1));
                solucaoFinal.append(" = ").append(custo.getValor());
            }
        }
        solucaoFinal.append("}");
        System.out.println(solucaoFinal);
    }

    private static Matriz cantoNoroeste() {
        Custo[][] solucaoInicial = new Custo[matrizInicial.getValores().length][matrizInicial.getValores()[0].length];

        int origem = 0;
        int destino = 0;
        int oferta = matrizInicial.getOfertas().get(origem).getValor();
        int demanda = matrizInicial.getDemandas().get(destino).getValor();

        while (origem < matrizInicial.getValores().length || destino < matrizInicial.getValores()[0].length) {
            if (oferta > demanda) {
                solucaoInicial[origem][destino] = new Custo(demanda, origem, destino);
                solucaoInicial[origem][destino].setBasica(true);
                oferta -= demanda;
                demanda = matrizInicial.getDemandas().get(++destino).getValor();
            } else if (oferta < demanda) {
                solucaoInicial[origem][destino] = new Custo(oferta, origem, destino);
                solucaoInicial[origem][destino].setBasica(true);
                demanda -= oferta;
                oferta = matrizInicial.getOfertas().get(++origem).getValor();
            } else {
                solucaoInicial[origem][destino] = new Custo(oferta, origem, destino);
                solucaoInicial[origem][destino].setBasica(true);
                boolean ultimaOrigem = origem == matrizInicial.getOfertas().size() - 1;
                boolean ultimoDestino = destino == matrizInicial.getDemandas().size() - 1;
                origem++;
                destino++;

                if (!ultimaOrigem) {
                    oferta = matrizInicial.getOfertas().get(origem).getValor();
                }
                if (!ultimoDestino) {
                    demanda = matrizInicial.getDemandas().get(destino).getValor();
                }
                if (!ultimaOrigem && !ultimoDestino) {
                    solucaoInicial[origem][destino - 1] = new Custo(0, origem, destino - 1);
                    solucaoInicial[origem][destino - 1].setBasica(true);
                }
            }
        }

        for (int i = 0; i < solucaoInicial.length; i++) {
            for (int j = 0; j < solucaoInicial[0].length; j++) {
                if (solucaoInicial[i][j] != null) continue;
                solucaoInicial[i][j] = new Custo(0, i, j);
            }
        }

        return new Matriz(solucaoInicial, matrizInicial.getOfertas(), matrizInicial.getDemandas(), matrizInicial.getU(), matrizInicial.getV(), matrizInicial.isMaximizar());
    }

    private static void circuitoCompensacao(Custo custoEntrada) {
        custoEntrada.setBasica(true);

        List<Custo> circuitoCompensacao = new ArrayList<>(solucao.getBasicas());
        List<Custo> caminho = new ArrayList<>();
        caminho.add(custoEntrada);

        for (Custo custo : solucao.getBasicas()) {
            if (custo.equals(custoEntrada))
                continue;
            if (solucao.getBasicasLinha(custo.getORIGEM()).isEmpty() || solucao.getBasicasColuna(custo.getDESTINO()).isEmpty())
                circuitoCompensacao.remove(custo);
        }

        boolean circuitoEncontrado = buscarProximo(custoEntrada, true, custoEntrada, circuitoCompensacao, caminho);

        if (circuitoEncontrado) {
            int teta = Integer.MAX_VALUE;
            for (int i = 0; i < caminho.size(); i++) {
                Custo custo = caminho.get(i);

                if (!custo.equals(custoEntrada) && custo.getValor() < teta && i % 2 != 0)
                    teta = custo.getValor();
            }

            boolean par = true;
            StringBuilder circuito = new StringBuilder();
            for (int i = 0; i < solucao.getValores().length; i++) {
                for (Custo custo : solucao.getValores()[i]) {
                    if (custo.equals(custoEntrada)) {
                        circuito.append("[θ] ");
                        continue;
                    }
                    circuito.append("[").append(custo.getValor());

                    if (caminho.contains(custo)) {
                        if (par) {
                            par = false;
                            circuito.append("-θ");
                        } else {
                            par = true;
                            circuito.append("+θ");
                        }
                    }
                    circuito.append("] ");
                }
                circuito.append("\n");
            }
            System.out.println(circuito);

            boolean variavelZerada = true;
            for (int i = 0; i < caminho.size(); i++) {
                Custo custo = caminho.get(i);

                if (i % 2 == 0) {
                    custo.setValor(custo.getValor() + teta);
                } else {
                    custo.setValor(custo.getValor() - teta);
                    if (custo.getValor() <= 0) {
                        custo.setValor(0);
                        if (variavelZerada) {
                            variavelZerada = false;
                            custo.setBasica(false);
                        }
                    }
                }
            }
        }
    }

    private static boolean buscarProximo(Custo celulaAtual, boolean isHorizontal, Custo custoEntrada, List<Custo> circuitoCompensacao, List<Custo> caminho) {
        if (isHorizontal) {
            for (Custo proxima : circuitoCompensacao) {
                if (proxima.getORIGEM() == celulaAtual.getORIGEM() && !caminho.contains(proxima)) {
                    caminho.add(proxima);

                    if (buscarProximo(proxima, false, custoEntrada, circuitoCompensacao, caminho))
                        return true;

                    caminho.removeLast();
                }
            }
        } else {
            for (Custo proxima : circuitoCompensacao) {
                if (proxima.getDESTINO() == celulaAtual.getDESTINO()) {
                    if (proxima == custoEntrada && caminho.size() >= 3)
                        return true;

                    if (!caminho.contains(proxima)) {
                        caminho.add(proxima);

                        if (buscarProximo(proxima, true, custoEntrada, circuitoCompensacao, caminho))
                            return true;

                        caminho.removeLast();
                    }
                }
            }
        }

        return false;
    }
}
