package com.company;

import java.util.*;
import java.io.*;

public class Main {

    private final Map<String, Double> classProbabilities = new HashMap<>(); // prawdopodobienstwo klassy
    private final Map<String, Map<String, Double>> featureProbabilities = new HashMap<>(); // prawdopodobieństwa cech

    public void train() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("training.txt"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.split(",");
            String clazz = tokens[0];
            if (!classProbabilities.containsKey(clazz)) {         //Sprawdzamy, czy mapa classProbabilities nie zawiera klucza odpowiadającego obecnej klasie clazz.
                classProbabilities.put(clazz, 0.0);
                featureProbabilities.put(clazz, new HashMap<>()); //tworzymy pustą mapę w mapie featureProbabilities dla danej klasy clazz.
            }
            classProbabilities.put(clazz, classProbabilities.get(clazz) + 1); // Zwiększamy o 1 wartość w mapie classProbabilities dla klasy clazz. Ta wartość reprezentuje liczbę wystąpień danej klasy w danych treningowych.
            for (int i = 1; i < tokens.length; i++) {
                String feature = tokens[i];
                if (!featureProbabilities.get(clazz).containsKey(feature)) {
                    featureProbabilities.get(clazz).put(feature, 0.0);
                }
                featureProbabilities.get(clazz).put(feature, featureProbabilities.get(clazz).get(feature) + 1); //Następnie zwiększamy o 1 wartość w mapie featureProbabilities dla danej klasy clazz i dla danej cechy feature. Ta wartość reprezentuje liczbę wystąpień danej cechy w danej klasie.
            }
        }
        bufferedReader.close();
        for (String clazz : classProbabilities.keySet()) {
            double count = classProbabilities.get(clazz); //Pobiera wartość (liczbę wystąpień) dla danej klasy clazz z mapy classProbabilities i przypisuje ją do zmiennej count.
            if (count == 0) {
                classProbabilities.put(clazz, laplace(count, getTotalCount(), classProbabilities.size()));
            } else {
                classProbabilities.put(clazz, count / (getTotalCount() + classProbabilities.size()));
            }
            Map<String, Double> probabilities = featureProbabilities.get(clazz); //Pobiera mapę prawdopodobieństw cech dla danej klasy clazz z mapy featureProbabilities i przypisuje ją do zmiennej probabilities.
            for (String feature : probabilities.keySet()) {
                double value = probabilities.get(feature); //Pobiera wartość (liczbę wystąpień) dla danej cechy feature z mapy probabilities i przypisuje ją do zmiennej value.
                if (value == 0) {
                    probabilities.put(feature, laplace(value, count, probabilities.size()));
                } else {
                    probabilities.put(feature, value / (count + probabilities.size()));
                }
            }
        }
    }

    public double laplace(double numerator, double denominator, int vocabularySize) {
        return (numerator + 1) / (denominator + vocabularySize);
    }

    public void classify() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("test.txt"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.split(",");
            double maxProbability = Double.NEGATIVE_INFINITY; // negative_infinity =  ujemną nieskończoność.
            String bestClass = null;
            for (String clazz : classProbabilities.keySet()) {
                double probability = Math.log(classProbabilities.get(clazz)); //Oblicza logarytm naturalny prawdopodobieństwa dla danej klasy clazz na podstawie wartości z mapy classProbabilities i przypisuje wynik do zmiennej probability.
                for (int i = 1; i < tokens.length; i++) {
                    String feature = tokens[i];
                    if (featureProbabilities.get(clazz).containsKey(feature)) { //Sprawdza, czy mapa prawdopodobieństw cech dla danej klasy clazz zawiera klucz odpowiadający aktualnej cechy feature.
                        probability += Math.log(featureProbabilities.get(clazz).get(feature));
                    } else {
                        double smoothedProbability = laplace(0.0, 0.0, featureProbabilities.get(clazz).size()); // w celu wygładzenia Laplace'a i obliczenia wygładzonego prawdopodobieństwa dla brakującej cechy.
                        probability += Math.log(smoothedProbability);
                    }
                }
                if (probability > maxProbability) {
                    maxProbability = probability;
                    bestClass = clazz;
                }
            }
            System.out.println("Classified as: " + bestClass);
        }
        bufferedReader.close();
    }

    // zwraca sumę wszystkich wartości
    private double getTotalCount() {
        double count = 0.0;
        for (double value : classProbabilities.values()) {
            count += value;
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        Main classifier = new Main();
        classifier.train();
        classifier.classify();
    }
}
