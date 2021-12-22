import util.Pair;
import util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.Vector.*;

public class Day20 {

    public static void main(String[] args) throws IOException {
        // Parse
        var input = Files.readString(Paths.get("c:\\dev\\input\\day_20.txt")).split("\n\n");
        var converter = input[0];
        var split = input[1].split("\n");
        var map = new HashMap<Vector, String>();
        for (int y = 0; y < split.length; y++) {
            var line = split[y];
            for (int x = 0; x < split.length; x++) {
                var pos = new Vector(x, y);
                if (line.charAt(x) == '#') {
                    map.put(pos, "1");
                }
            }
        }
        //print(map);
        List<Vector> ALL_DIR_WITH_DIAG = List.of(
                UP.add(LEFT), UP, UP.add(RIGHT),
                LEFT, ZERO, RIGHT,
                DOWN.add(LEFT), DOWN, DOWN.add(RIGHT));

        var previous = map;
        Pair<Vector, Vector> minMax = minMax(previous.keySet());
        for (int step = 0; step < 50; step++) {
            var next = new HashMap<Vector, String>();
            for (var x = minMax.left().x() - 1; x <= minMax.right().x() + 1; x++) {
                for (var y = minMax.left().y() - 1; y <= minMax.right().x() + 1; y++) {
                    var pos = new Vector(x, y);
                    StringBuilder sb = new StringBuilder();
                    for (Vector dir : ALL_DIR_WITH_DIAG) {
                        sb.append(previous.getOrDefault(pos.add(dir), step % 2 == 0 ? "0" : "1"));
                    }
                    int index = Integer.parseInt(sb.toString(), 2);
                    next.put(pos, converter.charAt(index) == '#' ? "1" : "0");
                }
            }
            previous = next;
            if (step == 1) {
                var part1 = previous.values().stream().filter(v -> v.equals("1")).count();
                System.out.println("Day 20, part 1 : " + part1); // 5571
            }
            minMax = new Pair<>(minMax.left().substract(new Vector(1, 1)), minMax.right().add(new Vector(1, 1)));
        }
        //print(previous);
        var part2 = previous.values().stream().filter(v -> v.equals("1")).count();
        System.out.println("Day 20, part 2 : " + part2); // 17965

    }


    public static void print(Map<Vector, String> m) {
        Map<Vector, Boolean> collect = m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().equals("1")));
        Vector.printMatrix(collect, "█", " ");
    }


}
