package com.lutavafs.linearprogramming.util;

import lombok.NoArgsConstructor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe utilitária para asserções personalizadas relacionadas ao Simplex.
 */
@NoArgsConstructor
public class SimplexAsserts {

    /**
     * Compara duas matrizes de números de ponto flutuante (double) para verificar
     * se são iguais.
     * <p>
     * A comparação é feita linha por linha, e cada elemento é comparado com uma
     * precisão de {@code 1.0E-9}. Se as matrizes tiverem tamanhos diferentes, uma
     * falha de asserção será
     * lançada.
     * </p>
     * 
     * @param expected matriz esperada.
     * @param actual   matriz atual.
     */
    public static void assertMatrixEquals(double[][] expected, double[][] actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.length, actual.length, "As matrizes possuem número de linhas diferentes");

        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(
                    expected[i],
                    actual[i],
                    1.0E-9,
                    "test failed at matrix index [" + i + "]");
        }
    }

    /**
     * Compara dois mapas de variáveis e seus valores para verificar se são iguais.
     * <p>
     * A comparação é feita verificando se ambos os mapas possuem o mesmo tamanho e
     * se cada chave e valor correspondentes são iguais. Se os mapas tiverem
     * tamanhos diferentes ou se houver uma chave ou valor diferente, uma falha de
     * asserção será lançada.
     * </p>
     * 
     * @param expected mapa esperado.
     * @param actual   mapa atual
     */
    public static void assertMapEquals(Map<String, String> expected, Map<String, String> actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.size(), actual.size(), "Os mapas possuem número de elementos diferentes");

        expected.forEach((key, expectedValue) -> {
            assertTrue(
                    actual.containsKey(key),
                    "A chave '" + key + "' esperada não foi encontrada no mapa final");

            assertEquals(
                    expectedValue,
                    actual.get(key),
                    "Falha de precisão no valor da variável '" + key + "'");
        });
    }
}
