package com.lutavafs.linearprogramming.util;

import lombok.NoArgsConstructor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@NoArgsConstructor
public class SimplexAsserts {

    public static void assertMatrixEquals(double[][] expected, double[][] actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.length, actual.length, "As matrizes possuem número de linhas diferentes");

        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(
                    expected[i],
                    actual[i],
                    1.0E-9,
                    "test failed at matrix index [" + i + "]"
            );
        }
    }

    public static void assertMapEquals(Map<String, String> expected, Map<String, String> actual) {
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected.size(), actual.size(), "Os mapas possuem número de elementos diferentes");

        expected.forEach((key, expectedValue) -> {
            assertTrue(
                    actual.containsKey(key),
                    "A chave '" + key + "' esperada não foi encontrada no mapa final"
            );

            assertEquals(
                    expectedValue,
                    actual.get(key),
                    "Falha de precisão no valor da variável '" + key + "'"
            );
        });
    }
}
