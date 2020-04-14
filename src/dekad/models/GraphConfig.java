package dekad.models;

public class GraphConfig {

    private double xMin = -10;

    private double xMax = 10;

    private double yMin = -10;

    private double yMax = 10;

    private double offset = 0.01;

    private double computingOffsetRatio = 20;

    private boolean computedYBounds = true;

    private double computedYBoundsFactor = 1.1;

    public double getXMin() {
        return xMin;
    }

    public void setXMin(final double xMin) {
        this.xMin = xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(final double xMax) {
        this.xMax = xMax;
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(final double yMin) {
        this.yMin = yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(final double yMax) {
        this.yMax = yMax;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(final double offset) {
        this.offset = offset;
    }

    public double getComputingOffsetRatio() {
        return computingOffsetRatio;
    }

    public void setComputingOffsetRatio(double computingOffsetRatio) {
        this.computingOffsetRatio = computingOffsetRatio;
    }

    public boolean hasComputedYBounds() {
        return computedYBounds;
    }

    public void setComputedYBounds(final boolean computedYBounds) {
        this.computedYBounds = computedYBounds;
    }

    public double getComputedYBoundsFactor() {
        return computedYBoundsFactor;
    }

    public void setComputedYBoundsFactor(final double computedYBoundsFactor) {
        this.computedYBoundsFactor = computedYBoundsFactor;
    }
}
