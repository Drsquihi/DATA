package com.seeds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part C: Data Access Object responsible for reading Seed objects from a CSV file.
 */
public class SeedCsvDAO {

    private static final String DELIMITER = ";";

    private String filename;

    public SeedCsvDAO(String filename) {
        this.filename = filename;
    }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    /** Reads the whole CSV file (skipping the header) and returns a List of Seed. */
    public List<Seed> getAllSeeds() {
        List<Seed> seeds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                seeds.add(parseSeedLine(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
        return seeds;
    }

    /** Parses one CSV data line into a Seed object. */
    private Seed parseSeedLine(String line) {
        String[] rawFields = line.split(DELIMITER);
        String[] fields = new String[rawFields.length];
        for (int i = 0; i < rawFields.length; i++) {
            fields[i] = rawFields[i].replace("\"", "").trim();
        }

        Seed seed = new Seed();
        seed.setArea(Double.parseDouble(fields[0]));
        seed.setPerimeter(Double.parseDouble(fields[1]));
        seed.setCompactness(Double.parseDouble(fields[2]));
        seed.setKernelLength(Double.parseDouble(fields[3]));
        seed.setKernelWidth(Double.parseDouble(fields[4]));
        seed.setAsymmetryCoefficient(Double.parseDouble(fields[5]));
        seed.setKernelGrooveLength(Double.parseDouble(fields[6]));
        seed.setOrganic(Boolean.parseBoolean(fields[7]));
        seed.setRegion(fields[8]);
        seed.setVariety(Integer.parseInt(fields[9]));
        return seed;
    }
}
