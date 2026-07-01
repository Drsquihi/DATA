package com.seeds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Part D and E: statistics about the seed dataset (variety distribution,
 * organic counts per region, and average variety profiles).
 */
public class SeedStatistics {

    /** D.a - Number of kernels per variety. */
    public Map<Integer, Integer> calculateVarietyDistribution(List<Seed> seeds) {
        Map<Integer, Integer> distribution = new TreeMap<>();
        for (Seed seed : seeds) {
            distribution.merge(seed.getVariety(), 1, Integer::sum);
        }
        return distribution;
    }

    /** D.b - Prints the distribution as a simple text histogram. */
    public void displayVarietyDistribution(Map<Integer, Integer> distribution) {
        for (Map.Entry<Integer, Integer> entry : distribution.entrySet()) {
            int count = entry.getValue();
            System.out.println("Variety " + entry.getKey() + " | " + "#".repeat(count) + " (" + count + ")");
        }
    }

    /** D.d - Number of organic kernels per region. */
    public Map<String, Integer> countOrganicByRegion(List<Seed> seeds) {
        Map<String, Integer> counts = new TreeMap<>();
        for (Seed seed : seeds) {
            if (seed.isOrganic()) {
                counts.merge(seed.getRegion(), 1, Integer::sum);
            }
        }
        return counts;
    }

    public void displayOrganicByRegion(Map<String, Integer> counts) {
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + " | " + "#".repeat(entry.getValue()) + " (" + entry.getValue() + ")");
        }
    }

    /** E.a/b/c - Average feature profile for each variety. */
    public Map<Integer, Seed> trainVarietyProfiles(List<Seed> trainingSeeds) {
        Map<Integer, double[]> sums = new HashMap<>();
        Map<Integer, Integer> counts = new HashMap<>();

        for (Seed seed : trainingSeeds) {
            int variety = seed.getVariety();
            double[] features = seed.getFeatures();
            sums.putIfAbsent(variety, new double[7]);
            counts.putIfAbsent(variety, 0);

            double[] sum = sums.get(variety);
            for (int i = 0; i < 7; i++) {
                sum[i] += features[i];
            }
            counts.put(variety, counts.get(variety) + 1);
        }

        Map<Integer, Seed> profiles = new TreeMap<>();
        for (Integer variety : sums.keySet()) {
            double[] sum = sums.get(variety);
            int count = counts.get(variety);
            double[] average = new double[7];
            for (int i = 0; i < 7; i++) {
                average[i] = sum[i] / count;
            }
            Seed profile = new Seed();
            profile.setFeatures(average);
            profile.setVariety(variety);
            profiles.put(variety, profile);
        }
        return profiles;
    }

    public void displayVarietyProfiles(Map<Integer, Seed> profiles) {
        for (Map.Entry<Integer, Seed> entry : profiles.entrySet()) {
            Seed profile = entry.getValue();
            System.out.printf("Variety %d (%s) average profile:%n", entry.getKey(), profile.getVarietyName());
            System.out.printf("  Area: %.3f%n", profile.getArea());
            System.out.printf("  Perimeter: %.3f%n", profile.getPerimeter());
            System.out.printf("  Compactness: %.4f%n", profile.getCompactness());
            System.out.printf("  Kernel Length: %.3f%n", profile.getKernelLength());
            System.out.printf("  Kernel Width: %.3f%n", profile.getKernelWidth());
            System.out.printf("  Asymmetry Coefficient: %.3f%n", profile.getAsymmetryCoefficient());
            System.out.printf("  Kernel Groove Length: %.3f%n", profile.getKernelGrooveLength());
        }
    }
}
