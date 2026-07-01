package com.seeds;

import java.util.List;
import java.util.Map;

public class TestF {
    public static void main(String[] args) {
        List<Seed> trainSeeds = new SeedCsvDAO("data/seeds-train.csv").getAllSeeds();
        List<Seed> testSeeds = new SeedCsvDAO("data/seeds-test.csv").getAllSeeds();

        SeedStatistics stats = new SeedStatistics();
        Map<Integer, Seed> profiles = stats.trainVarietyProfiles(trainSeeds);

        int limit = Math.min(20, testSeeds.size());
        int correct = 0;

        System.out.printf("%-5s%-10s%-10s%-10s%n", "#", "Actual", "Predicted", "Correct?");
        for (int i = 0; i < limit; i++) {
            Seed seed = testSeeds.get(i);
            int predicted = predictVariety(seed, profiles);
            boolean isCorrect = predicted == seed.getVariety();
            if (isCorrect) correct++;
            System.out.printf("%-5d%-10d%-10d%-10s%n", i + 1, seed.getVariety(), predicted, isCorrect);
        }

        System.out.println("\nCorrectly classified: " + correct + " / " + limit);
    }

    private static int predictVariety(Seed seed, Map<Integer, Seed> profiles) {
        int bestVariety = -1;
        double bestDistance = Double.MAX_VALUE;
        for (Map.Entry<Integer, Seed> entry : profiles.entrySet()) {
            double distance = euclideanDistance(seed.getFeatures(), entry.getValue().getFeatures());
            if (distance < bestDistance) {
                bestDistance = distance;
                bestVariety = entry.getKey();
            }
        }
        return bestVariety;
    }

    private static double euclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
