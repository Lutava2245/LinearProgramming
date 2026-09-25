package main.models;

import java.util.ArrayList;
import java.util.List;

public class Matriz {
    private Custo[][] valores;
    private final List<Oferta> ofertas;
    private final List<Demanda> demandas;
    private Integer[] u;
    private Integer[] v;
    private boolean maximizar;

    public Matriz(Custo[][] valores, List<Oferta> ofertas, List<Demanda> demandas, Integer[] u, Integer[] v, boolean maximizar) {
        this.valores = valores;
        this.ofertas = ofertas;
        this.demandas = demandas;
        this.u = u;
        this.v = v;
        this.maximizar = maximizar;
    }

    public Custo[][] getValores() {
        return valores;
    }

    public void setValores(Custo[][] valores) {
        this.valores = valores;
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public List<Demanda> getDemandas() {
        return demandas;
    }

    public Integer[] getU() {
        return u;
    }

    public void setU(Integer[] u) {
        this.u = u;
    }

    public Integer[] getV() {
        return v;
    }

    public void setV(Integer[] v) {
        this.v = v;
    }

    public boolean isMaximizar() {
        return maximizar;
    }

    public void setMaximizar(boolean maximizar) {
        this.maximizar = maximizar;
    }

    public List<Custo> getBasicas() {
        List<Custo> basicas = new ArrayList<>();
        for (Custo[] custos : valores) {
            for (Custo custo : custos)
                if (custo.isBasica())
                    basicas.add(custo);
        }
        return basicas;
    }

    public List<Custo> getNaoBasicas() {
        List<Custo> basicas = new ArrayList<>();
        for (Custo[] custos : valores) {
            for (Custo custo : custos)
                if (!custo.isBasica())
                    basicas.add(custo);
        }
        return basicas;
    }

    public void verificarBalanceamento() {
        int totalDemanda = 0;
        for (Demanda demanda : demandas) {
            totalDemanda += demanda.getValor();
        }

        int totalOferta = 0;
        for (Oferta oferta : ofertas) {
            totalOferta += oferta.getValor();
        }

        if (totalOferta != totalDemanda) {
            if (totalOferta > totalDemanda) {
                demandas.add(new Demanda(totalOferta-totalDemanda, demandas.size()));
            } else {
                ofertas.add(new Oferta(totalDemanda-totalOferta, ofertas.size()));
            }

            Custo[][] valoresBalanceados = new Custo[ofertas.size()][demandas.size()];

            for (int i = 0; i < valoresBalanceados.length; i++) {
                for (int j = 0; j < valoresBalanceados[i].length; j++) {
                    if ((totalOferta > totalDemanda && j == demandas.size() - 1) || (totalOferta < totalDemanda && i == ofertas.size() - 1)) {
                        valoresBalanceados[i][j] = new Custo(0, i, j);
                    } else {
                        valoresBalanceados[i][j] = valores[i][j];
                    }
                }
            }

            setValores(valoresBalanceados);
            setU(new Integer[ofertas.size()]);
            setV(new Integer[demandas.size()]);
        }
    }

    public void resetarUV() {
        setU(new Integer[u.length]);
        setV(new Integer[v.length]);
        u[0] = 0;

        System.out.println("U1 = 0");
    }

    public boolean valoresUPreenchidos() {
        for (Integer valor : u)
            if (valor == null)
                return false;
        return true;
    }

    public boolean valoresVPreenchidos() {
        for (Integer valor : v)
            if (valor == null)
                return false;
        return true;
    }

    public List<Custo> getBasicasLinha(int linha) {
        List<Custo> basicas = new ArrayList<>();
        for (Custo custo : valores[linha])
            if (custo.isBasica())
                basicas.add(custo);
        return basicas;
    }

    public List<Custo> getBasicasColuna(int coluna) {
        List<Custo> basicas = new ArrayList<>();
        for (Custo[] linha : valores)
            if (linha[coluna].isBasica())
                basicas.add(linha[coluna]);
        return basicas;
    }

    @Override
    public String toString() {
        StringBuilder matrizString = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores[i].length; j++)
                matrizString.append(valores[i][j]).append(" ");
            matrizString.append(ofertas.get(i)).append("\n");
        }
        demandas.forEach(demanda -> matrizString.append(demanda).append(" "));
        matrizString.append("\n");
        return matrizString.toString();
    }
}
