import util.Pair;
import util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) throws IOException {
        // Parse
        var data = Files.readString(Paths.get("c:\\dev\\input\\" + "day_13.txt"));
        String[] split = data.split("\\n\\n");
        var paper = split[0].lines().map(Vector::fromString).collect(Collectors.toSet());
        var moves = split[1].lines().map(s -> {
            String[] split1 = s.split("=");
            return new Pair<>(split1[0].charAt(split1[0].length() - 1), Integer.parseInt(split1[1]));
        }).collect(Collectors.toList());

        var part1Move = moves.get(0);
        var part1 = paper.stream()
                .map(pos -> fold(part1Move.left(), part1Move.right(), pos))
                .collect(Collectors.toSet());
        System.out.println("Day 13, Part 1 : " + part1.size()); // < 859

        moves.remove(0);
        var part2 = part1.stream()
                .map(pos -> {
                    var result = pos;
                    for (var m : moves) {
                        result = fold(m.left(), m.right(), result);
                    }
                    return result;
                }).collect(Collectors.toSet());
        /*
        var part2 = part1.stream()
                .flatMap(pos -> moves.stream().map(m -> fold(m.left(), m.right(), pos)))
                .collect(Collectors.toSet());
         */
        Map<Vector, Boolean> collect = part2.stream().collect(Collectors.toMap(Function.identity(), a -> true));
        System.out.println("Day 13, Part 2 : ");
        Vector.printMatrix(collect, "█", " ");

    }


    public static Vector fold(char axe, int axePos, Vector point) {
        if (axe == 'x' && axePos < point.x()) {
            return new Vector(axePos - (point.x() - axePos), point.y());
        } else if (axe == 'y' && axePos < point.y()) {
            return new Vector(point.x(), axePos - (point.y() - axePos));
        }
        return point;
    }

}