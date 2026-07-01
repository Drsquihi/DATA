package com.seeds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part A: raw, "beginner style" reading of the CSV file, before any
 * refactoring into proper classes (done later in Part C).
 *
 * Each seed is represented as a plain array of 10 Objects, in the same
 * order as the CSV columns:
 *   [0..6] double  -> the 7 geometric measurements
 *   [7]    boolean -> organic
 *   [8]    String  -> region
 *   [9]    int     -> variety
 */
public class RawSeedReader {

    private static final String DELIMITER = ";";

    /**
     * A.a - Reads every line of the CSV file as-is (no parsing yet) and
     * returns them as a List of String.
     */
    public List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
        return lines;
    }

    /**
     * A.b - Splits a raw CSV line on the ';' delimiter and strips the
     * surrounding double quotes from each field.
     */
    public String[] splitLine(String line) {
        String[] rawFields = line.split(DELIMITER);
        String[] fields = new String[rawFields.length];
        for (int i = 0; i < rawFields.length; i++) {
            fields[i] = rawFields[i].replace("\"", "").trim();
        }
        return fields;
    }

    /**
     * A.c - Converts one already-split data line (header excluded) into a
     * mixed-type array: 7 doubles, 1 boolean, 1 String, 1 int.
     */
    public Object[] parseFields(String[] fields) {
        Object[] seed = new Object[10];
        for (int i = 0; i < 7; i++) {
            seed[i] = Double.parseDouble(fields[i]);
        }
        seed[7] = Boolean.parseBoolean(fields[7]);   // organic
        seed[8] = fields[8];                          // region
        seed[9] = Integer.parseInt(fields[9]);         // variety
        return seed;
    }

    /**
     * Convenience method: reads the whole file and returns every data row
     * (header skipped) already parsed into Object[] rows.
     */
    public List<Object[]> readAllSeeds(String filename) {
        List<String> lines = readAllLines(filename);
        List<Object[]> seeds = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) { // skip header line
            String line = lines.get(i);
            if (line.isBlank()) continue;
            seeds.add(parseFields(splitLine(line)));
        }
        return seeds;
    }
}
