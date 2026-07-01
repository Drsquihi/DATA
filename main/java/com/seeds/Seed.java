package com.seeds;

/**
 * Part C: proper object-oriented representation of a wheat kernel.
 */
public class Seed {

    private double area;
    private double perimeter;
    private double compactness;
    private double kernelLength;
    private double kernelWidth;
    private double asymmetryCoefficient;
    private double kernelGrooveLength;
    private boolean organic;
    private String region;
    private int variety;

    private static final String[] FEATURE_NAMES = {
            "Area", "Perimeter", "Compactness", "Kernel Length",
            "Kernel Width", "Asymmetry Coeff.", "Kernel Groove Length"
    };
    private static final String[] VARIETY_NAMES = {"", "Kama", "Rosa", "Canadian"};

    public Seed() {
    }

    public Seed(double area, double perimeter, double compactness, double kernelLength,
                double kernelWidth, double asymmetryCoefficient, double kernelGrooveLength,
                boolean organic, String region, int variety) {
        this.area = area;
        this.perimeter = perimeter;
        this.compactness = compactness;
        this.kernelLength = kernelLength;
        this.kernelWidth = kernelWidth;
        this.asymmetryCoefficient = asymmetryCoefficient;
        this.kernelGrooveLength = kernelGrooveLength;
        this.organic = organic;
        this.region = region;
        this.variety = variety;
    }

    // --- Getters / setters ---

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public double getPerimeter() { return perimeter; }
    public void setPerimeter(double perimeter) { this.perimeter = perimeter; }

    public double getCompactness() { return compactness; }
    public void setCompactness(double compactness) { this.compactness = compactness; }

    public double getKernelLength() { return kernelLength; }
    public void setKernelLength(double kernelLength) { this.kernelLength = kernelLength; }

    public double getKernelWidth() { return kernelWidth; }
    public void setKernelWidth(double kernelWidth) { this.kernelWidth = kernelWidth; }

    public double getAsymmetryCoefficient() { return asymmetryCoefficient; }
    public void setAsymmetryCoefficient(double asymmetryCoefficient) { this.asymmetryCoefficient = asymmetryCoefficient; }

    public double getKernelGrooveLength() { return kernelGrooveLength; }
    public void setKernelGrooveLength(double kernelGrooveLength) { this.kernelGrooveLength = kernelGrooveLength; }

    public boolean isOrganic() { return organic; }
    public void setOrganic(boolean organic) { this.organic = organic; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public int getVariety() { return variety; }
    public void setVariety(int variety) { this.variety = variety; }

    public String getVarietyName() {
        return (variety >= 1 && variety <= 3) ? VARIETY_NAMES[variety] : "Unknown";
    }

    /** Returns the 7 numeric features as an array, always in the same order. */
    public double[] getFeatures() {
        return new double[]{
                area, perimeter, compactness, kernelLength,
                kernelWidth, asymmetryCoefficient, kernelGrooveLength
        };
    }

    /** Replaces the 7 numeric features from an array, in the same order as getFeatures(). */
    public void setFeatures(double[] features) {
        if (features.length != 7) {
            throw new IllegalArgumentException("Expected 7 features, got " + features.length);
        }
        area = features[0];
        perimeter = features[1];
        compactness = features[2];
        kernelLength = features[3];
        kernelWidth = features[4];
        asymmetryCoefficient = features[5];
        kernelGrooveLength = features[6];
    }

    /**
     * Returns a new Seed whose 7 features are normalized to the 0-1 range
     * using the provided min/max arrays (same order as getFeatures()).
     * organic, region and variety are copied unchanged.
     */
    public Seed normalize(double[] min, double[] max) {
        double[] features = getFeatures();
        double[] normalized = new double[7];
        for (int i = 0; i < 7; i++) {
            double range = max[i] - min[i];
            normalized[i] = (range == 0) ? 0 : (features[i] - min[i]) / range;
        }
        Seed result = new Seed();
        result.setFeatures(normalized);
        result.setOrganic(this.organic);
        result.setRegion(this.region);
        result.setVariety(this.variety);
        return result;
    }

    public void displayProfile() {
        System.out.println("Seed Profile:");
        System.out.println("Area: " + area);
        System.out.println("Perimeter: " + perimeter);
        System.out.println("Compactness: " + compactness);
        System.out.println("Kernel Length: " + kernelLength);
        System.out.println("Kernel Width: " + kernelWidth);
        System.out.println("Asymmetry Coefficient: " + asymmetryCoefficient);
        System.out.println("Kernel Groove Length: " + kernelGrooveLength);
        System.out.println("Organic: " + organic);
        System.out.println("Region: " + region);
        System.out.println("Variety: " + variety + " (" + getVarietyName() + ")");
    }

    public void displayFeatureBars(double[] min, double[] max) {
        Seed normalized = normalize(min, max);
        double[] values = normalized.getFeatures();
        for (int i = 0; i < FEATURE_NAMES.length; i++) {
            int barLength = (int) Math.round(values[i] * 20);
            System.out.println(FEATURE_NAMES[i] + " | " + "#".repeat(Math.max(0, barLength)));
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Seed{area=%.3f, perimeter=%.3f, compactness=%.4f, kernelLength=%.3f, " +
                        "kernelWidth=%.3f, asymmetry=%.3f, grooveLength=%.3f, organic=%s, region=%s, variety=%d}",
                area, perimeter, compactness, kernelLength, kernelWidth,
                asymmetryCoefficient, kernelGrooveLength, organic, region, variety);
    }
}
