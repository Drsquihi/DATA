package com.seeds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parts F, G and I: nearest-centroid classifier for wheat varieties.
 *
 * Supports (Part I improvements):
 *  - equal-weight or custom-weight Euclidean distance
 *  - restricting the distance computation to a subset of features
 *  - two centroid strategies: AVERAGE (mean) or MEDIAN (medoid-like center)
 */
public class SeedVarietyClassifier {

    public enum CenterStrategy { AVERAGE, MEDIAN }

    private Map<Integer, Seed> varietyProfiles;
    private double[] weights = {1, 1, 1, 1, 1, 1, 1};
    private boolean[] selectedFeatures = {true, true, true, true, true, true, true};
    private CenterStrategy centerStrategy = CenterStrategy.AVERAGE;

    public void setWeights(double[] weights) {
        if (weights.length != 7) throw new IllegalArgumentException("Expected 7 weights");
        this.weights = weights;
    }

    public void setSelectedFeatures(boolean[] selectedFeatures) {
        if (selectedFeatures.length != 7) throw new IllegalArgumentException("Expected 7 flags");
        this.selectedFeatures = selectedFeatures;
    }

    public void setCenterStrategy(CenterStrategy centerStrategy) {
        this.centerStrategy = centerStrategy;
    }

    public Map<Integer, Seed> getVarietyProfiles() {
        return varietyProfiles;
    }

    /** G.a / E - Trains one profile (centroid) per variety from the training set. */
    public void trainProfiles(List<Seed> trainingSeeds) {
        Map<Integer, List<Seed>> byVariety = new TreeMap<>();
        for (Seed seed : trainingSeeds) {
            byVariety.computeIfAbsent(seed.getVariety(), k -> new ArrayList<>()).add(seed);
        }

        varietyProfiles = new TreeMap<>();
        for (Map.Entry<Integer, List<Seed>> entry : byVariety.entrySet()) {
            double[] center = (centerStrategy == CenterStrategy.AVERAGE)
                    ? computeAverage(entry.getValue())
                    : computeMedian(entry.getValue());
            Seed profile = new Seed();
            profile.setFeatures(center);
            profile.setVariety(entry.getKey());
            varietyProfiles.put(entry.getKey(), profile);
        }
    }

    private double[] computeAverage(List<Seed> seeds) {
        double[] sum = new double[7];
        for (Seed seed : seeds) {
            double[] f = seed.getFeatures();
            for (int i = 0; i < 7; i++) sum[i] += f[i];
        }
        double[] average = new double[7];
        for (int i = 0; i < 7; i++) average[i] = sum[i] / seeds.size();
        return average;
    }

    /** Bonus (I) - "medoid": per-feature median of the variety's kernels. */
    private double[] computeMedian(List<Seed> seeds) {
        double[] median = new double[7];
        for (int i = 0; i < 7; i++) {
            double[] values = new double[seeds.size()];
            for (int j = 0; j < seeds.size(); j++) {
                values[j] = seeds.get(j).getFeatures()[i];
            }
            Arrays.sort(values);
            int n = values.length;
            median[i] = (n % 2 == 1)
                    ? values[n / 2]
                    : (values[n / 2 - 1] + values[n / 2]) / 2.0;
        }
        return median;
    }

    /** G.b / F - Predicts the variety of a seed as the closest profile. */
    public int predict(Seed seed) {
        int bestVariety = -1;
        double bestDistance = Double.MAX_VALUE;
        for (Map.Entry<Integer, Seed> entry : varietyProfiles.entrySet()) {
            double distance = calculateDistance(seed, entry.getValue());
            if (distance < bestDistance) {
                bestDistance = distance;
                bestVariety = entry.getKey();
            }
        }
        return bestVariety;
    }

    /**
     * G.c / F / I - Weighted Euclidean distance over the (optionally
     * restricted) set of features between two seeds.
     */
    private double calculateDistance(Seed a, Seed b) {
        double[] fa = a.getFeatures();
        double[] fb = b.getFeatures();
        double sumSquares = 0;
        for (int i = 0; i < 7; i++) {
            if (!selectedFeatures[i]) continue;
            double diff = fa[i] - fb[i];
            sumSquares += weights[i] * diff * diff;
        }
        return Math.sqrt(sumSquares);
    }

    /**
     * I - Normalizes a list of seeds' 7 features to 0-1, using min/max
     * computed from the reference (training) list, and returns new Seed
     * instances (does not mutate the input).
     */
    public static List<Seed> normalizeAll(List<Seed> toNormalize, List<Seed> reference) {
        double[] min = new double[7];
        double[] max = new double[7];
        Arrays.fill(min, Double.MAX_VALUE);
        Arrays.fill(max, -Double.MAX_VALUE);

        for (Seed seed : reference) {
            double[] f = seed.getFeatures();
            for (int i = 0; i < 7; i++) {
                if (f[i] < min[i]) min[i] = f[i];
                if (f[i] > max[i]) max[i] = f[i];
            }
        }

        List<Seed> normalized = new ArrayList<>();
        for (Seed seed : toNormalize) {
            normalized.add(seed.normalize(min, max));
        }
        return normalized;
    }
}
