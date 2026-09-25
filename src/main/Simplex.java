package main;

import main.models.CampoSimplex;
import main.models.Tabela;
import main.util.LeitorTabela;

import java.util.Arrays;

public class Simplex {
    private static Tabela simplex;
    private static Tabela tabela;
    private static int colunaPivo = -1;
    private static int linhaPivo = -1;
    private static boolean infinito = false;
    private static boolean variasSolucoes = false;

    public static void calcular() {
        try {
            tabela = LeitorTabela.lerRestricoes("src/resources/matriz.txt", "src/resources/variaveis.txt");
        } catch (Exception e) {
            return;
        }

        System.out.println(simplex);

        analisarValores();

        do {
            double menorValor = Double.MAX_VALUE;
            for (CampoSimplex campo : simplex.getLinha(0)) {
                if (!campo.getVariavel().startsWith("x")) continue;
                if (campo.getValor() < menorValor) {
                    menorValor = campo.getValor();
                    colunaPivo = campo.getVertical();
                }
            }

            if (menorValor >= 0)
                break;

            menorValor = Double.MAX_VALUE;
            for (int i = 1; i < simplex.totalLinhas(); i++) {
                double divisao = simplex.getConstantes().get(i).getValor() / simplex.getCampo(i, colunaPivo).getValor();
                if (divisao < 0)
                    continue;
                if (menorValor > divisao) {
                    menorValor = divisao;
                    linhaPivo = i;
                }
            }

            if (menorValor == Double.MAX_VALUE) {
                infinito = true;
                break;
            }

            trocarVariaveis();

            System.out.println(simplex);
        } while (true);

        if (infinito) {
            System.out.println("Não há Max " + simplex.getCampo(0, 0).getVariavel() + " porque " + simplex.getCampo(0, colunaPivo).getVariavel() + " tende ao infinito.");
        } else {
            for (CampoSimplex campo : simplex.getLinha(0)) {
                if (campo.getVariavel().startsWith("x") && campo.getValor() == 0) {
                    variasSolucoes = simplex.getVariaveis().get(campo.getVariavel()) == null;
                    break;
                }
            }

            if (variasSolucoes) {
                System.out.println("Há várias soluções.");

                StringBuilder equacao = new StringBuilder();
                equacao.append(simplex.getCampo(0, 0).getVariavel()).append(" =");
                for (int i = 0; i < simplex.getLinha(0).length; i++) {
                    CampoSimplex campo =  simplex.getCampo(0, i);
                    double valor = campo.getValor();

                    if (campo.getVariavel().startsWith("Z") || campo.getVariavel().startsWith("a") || campo.getVariavel().isBlank())
                        continue;
                    String valorString = valor % 1 == 0 ? String.format("%.0f", valor) : Double.toString(valor);
                    if (valor >= 0) {
                        equacao.append(" -").append(valorString);
                    } else {
                        equacao.append(" -(").append(valorString).append(")");
                    }

                    campo = simplex.getVariaveis().get(campo.getVariavel());
                    equacao.append("(").append(
                            campo == null
                                    ? 0
                                    : campo.getValor() % 1 == 0
                                      ? String.format("%.0f", campo.getValor())
                                      : campo.getValor()
                    ).append(")");
                }
                System.out.println(equacao + "\n");
            }

            simplex.getVariaveis().forEach((variavel, campoSimplex) -> System.out.println(variavel + ": " + (
                    campoSimplex == null
                            ? 0
                            : campoSimplex.getValor() % 1 == 0
                              ? String.format("%.0f", campoSimplex.getValor())
                              : campoSimplex.getValor())));
        }
    }

    private static void analisarValores() {
        boolean possuiArtificiais = false;
        boolean dual = tabela.getCampo(0,0).getVariavel().equals("D");
        for (CampoSimplex campo : tabela.getLinha(0)) {
            possuiArtificiais = campo.getConstanteM() > 0;
            if (possuiArtificiais) {
                break;
            }
        }

        simplex = dual ? tabela.criarDual() : tabela;

        if (possuiArtificiais) {
            resetarLinhaZ();
        }
    }

    private static void resetarLinhaZ() {
        for (CampoSimplex campo : simplex.getLinha(0)) {
            if (campo.getVariavel().startsWith("Z")) continue;
            double valorM = campo.getConstanteM();
            for (String variavel : simplex.getLinhas().keySet()) {
                if (variavel.startsWith("a")) {
                    int posicao = Integer.parseInt(variavel.substring(1));
                    valorM -= simplex.getCampo(posicao, campo.getVertical()).getValor();
                }
            }
            campo.setConstanteM(valorM);
        }

        System.out.println(simplex);

        do {
            double menorValorM = Double.MAX_VALUE;
            double menorValor = Double.MAX_VALUE;
            for (CampoSimplex campo : simplex.getLinha(0)) {
                if (campo.getVariavel().startsWith("Z") || simplex.getConstantes().contains(campo)) {
                    continue;
                }
                if (campo.getConstanteM() < menorValorM) {
                    menorValorM = campo.getConstanteM();
                    menorValor = campo.getValor();
                    colunaPivo = campo.getVertical();
                } else if (campo.getConstanteM() == menorValorM) {
                    if (campo.getValor() < menorValor) {
                        menorValor = campo.getValor();
                        colunaPivo = campo.getVertical();
                    }
                }
            }

            if (menorValorM >= 0) {
                for (int i = 0; i < simplex.getVariaveis().size(); i++) {
                    simplex.getVariaveis().remove("a" + (i + 1));
                }
                break;
            }

            menorValorM = Double.MAX_VALUE;
            for (int i = 1; i < simplex.totalLinhas(); i++) {
                double divisao = simplex.getConstantes().get(i).getValor() / simplex.getCampo(i, colunaPivo).getValor();
                if (divisao < 0)
                    continue;
                if (menorValorM > divisao) {
                    menorValorM = divisao;
                    linhaPivo = i;
                }
            }

            trocarVariaveis();

            System.out.println(simplex);
        } while (true);
    }

    private static void trocarVariaveis() {
        double[][] novosValores = new double[simplex.totalLinhas()][simplex.totalColunas()];
        double[] novasConstantesM = new double[simplex.totalColunas()];

        simplex.getLinhas().forEach((variavel, linha) -> {
            if (Arrays.equals(simplex.getLinha(linhaPivo), linha)) {
                simplex.getVariaveis().replace(variavel, null);
            }
        });

        simplex.getVariaveis().put(simplex.getCampo(0, colunaPivo).getVariavel(), simplex.getConstantes().get(linhaPivo));

        for (int i = 0; i < novosValores[linhaPivo].length; i++) {
            novosValores[linhaPivo][i] = simplex.getCampo(linhaPivo, i).getValor();
        }

        for (int i = 0; i < simplex.totalLinhas(); i++) {
            if (i == linhaPivo)
                continue;
            for (int j = 0; j < simplex.totalColunas(); j++) {
                if (i == 0) {
                    novasConstantesM[j] = simplex.getCampo(0, j).getConstanteM()
                            - (novosValores[linhaPivo][j] * simplex.getCampo(0, colunaPivo).getConstanteM());
                }
                novosValores[i][j] = (simplex.getCampo(i, j).getValor() * simplex.getCampo(linhaPivo, colunaPivo).getValor())
                        + (simplex.getCampo(linhaPivo, j).getValor() * (simplex.getCampo(i, colunaPivo).getValor() * -1));
            }
        }

        for (int i = 0; i < simplex.totalLinhas(); i++) {
            for (int j = 0; j < simplex.totalColunas(); j++) {
                if (i == 0) {
                    simplex.editarConstantesM(0, j, novasConstantesM[j]);
                }
                simplex.editarValor(i, j, novosValores[i][j]);
            }
        }
    }
}
