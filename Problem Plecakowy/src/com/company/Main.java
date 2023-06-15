package com.company;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String fileName = "plecak.txt";
        int capacity;
        int length;
        int[] weights;
        int[] values;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] firstLine = reader.readLine().split(" ");
            capacity = Integer.parseInt(firstLine[0]);
            length = Integer.parseInt(firstLine[1]);

            weights = parseArray(reader.readLine());
            values = parseArray(reader.readLine());
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            return;
        }

        int maxVal = 0;
        int[] bestVector = new int[length];

        // generuję wszystkie możliwe wektory binarne o długości n
        for (int i = 0; i < Math.pow(2, length); i++) {
            int[] vector = generateVector(i, length);
            int weightSum = calculateSum(vector, weights);
            int valueSum = calculateSum(vector, values);

            // jeśli suma wag jest mniejsza od pojemności i suma wartości jest większa od dotychczasowej maksymalnej wartości
            if (weightSum <= capacity && valueSum > maxVal) {
                maxVal = valueSum;
                bestVector = vector;
            }

            System.out.println("Iterations: " + (i + 1) + ", Best vector: " + Arrays.toString(bestVector) + ", Weight: " + weightSum + ", Value: " + maxVal);
        }
    }

    // parsuję linię na tablicę liczb
    private static int[] parseArray(String line) {
        String[] elements = line.split(",");
        int[] array = new int[elements.length];
        for (int i = 0; i < elements.length; i++) {
            array[i] = Integer.parseInt(elements[i]);
        }
        return array;
    }

    // generuję wektor binarny o długości length na podstawie liczby number
    private static int[] generateVector(int number, int length) {
        int[] vector = new int[length];
        for (int i = 0; i < length; i++) {
            vector[i] = (number >> i) & 1;
        }
        return vector;
    }

    // sumuję iloczyny wektorów i wartości i zwracam sumę
    private static int calculateSum(int[] vector, int[] values) {
        int sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i] * values[i];
        }
        return sum;
    }
}
