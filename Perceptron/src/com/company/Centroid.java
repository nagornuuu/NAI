package com.company;

import java.util.List;
import java.util.Objects;

public class Centroid {

    double[] dimensions;
    double groupDistance;
    static int centroidsCreated;
    int centroidNumber;

    public Centroid(double[] dimensions) {

        this.dimensions = dimensions;
        this.centroidNumber = centroidsCreated;
        ++centroidsCreated;                     // inkrementuje liczbę utworzonych centroidów

    }

    // metoda, oblicza nowy centroid dla danej grupy punktow
    public void newCentroid(List<Vector> vectors) {

        // tablica przechowująca sumy wartości
        int[] liczniki = new int[vectors.get(0).values.length];
        double mianownik = 0;

        for (Vector vector : vectors) {
            if (Objects.equals(vector.centroid, this)) {
                for (int i = 0; i < vector.values.length; i++) {
                    liczniki[i] += vector.values[i];
                }
                mianownik++; // zwiększa mianownik o 1
            }
        }

        for (int i = 0; i < this.dimensions.length; i++) {
            if (mianownik != 0) {
                this.dimensions[i] = (liczniki[i] / mianownik);  // oblicza nowe wymiary centroidu
            }
        }
    }
}
