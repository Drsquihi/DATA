package com.seeds;

import java.util.ArrayList;
import java.util.List;

public class TestI {
    public static void main(String[] args) {
        List<Seed> trainSeeds = new SeedCsvDAO("data/seeds-train.csv").getAllSeeds();
        List<Seed> testSeeds = new SeedCsvDAO("data/seeds-test.csv").getAllSeeds();

        System.out.println("### Baseline (equal weights, all features, average profile) ###");
        double baseline = evaluate(trainSeeds, testSeeds, null, null, SeedVarietyClassifier.CenterStrategy.AVERAGE);

        System.out.println("\n### With feature normalization (min/max from training set) ###");
        List<Seed> trainNorm = SeedVarietyClassifier.normalizeAll(trainSeeds, trainSeeds);
        List<Seed> testNorm = SeedVarietyClassifier.normalizeAll(testSeeds, trainSeeds);
        double normalizedAcc = evaluate(trainNorm, testNorm, null, null, SeedVarietyClassifier.CenterStrategy.AVERAGE);

        System.out.println("\n### Feature selection: area, compactness, kernel groove length only ###");
        boolean[] selected = {true, false, true, false, false, false, true};
        double selectedAcc = evaluate(trainNorm, testNorm, null, selected, SeedVarietyClassifier.CenterStrategy.AVERAGE);

        System.out.println("\n### Weighted distance (emphasizing area & compactness) ###");
        double[] weights = {2.0, 1.0, 2.0, 1.0, 1.0, 0.5, 1.0};
        double weightedAcc = evaluate(trainNorm, testNorm, weights, null, SeedVarietyClassifier.CenterStrategy.AVERAGE);

        System.out.println("\n### Bonus: median (medoid) profile instead of average, normalized ###");
        double medianAcc = evaluate(trainNorm, testNorm, null, null, SeedVarietyClassifier.CenterStrategy.MEDIAN);

        System.out.println("\n=== Summary ===");
        System.out.printf("Baseline (raw scale, average, equal weights): %.1f%%%n", baseline * 100);
        System.out.printf("Normalized features:                          %.1f%%%n", normalizedAcc * 100);
        System.out.printf("Normalized + feature selection:               %.1f%%%n", selectedAcc * 100);
        System.out.printf("Normalized + weighted distance:               %.1f%%%n", weightedAcc * 100);
        System.out.printf("Normalized + median centroid:                 %.1f%%%n", medianAcc * 100);
    }

    private static double evaluate(List<Seed> train, List<Seed> test, double[] weights,
                                     boolean[] selectedFeatures, SeedVarietyClassifier.CenterStrategy strategy) {
        SeedVarietyClassifier classifier = new SeedVarietyClassifier();
        if (weights != null) classifier.setWeights(weights);
        if (selectedFeatures != null) classifier.setSelectedFeatures(selectedFeatures);
        classifier.setCenterStrategy(strategy);
        classifier.trainProfiles(train);

        List<Integer> actual = new ArrayList<>();
        List<Integer> predicted = new ArrayList<>();
        for (Seed seed : test) {
            actual.add(seed.getVariety());
            predicted.add(classifier.predict(seed));
        }
        ClassificationEvaluator evaluator = new ClassificationEvaluator(actual, predicted);
        evaluator.displayReport();
        return evaluator.accuracy();
    }
}
