package com.lutavafs.linearprogramming.service;

import com.lutavafs.linearprogramming.solver.*;

import java.util.Scanner;

public class Service {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("""
                Escolha um algoritmo:
                1 - Simplex
                2 - Problema de Transporte
                """);
        if (scan.nextInt() == 1) {
            Simplex.calcular();
        } else {
            Transporte.calcular();
        }
    }
}