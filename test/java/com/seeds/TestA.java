package com.seeds;

import java.util.List;

public class TestA {
    public static void main(String[] args) {
        String filename = "data/seeds-train.csv";
        RawSeedReader reader = new RawSeedReader();

        // a. Print the first 2 lines
        List<String> lines = reader.readAllLines(filename);
        System.out.println("--- First 2 raw lines ---");
        System.out.println(lines.get(0));
        System.out.println(lines.get(1));

        // b. Split on ';'
        System.out.println("\n--- Split first data line ---");
        String[] fields = reader.splitLine(lines.get(1));
        for (String f : fields) {
            System.out.print("[" + f + "] ");
        }
        System.out.println();

        // c. Convert into mixed-type array
        System.out.println("\n--- Parsed first data line ---");
        Object[] seed = reader.parseFields(fields);
        for (Object o : seed) {
            System.out.print(o + " (" + o.getClass().getSimpleName() + ")  ");
        }
        System.out.println();

        // Full file
        List<Object[]> allSeeds = reader.readAllSeeds(filename);
        System.out.println("\nTotal seeds parsed: " + allSeeds.size());
    }
}
