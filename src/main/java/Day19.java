import util.Pair;
import util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day19 {


    public static void main(String[] args) throws IOException {
        // Parse
        var input = Arrays.stream(Files.readString(Paths.get("c:\\dev\\input\\day_19.txt")).split("\n\n"))
                .map(AllCube::new)
                .toList();

        AllCube start = input.get(0);
        start.fix(0);
        Queue<AllCube> q = new LinkedList<>();
        q.add(start);
        while (!q.isEmpty()) {
            var current = q.poll();
            for (AllCube ac : input) {
                if (ac.fixed != -1) continue;
                for (Cube other : ac.cubes) {
                    Optional<Vector> merge = current.get().merge(other);
                    if (merge.isPresent()) {
                        ac.fix(other, current.origin.add(merge.get()));
                        q.add(ac);
                        break;
                    }
                }
            }
        }

        int part1 = input.stream().map(AllCube::getAllVector).flatMap(Collection::stream).collect(Collectors.toSet()).size();
        System.out.println("Day 19, part 1 : " + part1); // 445

        List<Vector> vectors = input.stream().map(a -> a.origin).toList();
        int part2 = 0;
        for (Vector a : vectors) {
            for (Vector b : vectors) {
                var dist = a.dist3D(b);
                if (dist > part2) part2 = dist;
            }
        }
        System.out.println("Day 19, part 2 : " + part2); // 13225

    }
    //

    public static List<Vector> allRotationOf(Vector v) {
        int x = v.x(), y = v.y(), z = v.z();
        /*return List.of(
                new Vector(x, y, z),
        new Vector(y, z, x),
        new Vector(-z, y, x),
        new Vector(-x, z, y),
        new Vector(-z, -x, y),
        new Vector(-x, -y, z),
        new Vector(-y, x, z),
        new Vector(-z, -y, -x),
        new Vector(-y, z, -x),
        new Vector(y, x, -z),
        new Vector(x, z, -y),
        new Vector(z, -y, x),
        new Vector(z, x, y),
        new Vector(-z, x, -y),
        new Vector(z, -x, -y),
        new Vector(x, -y, -z),
        new Vector(y, -x, z),
        new Vector(y, -z, -x),
        new Vector(-y, -z, x),
        new Vector(z, y, -x),
        new Vector(-x, -z, -y),
        new Vector(-y, -x, -z),
        new Vector(x, -z, y),
        new Vector(-x, y, -z),
        new Vector(z, y, -x),
        new Vector(x, -z, y),
        new Vector(-x, z, y),
        new Vector(-y, z, -x),
        new Vector(-x, y, -z),
        new Vector(-z, -y, -x),
        new Vector(-z, x, -y),
        new Vector(x, -z, y),
        new Vector(-x, y, -z),
        new Vector(y, -x, z),
        new Vector(x, -z, y),
        new Vector(y, z, x),
        new Vector(z, x, y)
        );*/
        return List.of(
                new Vector(x, y, z),
                new Vector(x, y, -z),
                new Vector(x, -y, z),
                new Vector(x, -y, -z),
                new Vector(-x, y, z),
                new Vector(-x, y, -z),
                new Vector(-x, -y, z),
                new Vector(-x, -y, -z),
                new Vector(x, z, y),
                new Vector(x, z, -y),
                new Vector(x, -z, y),
                new Vector(x, -z, -y),
                new Vector(-x, z, y),
                new Vector(-x, z, -y),
                new Vector(-x, -z, y),
                new Vector(-x, -z, -y),
                new Vector(y, x, z),
                new Vector(y, x, -z),
                new Vector(y, -x, z),
                new Vector(y, -x, -z),
                new Vector(-y, x, z),
                new Vector(-y, x, -z),
                new Vector(-y, -x, z),
                new Vector(-y, -x, -z),
                new Vector(y, z, x),
                new Vector(-y, z, x),
                new Vector(y, -z, x),
                new Vector(y, z, -x),
                new Vector(-y, -z, x),
                new Vector(y, -z, -x),
                new Vector(-y, z, -x),
                new Vector(-y, -z, -x),
                new Vector(z, x, y),
                new Vector(-z, x, y),
                new Vector(z, -x, y),
                new Vector(z, x, -y),
                new Vector(-z, -x, y),
                new Vector(z, -x, -y),
                new Vector(-z, x, -y),
                new Vector(-z, -x, -y),
                new Vector(z, y, x),
                new Vector(-z, y, x),
                new Vector(z, -y, x),
                new Vector(z, y, -x),
                new Vector(-z, -y, x),
                new Vector(z, -y, -x),
                new Vector(-z, y, -x),
                new Vector(-z, -y, -x)
        );/*
        Vector        List<Vector> result = new ArrayList<>();
        for (var x : List.of(1, -1)) {
            for (var y : List.of(1, -1)) {
//                for (var z : List.of(1, -1)) {
                var r = new Vector(v.x() * x, v.y() * y, v.z());
                result.add(r);
                result.add(new Vector(r.y(), r.z(), r.x()));
                result.add(new Vector(r.z(), r.x(), r.y()));
                result.add(new Vector(r.y(), r.x(), r.z()));
                result.add(new Vector(r.x(), r.z(), r.y()));
                result.add(new Vector(r.z(), r.y(), r.x()));
//                }
            }
        }
        return result;*/
    }

    static class AllCube {

        List<Cube> cubes = new ArrayList<>();
        int fixed = -1;
        Vector origin;

        AllCube(String s) {
            var i = s.lines().skip(1).map(Vector::fromString)
                    .map(Day19::allRotationOf)
                    .toList();
            for (int j = 0; j < 48; j++) {
                var c = new Cube();
                for (List<Vector> vectors : i) {
                    c.data.add(vectors.get(j));
                }
                c.computeVec();
                cubes.add(c);
            }
        }

        void fix(int i) {
            fixed = i;
            origin = Vector.ZERO;
        }

        void fix(Cube c, Vector move) {
            fixed = cubes.indexOf(c);
            origin = move;
        }

        Cube get() {
            return cubes.get(fixed);
        }

        List<Vector> getAllVector() {
            return cubes.get(fixed).data.stream().map(v -> v.substract(origin)).toList();
        }
    }

    static class Cube {
        List<Vector> data;
        Map<Vector, Pair<Vector, Vector>> allVec = new HashMap<>();

        Cube(String s) {
            data = s.lines().skip(1).map(Vector::fromString).toList();
            computeVec();
        }

        public Cube() {
            data = new ArrayList<>();

        }

        void computeVec() {
            for (var a : data) {
                for (var b : data) {
                    if (a == b) continue;
                    Vector move = b.substract(a);
                    if (allVec.containsKey(move)) {
                        System.out.println("already in");
                    }
                    allVec.put(move, new Pair<>(a, b));
                }
            }
        }

        Optional<Vector> merge(Cube other) {
            var inter = new HashSet<>(this.allVec.keySet());
            inter.retainAll(other.allVec.keySet());
            if (inter.size() >= 132) {
//                System.out.println("found commun " + inter.size());
                List<Vector> moves = new ArrayList<>();
                for (Vector next : inter) {
                    var a = this.allVec.get(next).left();
                    var b = other.allVec.get(next).left();
                    var move = b.substract(a);
                    // System.out.println(move);
                    moves.add(move);
                }
                var moveResultMap = moves.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                Optional<Vector> moveResult = moveResultMap.entrySet().stream()
                        .filter(e -> e.getValue() >= 132)
                        .map(Map.Entry::getKey)
                        .findFirst();
//                System.out.println(moveResultMap);
//                System.out.println();
                return moveResult;
            }
            return Optional.empty();
        }


    }


}
