package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    static Random random = new Random();
    static int iterations = 0;
    static double distance = 0;
    static boolean changedGroup;
    static List<Vector> vectors = new ArrayList<>();
    static List<Centroid> centroids = new ArrayList<>();
    static int x = 0;
    static boolean validInput = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[]args) {
        while (!validInput) {
            System.out.println("Podaj liczbe grup: ");
            try {
                x = Integer.parseInt(scanner.nextLine());
                validInput = true;
                if (x == 0) {
                    System.out.println("Podaj liczbe > 0");
                    validInput = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowe dane wejsciowe, podaj liczbe");
            }
        }
        System.out.printf("Wybrales podzial na: %d grupy \n\n", x);


        try {
            BufferedReader reader = new BufferedReader(new FileReader("test.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] valuesString = line.split(",");
                double[] values = new double[valuesString.length];          // tworzy tablice z wartosciami
                for (int i = 0; i < valuesString.length; i++) {
                    values[i] = Double.parseDouble(valuesString[i]);
                }
                Vector vector = new Vector(values);                         // tworzy wektor z wartosciami
                vectors.add(vector);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Blad podczas odczytu pliku: " + e.getMessage());
            System.exit(1);
        }

        // tworzy kopie wektorow przed losowaniem centroidow i przypisaniem wektorow do centroidow
        List<double[]> copy = new ArrayList<>();
        for (Vector value : vectors) {
            copy.add(value.values.clone());
        }

        // losowo miesza listę wektorow
        List<Vector> mix = new ArrayList<>(vectors);
        Collections.shuffle(mix);

        // tworzy centroidy z pierwszych x wektorow
        for (int i = 0; i < x; i++) {
            centroids.add(new Centroid(mix.get(i).values));
        }

        // przypisuje wektory do centroidow (losowo)
        for (Vector vector : vectors) {
            int index = random.nextInt(centroids.size());
            vector.setCentroid(centroids.get(index));
        }

        // algorytm k-means
        int iterations = kMeans(vectors, centroids);

        for (int i = 0; i < vectors.size(); i++) {
            System.out.printf("Vector %s ,centroid %d%n", Arrays.toString(copy.get(i)), (vectors.get(i).centroid.centroidNumber + 1));
        }

        System.out.printf("\nPotrzeba: %d iteracji.\n", iterations);

    }

    public static int kMeans(List<Vector> vectors, List<Centroid> centroids) {

        do {
            changedGroup = false;
            for (Vector vector : vectors) {
                for (Centroid centroid : centroids) {
                    if (vector.ownDistance() > vector.Distance(centroid)) {       // jezeli odleglosc wektora do centroidu jest mniejszy od odleglosc wektora do centroidu do ktorego nalezy
                        vector.centroid = centroid;                               // przypisuje nowy centroid
                        changedGroup = true;
                    }
                }
            }
            if (changedGroup) {
                for (Centroid centroid : centroids) {
                    centroid.newCentroid(vectors);
                }
            }
            iterations++;
            for (Centroid centroid : centroids) {
                centroid.groupDistance = 0;                           // zerujemy odleglosc do centroidow
                for (Vector vector : vectors) {
                    if (vector.centroid.equals(centroid)) {           // jesli centroid jest taki sam jak centroid wektora
                        centroid.groupDistance += vector.ownDistance();   // dodajemy odleglosc do centroidu
                    }
                }
            }
            for (Centroid centroid : centroids) {
                distance += centroid.groupDistance;   // liczymy odleglosc rekordow do centroidow
            }
            System.out.println("Po " + iterations + " iteracji dystans rekordow do centroidow wynosi: " + distance);
        } while (changedGroup);          // petla wykonuje sie dopoki zmieniamy grupe
        System.out.println();
        return iterations;
    }
}

