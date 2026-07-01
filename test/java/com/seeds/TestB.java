package com.seeds;

import java.util.List;

public class TestB {
    public static void main(String[] args) {
        String filename = "data/seeds-train.csv";
        RawSeedReader reader = new RawSeedReader();
        List<Object[]> allSeeds = reader.readAllSeeds(filename);
        SeedAnalyzer analyzer = new SeedAnalyzer();

        // Line 10 and line 50 of the file correspond to data rows 9 and 49
        // (0-indexed list, after the header line has already been removed)
        Object[] seed10 = allSeeds.get(8);  // "line 10" = 9th data row (header is line 1)
        Object[] seed50 = allSeeds.get(48); // "line 50" = 49th data row

        System.out.println("=== Kernel from line 10 ===");
        analyzer.displaySeedProfile(seed10);
        System.out.println();
        analyzer.displayFeatureBars(seed10, allSeeds);

        System.out.println("\n=== Kernel from line 50 ===");
        analyzer.displaySeedProfile(seed50);
        System.out.println();
        analyzer.displayFeatureBars(seed50, allSeeds);

        System.out.println("\nVariety of kernel #10: " + seed10[9]);
        System.out.println("Variety of kernel #50: " + seed50[9]);
    }
}
