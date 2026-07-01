package com.seeds;

import java.util.List;

public class TestG {
    public static void main(String[] args) {
        List<Seed> trainSeeds = new SeedCsvDAO("data/seeds-train.csv").getAllSeeds();
        List<Seed> testSeeds = new SeedCsvDAO("data/seeds-test.csv").getAllSeeds();

        SeedVarietyClassifier classifier = new SeedVarietyClassifier();
        classifier.trainProfiles(trainSeeds);

        int correct = 0;
        System.out.printf("%-5s%-10s%-10s%-10s%n", "#", "Actual", "Predicted", "Correct?");
        for (int i = 0; i < testSeeds.size(); i++) {
            Seed seed = testSeeds.get(i);
            int predicted = classifier.predict(seed);
            boolean isCorrect = predicted == seed.getVariety();
            if (isCorrect) correct++;
            System.out.printf("%-5d%-10d%-10d%-10s%n", i + 1, seed.getVariety(), predicted, isCorrect);
        }

        System.out.println("\nCorrectly classified: " + correct + " / " + testSeeds.size() +
                String.format(" (%.1f%%)", 100.0 * correct / testSeeds.size()));
    }
}
