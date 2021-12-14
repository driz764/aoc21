import util.Parser;
import util.Vector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void main(String[] args) throws IOException {
        List<Move> input = getMoves("day_5.txt");
        System.out.println("Day 5, part 1 : " + part1(input)); //  6397
        System.out.println("Day 5, part 2 : " + solve2(input)); // 22335
    }

    public record Move(Vector origin, Vector direction, int distance) {
    }

    static List<Move> getMoves(String file) throws IOException {
        return Parser.parse(file, s -> {
            var split = s.split(" -> ");
            var from = Vector.fromString(split[0]);
            var to = Vector.fromString(split[1]);
            var vector = to.substract(from);
            return new Move(from, vector.normlike(), vector.dist2dlike());
        });
    }

    public static long solve(List<Move> input) {
        Map<Vector, Integer> result = new HashMap<>();
        input.forEach(m -> {
            for (int i = 0; i <= m.distance(); i++) {
                var pos = m.origin().add(m.direction().mul(i));
                var count = result.getOrDefault(pos, 0);
                result.put(pos, count + 1);
            }
        });
        return result.values().stream().filter(i -> i > 1).count();
    }

    public static long solve2(List<Move> input) {
        int[][] result2 = new int[1000][1000];
        input.forEach(m -> {
            for (int i = 0; i <= m.distance(); i++) {
                var pos = m.origin().add(m.direction().mul(i));
                result2[pos.x()][pos.y()] += 1;

            }
        });
        var count = 0;
        for (int[] i : result2) {
            for (int ij : i) {
                if (ij > 1) count++;
            }
        }
        return count;
    }

    public static long part1(List<Move> input) {
        List<Move> noDiag = input.stream().filter(m -> m.direction.xy() == 0).toList();
        return solve(noDiag);
    }

}
