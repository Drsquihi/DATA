package com.seeds;

import java.util.List;

/**
 * Part B: analyzing a raw Object[] seed (as produced by RawSeedReader)
 * before the code gets refactored into proper classes in Part C.
 */
public class SeedAnalyzer {

    private static final String[] FEATURE_NAMES = {
            "Area", "Perimeter", "Compactness", "Kernel Length",
            "Kernel Width", "Asymmetry Coeff.", "Kernel Groove Length"
    };

    private static final String[] VARIETY_NAMES = {"", "Kama", "Rosa", "Canadian"};

    /** B.a - Prints a kernel's properties in a formatted way. */
    public void displaySeedProfile(Object[] seed) {
        System.out.println("Seed Profile:");
        System.out.println("Area: " + seed[0]);
        System.out.println("Perimeter: " + seed[1]);
        System.out.println("Compactness: " + seed[2]);
        System.out.println("Kernel Length: " + seed[3]);
        System.out.println("Kernel Width: " + seed[4]);
        System.out.println("Asymmetry Coefficient: " + seed[5]);
        System.out.println("Kernel Groove Length: " + seed[6]);
        System.out.println("Organic: " + seed[7]);
        System.out.println("Region: " + seed[8]);
        int variety = (int) seed[9];
        System.out.println("Variety: " + variety + " (" + VARIETY_NAMES[variety] + ")");
    }

    /**
     * B.b - Normalizes the 7 numeric features of one seed to the 0-1 range,
     * using the min/max of each feature computed across the whole dataset.
     */
    public double[] normalizeValues(Object[] seed, List<Object[]> dataset) {
        double[] min = new double[7];
        double[] max = new double[7];
        for (int i = 0; i < 7; i++) {
            min[i] = Double.MAX_VALUE;
            max[i] = -Double.MAX_VALUE;
        }
        for (Object[] row : dataset) {
            for (int i = 0; i < 7; i++) {
                double value = (double) row[i];
                if (value < min[i]) min[i] = value;
                if (value > max[i]) max[i] = value;
            }
        }

        double[] normalized = new double[7];
        for (int i = 0; i < 7; i++) {
            double value = (double) seed[i];
            normalized[i] = (value - min[i]) / (max[i] - min[i]);
        }
        return normalized;
    }

    /**
     * B.c - Prints, for each of the 7 numeric features, the feature name
     * followed by a bar of '#' proportional to the normalized value
     * (20 characters for a value of 1.0).
     */
    public void displayFeatureBars(Object[] seed, List<Object[]> dataset) {
        double[] normalized = normalizeValues(seed, dataset);
        for (int i = 0; i < FEATURE_NAMES.length; i++) {
            int barLength = (int) Math.round(normalized[i] * 20);
            System.out.println(FEATURE_NAMES[i] + " | " + "#".repeat(Math.max(0, barLength)));
        }
    }
}
