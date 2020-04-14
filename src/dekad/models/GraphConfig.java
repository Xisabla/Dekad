package dekad.models;

public class GraphConfig {

    private double xMin = -10;

    private double xMax = 10;

    private double yMin = -10;

    private double yMax = 10;

    private double offset = 0.01;

    private boolean autoYBounds = true;

    private double autoYBoundsFactor = 2;

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

    public boolean hasAutoYBounds() {
        return autoYBounds;
    }

    public void setAutoYBounds(final boolean autoYBounds) {
        this.autoYBounds = autoYBounds;
    }

    public double getAutoYBoundsFactor() {
        return autoYBoundsFactor;
    }

    public void setAutoYBoundsFactor(final double autoYBoundsFactor) {
        this.autoYBoundsFactor = autoYBoundsFactor;
    }
}
