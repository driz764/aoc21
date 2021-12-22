import util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Day22 {

    public static void main(String[] args) throws IOException {
        // Parse
        var input = Arrays.stream(Files.readString(Paths.get("c:\\dev\\input\\day_22.txt")).split("\n"))
                .map(s -> {
                    String[] s1 = s.replaceAll(" ", ",")
                            .replaceAll("\\.\\.", ",")
                            .replaceAll("x=", "")
                            .replaceAll("y=", "")
                            .replaceAll("z=", "")
                            .split(",");
                    return new Range(parseInt(s1[1]), parseInt(s1[3]), parseInt(s1[5]),
                            parseInt(s1[2]), parseInt(s1[4]), parseInt(s1[6]),
                            s1[0]);
                }).collect(Collectors.toList());
        Collections.reverse(input);
        var part1 = 0;
        for (int x = -50; x <= 50; x++) {
            for (int y = -50; y <= 50; y++) {
                for (int z = -50; z <= 50; z++) {
                    for (Range range : input) {
                        if (range.contains(x, y, z)) {
                            if (range.onOff.equals("on")) {
                                part1++;
                            }
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("Day 22, Part 1 : " + part1); // 615700

        /* Part 2 :
        - subdivise space on each point ( 8 par ligne de l'input)
        - why not in octree
        - how to find if on or off ? mi brut force en premier un point pour sudivision pour remonter les input comme la part 1 ?
*/
        var xMax = input.stream().mapToLong(Range::xMax).max().getAsLong();
        var yMax = input.stream().mapToLong(Range::yMax).max().getAsLong();
        var zMax = input.stream().mapToLong(Range::zMax).max().getAsLong();
        var xMin = input.stream().mapToLong(Range::xMin).min().getAsLong();
        var yMin = input.stream().mapToLong(Range::yMin).min().getAsLong();
        var zMin = input.stream().mapToLong(Range::zMin).min().getAsLong();

        //System.out.println(xMin);
        System.out.println((xMax - xMin) * (yMax - yMin) * (zMax - zMin));
        var part2 = 0L;
        /*for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    for (Range range : input) {
                        if (range.contains(x, y, z)) {
                            if (range.onOff.equals("on")) {
                                part2++;
                            }
                            break;
                        }
                    }
                }
            }
        }*/
        System.out.println("Day 22, Part 2 : " + part2);
        Octree octree = new Octree(Vector.ZERO);
        for (var r : input) {
            octree.add(new Vector(r.xMin, r.yMin, r.zMin));
            octree.add(new Vector(r.xMin, r.yMin, r.zMax));
            octree.add(new Vector(r.xMin, r.yMax, r.zMin));
            octree.add(new Vector(r.xMin, r.yMax, r.zMax));
            octree.add(new Vector(r.xMax, r.yMin, r.zMin));
            octree.add(new Vector(r.xMax, r.yMin, r.zMax));
            octree.add(new Vector(r.xMax, r.yMax, r.zMin));
            octree.add(new Vector(r.xMax, r.yMax, r.zMax));
            break;
        }
        System.out.println(octree);
    }

    record Range(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, String onOff) {
        boolean contains(int x, int y, int z) {
            return x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
        }
    }


    static class Octree {
        Vector pos;
        Octree[] children = new Octree[8];

        public Octree(Vector v) {
            this.pos = v;
        }

        public void add(Vector v) {
            var index = -1;
            if (pos.x() >= v.x() && pos.y() >= v.y() && pos.z() >= v.z()) {
                index = 0;
            } else if (pos.x() >= v.x() && pos.y() >= v.y() && pos.z() < v.z()) {
                index = 1;
            } else if (pos.x() >= v.x() && pos.y() < v.y() && pos.z() >= v.z()) {
                index = 2;
            } else if (pos.x() >= v.x() && pos.y() < v.y() && pos.z() < v.z()) {
                index = 3;
            } else if (pos.x() < v.x() && pos.y() >= v.y() && pos.z() >= v.z()) {
                index = 4;
            } else if (pos.x() < v.x() && pos.y() >= v.y() && pos.z() < v.z()) {
                index = 5;
            } else if (pos.x() < v.x() && pos.y() < v.y() && pos.z() >= v.z()) {
                index = 6;
            } else if (pos.x() < v.x() && pos.y() < v.y() && pos.z() < v.z()) {
                index = 7;
            }
            if (children[index] == null)
                children[index] = new Octree(v);
            else
                children[index].add(v);
        }
    }

}
