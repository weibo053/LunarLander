package me.themgrf.lunarlander.handlers;

import java.util.Random;

/**
 * Terrain generator using noise maps
 * @see <a href="https://www.redblobgames.com/articles/noise/introduction.html">Noise Generator</a>
 */
public class Terrain {

    private static final Random generator = new Random();

    public static double[] noise(double f, int N) {
        double[] n = new double[N];
        double phi = generator.nextDouble() * 2 * Math.PI;

        for (int x = 0; x < N; x++) n[x] = (Math.sin(2 * Math.PI * f * x / N + phi));

        return n;
    }

    public static double[] weighted_sum(double[] amplitudes, double[][] noises) {
        int N = noises[0].length;
        double[] sum = new double[N];

        for (int k = 0; k < noises.length; k++) {
            for (int x = 0; x < N; x++) sum[x] += amplitudes[k] * noises[k][x];
        }

        return sum;
    }

    public static double[] ground(int size, int frequencies) {
        double[] a = new double[frequencies];
        double[] f = new double[frequencies];

        for (int i = 0; i < f.length; i++) {
            f[i] = i + 1;
            a[i] = 1 / f[i];
        }

        double[][] noises = new double[a.length][size];
        for (int j = 0; j < f.length; j++) noises[j] = noise(f[j], size);

        return weighted_sum(a, noises);
    }

    public static double[] wrapped(double[] gnd) {
        double end = (gnd[0] + gnd[gnd.length - 1]) / 2;
        gnd[0] = gnd[gnd.length - 1] = end;
        return gnd;
    }
}
