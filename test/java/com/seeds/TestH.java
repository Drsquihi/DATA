package com.seeds;

import java.util.ArrayList;
import java.util.List;

public class TestH {
    public static void main(String[] args) {
        List<Seed> trainSeeds = new SeedCsvDAO("data/seeds-train.csv").getAllSeeds();
        List<Seed> testSeeds = new SeedCsvDAO("data/seeds-test.csv").getAllSeeds();

        SeedVarietyClassifier classifier = new SeedVarietyClassifier();
        classifier.trainProfiles(trainSeeds);

        List<Integer> actual = new ArrayList<>();
        List<Integer> predicted = new ArrayList<>();
        for (Seed seed : testSeeds) {
            actual.add(seed.getVariety());
            predicted.add(classifier.predict(seed));
        }

        ClassificationEvaluator evaluator = new ClassificationEvaluator(actual, predicted);
        System.out.println("=== Evaluation on full test set (" + testSeeds.size() + " kernels) ===");
        evaluator.displayReport();
    }
}
