package com.seeds;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Part H: evaluates a classifier's predictions against actual labels.
 * Varieties are assumed to be 1, 2 and 3 (extendable to any set of ints).
 */
public class ClassificationEvaluator {

    private final List<Integer> actual;
    private final List<Integer> predicted;
    private final TreeSet<Integer> varieties;
    /** confusionMatrix.get(actualVariety).get(predictedVariety) = count */
    private final TreeMap<Integer, TreeMap<Integer, Integer>> confusionMatrix;

    public ClassificationEvaluator(List<Integer> actual, List<Integer> predicted) {
        if (actual.size() != predicted.size()) {
            throw new IllegalArgumentException("actual and predicted must have the same size");
        }
        this.actual = actual;
        this.predicted = predicted;

        varieties = new TreeSet<>();
        varieties.addAll(actual);
        varieties.addAll(predicted);

        confusionMatrix = new TreeMap<>();
        for (int v : varieties) {
            TreeMap<Integer, Integer> row = new TreeMap<>();
            for (int p : varieties) row.put(p, 0);
            confusionMatrix.put(v, row);
        }
        for (int i = 0; i < actual.size(); i++) {
            int a = actual.get(i);
            int p = predicted.get(i);
            confusionMatrix.get(a).put(p, confusionMatrix.get(a).get(p) + 1);
        }
    }

    public int truePositives(int variety) {
        return confusionMatrix.get(variety).get(variety);
    }

    public int falsePositives(int variety) {
        int fp = 0;
        for (int a : varieties) {
            if (a == variety) continue;
            fp += confusionMatrix.get(a).get(variety);
        }
        return fp;
    }

    public int falseNegatives(int variety) {
        int fn = 0;
        for (int p : varieties) {
            if (p == variety) continue;
            fn += confusionMatrix.get(variety).get(p);
        }
        return fn;
    }

    public int trueNegatives(int variety) {
        return actual.size() - truePositives(variety) - falsePositives(variety) - falseNegatives(variety);
    }

    public double precision(int variety) {
        int tp = truePositives(variety);
        int fp = falsePositives(variety);
        return (tp + fp == 0) ? 0.0 : (double) tp / (tp + fp);
    }

    public double recall(int variety) {
        int tp = truePositives(variety);
        int fn = falseNegatives(variety);
        return (tp + fn == 0) ? 0.0 : (double) tp / (tp + fn);
    }

    public double accuracy() {
        int correct = 0;
        for (int i = 0; i < actual.size(); i++) {
            if (actual.get(i).equals(predicted.get(i))) correct++;
        }
        return (double) correct / actual.size();
    }

    public void displayConfusionMatrix() {
        System.out.print("Actual \\ Predicted");
        for (int v : varieties) System.out.print("\t" + v);
        System.out.println();
        for (int a : varieties) {
            System.out.print("  " + a);
            for (int p : varieties) {
                System.out.print("\t" + confusionMatrix.get(a).get(p));
            }
            System.out.println();
        }
    }

    public void displayReport() {
        displayConfusionMatrix();
        System.out.printf("Overall accuracy: %.2f%%%n", accuracy() * 100);
        for (int v : varieties) {
            System.out.printf("Variety %d -> precision: %.2f%%, recall: %.2f%% (TP=%d, FP=%d, FN=%d, TN=%d)%n",
                    v, precision(v) * 100, recall(v) * 100,
                    truePositives(v), falsePositives(v), falseNegatives(v), trueNegatives(v));
        }
    }
}
