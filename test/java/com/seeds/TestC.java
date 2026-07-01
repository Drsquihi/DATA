package com.seeds;

import java.util.List;

public class TestC {
    public static void main(String[] args) {
        SeedCsvDAO dao = new SeedCsvDAO("data/seeds-train.csv");
        List<Seed> seeds = dao.getAllSeeds();

        System.out.println("Total seeds loaded: " + seeds.size());

        Seed first = seeds.get(0);
        System.out.println("\n--- First seed (toString) ---");
        System.out.println(first);

        System.out.println("\n--- First seed (displayProfile) ---");
        first.displayProfile();

        System.out.println("\n--- First seed feature array ---");
        double[] features = first.getFeatures();
        for (double f : features) {
            System.out.print(f + " ");
        }
        System.out.println();
    }
}
