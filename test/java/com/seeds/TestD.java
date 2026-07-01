package com.seeds;

import java.util.List;
import java.util.Map;

public class TestD {
    public static void main(String[] args) {
        SeedCsvDAO dao = new SeedCsvDAO("data/seeds-train.csv");
        List<Seed> seeds = dao.getAllSeeds();
        SeedStatistics stats = new SeedStatistics();

        System.out.println("=== Variety distribution ===");
        Map<Integer, Integer> distribution = stats.calculateVarietyDistribution(seeds);
        stats.displayVarietyDistribution(distribution);

        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int count : distribution.values()) {
            if (count > max) max = count;
            if (count < min) min = count;
        }
        if (max == min) {
            System.out.println("All varieties are equally represented (" + max + " kernels each) - the training set is perfectly balanced.");
        } else {
            System.out.println("Most common variety count: " + max + ", least common: " + min);
        }

        System.out.println("\n=== Organic kernels per region ===");
        Map<String, Integer> organicByRegion = stats.countOrganicByRegion(seeds);
        stats.displayOrganicByRegion(organicByRegion);
    }
}
