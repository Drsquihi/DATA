package com.seeds;

import java.util.List;
import java.util.Map;

public class TestE {
    public static void main(String[] args) {
        SeedCsvDAO dao = new SeedCsvDAO("data/seeds-train.csv");
        List<Seed> seeds = dao.getAllSeeds();
        SeedStatistics stats = new SeedStatistics();

        Map<Integer, Seed> profiles = stats.trainVarietyProfiles(seeds);
        System.out.println("=== Average variety profiles ===");
        stats.displayVarietyProfiles(profiles);

        System.out.println("\n=== Largest kernels (by area) ===");
        int largest = -1;
        double maxArea = -1;
        for (Map.Entry<Integer, Seed> e : profiles.entrySet()) {
            if (e.getValue().getArea() > maxArea) {
                maxArea = e.getValue().getArea();
                largest = e.getKey();
            }
        }
        System.out.println("Variety with largest average kernels: " + largest +
                " (" + profiles.get(largest).getVarietyName() + "), area = " + maxArea);
    }
}
