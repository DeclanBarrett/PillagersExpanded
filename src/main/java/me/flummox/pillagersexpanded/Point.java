package me.flummox.pillagersexpanded;

public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Here you are. This is what you want to implement.
     * from.moveTo(0.0, to) => from
     * from.moveTo(1.0, to) => to
     *
     * @param by - from 0.0 to 1.0 (from 0% to 100%)
     * @param target - move toward target by delta
     */
    public Point moveTo(double by, Point target) {
        Point delta = target.subtract(this);
        return add(delta.dot(by));
    }

    public Point add(Point point) {
        return new Point(x + point.x, y + point.y);
    }

    public Point subtract(Point point) {
        return new Point(x - point.x, y - point.y);
    }

    public Point dot(double v) {
        return new Point(v * x, v * y);
    }

    public double dist(Point point) {
        return subtract(point).length();
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public String toString() {
        return x + ":" + y;
    }
}
