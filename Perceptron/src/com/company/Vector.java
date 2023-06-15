package com.company;

public class Vector {

    double[] values;
    Centroid centroid;

    public Vector(double[] values) {

        this.values = values;

    }

    // metoda licząca odległość do innego centroidu
    public double Distance(Centroid otherCentroid) {
        double sum = 0;

        for (int i = 0; i < otherCentroid.dimensions.length; i++) {
            sum += Math.pow(this.values[i] - otherCentroid.dimensions[i], 2); // suma kwadratów różnic
        }

        return sum;

    }

    // metoda licząca odległość do własnego centroidu
    public double ownDistance() {
        double sum = 0;

        for (int i = 0; i < this.values.length; i++) {
            sum += Math.pow(this.values[i] - this.centroid.dimensions[i], 2); // suma kwadratów różnic,
        }

        return sum;

    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }
}
