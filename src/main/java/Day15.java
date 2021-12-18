import org.apache.commons.lang3.time.StopWatch;
import util.Pair;
import util.Parser;
import util.Vector;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;

public class Day15 {

    static Map<Pair<Character, Character>, Character> insertionRules;

    public static void main(String[] args) throws IOException {
        // Parse
        var data = Parser.parse("day_15.txt", l -> l);
        Map<Vector, Integer> input = new HashMap<>();
        for (int y = 0; y < data.size(); y++) {
            char[] chars = data.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                int val = Integer.parseInt(chars[x] + "");
                input.put(new Vector(x, y), val);
            }
        }
        Map<Vector, List<Pair<Vector, Integer>>> graph = new HashMap<>();
        for (Map.Entry<Vector, Integer> v : input.entrySet()) {
            Vector pos = v.getKey();
            for (Vector dir : Vector.ALL_DIR) {
                Vector voisin = pos.add(dir);
                if (!input.containsKey(voisin)) continue;
                Integer cost = input.get(voisin);
                graph.merge(pos, List.of(new Pair<>(voisin, cost)), (a, b) -> Stream.of(a, b)
                        .flatMap(Collection::stream)
                        .toList());
            }
        }
        var end = new Vector(data.get(0).length() - 1, data.size() - 1);
        var result = astar(graph, new Vector(0, 0), end);

        System.out.println("Day 15, Part 1 : " + result.right()); // 540

        //printPath(result.left(), end);
        x5(input, end);
        Map<Vector, List<Pair<Vector, Integer>>> graph2 = new HashMap<>();
        for (Map.Entry<Vector, Integer> v : input.entrySet()) {
            Vector pos = v.getKey();
            for (Vector dir : Vector.ALL_DIR) {
                Vector voisin = pos.add(dir);
                if (!input.containsKey(voisin)) continue;
                Integer cost = input.get(voisin);
                graph2.merge(pos, List.of(new Pair<>(voisin, cost)), (a, b) -> Stream.of(a, b)
                        .flatMap(Collection::stream)
                        .toList());
            }
        }
        end = new Vector((end.x() + 1) * 5 - 1, (end.y() + 1) * 5 - 1);
        StopWatch sw = StopWatch.createStarted();
        result = astar(graph2, new Vector(0, 0), end);
        sw.stop();
        System.out.println("Day 15, Part 2 : " + result.right() + " in " + sw.formatTime() + "ms"); // 2879

    }

    public static Pair<Map<Vector, Vector>, Integer> astar(Map<Vector, List<Pair<Vector, Integer>>> graph, Vector start, Vector end) {
        var explored = new HashSet<Vector>();
        Map<Vector, Vector> path = new HashMap<>();
        Map<Vector, Integer> score = new HashMap<>();
        score.put(start, 0);
        Comparator<Vector> objectComparator = comparingInt(v -> v.distNoDiag(end) + score.getOrDefault(v, Integer.MAX_VALUE));
        Queue<Vector> queue = new PriorityQueue<>(objectComparator);
        queue.add(start);
        while (!queue.isEmpty()) {
            var current = queue.poll();
            explored.add(current);
            if (current.equals(end)) {
                System.out.println("Explored : " + explored.size() + "/" + graph.size());
                return new Pair<>(path, score.get(current));
            }
            for (Pair<Vector, Integer> neighbor : graph.get(current)) {
                var currentScore = score.get(current) + neighbor.right();
                if (currentScore < score.getOrDefault(neighbor.left(), Integer.MAX_VALUE)) {
                    //path.put(neighbor.left(), current);
                    score.put(neighbor.left(), currentScore);
                    if (!queue.contains(neighbor.left())) {
                        queue.add(neighbor.left());
                    }
                }
            }
        }
        throw new RuntimeException("Can't find path to target");
    }


    public static void printPath(Map<Vector, Vector> path, Vector end) {
        var current = end;
        List<Vector> pathLs = new ArrayList<>();
        pathLs.add(end);
        while (true) {
            var prev = path.get(current);
            if (prev == null) break;
            pathLs.add(prev);
            current = prev;
        }
        for (int i = pathLs.size() - 1; i >= 0; i--) {
            System.out.println(pathLs.get(i));
        }
    }


    public static void x5(Map<Vector, Integer> input, Vector end) {
        int sizeX = end.x() + 1;
        int sizeY = end.y() + 1;
        for (int line = 0; line < 5; line++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = line * sizeY; y < (line + 1) * sizeY; y++) {
                    Vector initPos = new Vector(x, y);
                    int prev = input.get(initPos);
                    for (int step = 1; step < 5; step++) {
                        prev = prev == 9 ? 1 : prev + 1;
                        input.put(initPos.add(Vector.RIGHT.mul(step * sizeX)), prev);
                        if (step == 1 && line != 4) input.put(initPos.add(Vector.DOWN.mul(step * sizeY)), prev);
                    }
                }
            }
        }
    }

}

/*

 */