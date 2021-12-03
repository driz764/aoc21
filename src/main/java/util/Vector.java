package util;

public record Vector(int x, int y, int z) {
    public static final Vector ZERO = new Vector(0, 0, 0);

    public Vector(int x, int y) {
        this(x, y, 0);
    }

    public Vector negate(){
        return new Vector(-x,-y,-z);
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
}
