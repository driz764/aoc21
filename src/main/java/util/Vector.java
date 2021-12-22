package util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record Vector(int x, int y, int z) {
    public static final Vector ZERO = new Vector(0, 0, 0);
    public static final Vector UP = new Vector(0, -1, 0);
    public static final Vector DOWN = new Vector(0, 1, 0);
    public static final Vector RIGHT = new Vector(1, 0, 0);
    public static final Vector LEFT = new Vector(-1, 0, 0);
    public static final List<Vector> ALL_DIR = List.of(UP, DOWN, LEFT, RIGHT);
    public static final List<Vector> ALL_DIR_WITH_DIAG = List.of(UP, DOWN, LEFT, RIGHT, UP.add(LEFT),
            UP.add(RIGHT), DOWN.add(LEFT), DOWN.add(RIGHT));


    public Vector(int x, int y) {
        this(x, y, 0);
    }

    public static Vector fromString(String s) {
        String[] split = s.split(",");
        if (split.length == 2)
            return new Vector(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        return new Vector(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public Vector negate() {
        return new Vector(-x, -y, -z);
    }

    public Vector add(Vector o) {
        return new Vector(x + o.x, y + o.y, z + o.z);
    }

    public Vector substract(Vector o) {
        return add(o.negate());
    }

    public int xy() {
        return x * y;
    }

    public double distance(Vector o) {
        var dx = o.x - x;
        var dy = o.y - y;
        var dz = o.z - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static void printMatrix(Map<Vector, Boolean> data, String present, String absent) {
        var minX = data.keySet().stream().map(Vector::x).mapToInt(Integer::intValue).min().getAsInt();
        var minY = data.keySet().stream().map(Vector::y).mapToInt(Integer::intValue).min().getAsInt();
        var maxX = data.keySet().stream().map(Vector::x).mapToInt(Integer::intValue).max().getAsInt();
        var maxY = data.keySet().stream().map(Vector::y).mapToInt(Integer::intValue).max().getAsInt();
        for (int y = minY; y < maxY + 1; y++) {
            for (int x = minX; x < maxX + 1; x++) {
                System.out.print(data.getOrDefault(new Vector(x, y), false) ? present : absent);
            }
            System.out.println();
        }
    }

    public int dist2dlike() {
        if (x == 0) return Math.abs(y);
        return Math.abs(x);
    }

    public int distNoDiag(Vector o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y);
    }

    public int dist3D(Vector o){
        return Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
    }

    public Vector normlike() {
        if (x == 0) return new Vector(0, y / Math.abs(y));
        if (y == 0) return new Vector(x / Math.abs(x), 0);
        return new Vector(x / Math.abs(x), y / Math.abs(y));
    }

    public Vector mul(int i) {
        return new Vector(x * i, y * i);
    }

    public static Pair<Vector, Vector> minMax(Collection<Vector> in) {
        var minX = in.stream().map(Vector::x).mapToInt(Integer::intValue).min().getAsInt();
        var minY = in.stream().map(Vector::y).mapToInt(Integer::intValue).min().getAsInt();
        var maxX = in.stream().map(Vector::x).mapToInt(Integer::intValue).max().getAsInt();
        var maxY = in.stream().map(Vector::y).mapToInt(Integer::intValue).max().getAsInt();
        return new Pair<>(new Vector(minX,minY), new Vector(maxX,maxY));
    }
}
