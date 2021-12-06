package util;

import java.util.Map;

public record Vector(int x, int y, int z) {
    public static final Vector ZERO = new Vector(0, 0, 0);

    public Vector(int x, int y) {
        this(x, y, 0);
    }

    public static Vector fromString(String s) {
        String[] split = s.split(",");
        return new Vector(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
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

    public static void printMatrix(Map<Vector, Integer> data) {
        var maxX = data.keySet().stream().map(Vector::x).mapToInt(Integer::intValue).max().getAsInt();
        var maxY = data.keySet().stream().map(Vector::y).mapToInt(Integer::intValue).max().getAsInt();
        for (int y = 0; y < maxY + 1; y++) {
            for (int x = 0; x < maxX + 1; x++) {
                System.out.print(data.getOrDefault(new Vector(x, y), 0));
            }
            System.out.println();
        }
    }

    public int dist2dlike() {
        if (x == 0) return Math.abs(y);
        return Math.abs(x);
    }

    public Vector normlike() {
        if (x == 0) return new Vector(0, y / Math.abs(y));
        if (y == 0) return new Vector(x / Math.abs(x), 0);
        return new Vector(x / Math.abs(x), y / Math.abs(y));
    }

    public Vector mul(int i) {
        return new Vector(x * i, y * i);
    }
}
