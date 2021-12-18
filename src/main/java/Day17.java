import util.Vector;

import java.io.IOException;
import java.util.Optional;

public class Day17 {

    // target area: x=88..125, y=-157..-103
    static Vector min = new Vector(88, -157);
    static Vector max = new Vector(125, -103);

    public static void main(String[] args) throws IOException {
        System.out.println((min.y() * (min.y() + 1)) / 2);
        var part1 = 0;
        var part2 = 0;
        for (int dx = 1; dx <= 125; dx++) {
            for (int dy = min.y(); dy < Math.abs(min.y()); dy++) {
                Optional<Integer> result = run(new Vector(dx, dy));
                if (result.isPresent()) {
                    if (result.get() > part1) part1 = result.get();
                    part2++;
                }
            }
        }
        System.out.println("Day 17, Part 1 : " + part1); // 12246
        System.out.println("Day 17, Part 2 : " + part2); //  3528
    }

    public static Optional<Integer> run(Vector init) {
        var maxY = 0;
        var pos = Vector.ZERO;
        var move = init;
        do {
            pos = pos.add(move);
            if (pos.y() > maxY) maxY = pos.y();
            move = applyGravityAndStuff(move);
            if (onTarget(pos)) return Optional.of(maxY);
        } while (stillGood(pos));
        return Optional.empty();
    }

    private static boolean onTarget(Vector pos) {
        return pos.x() >= min.x() && pos.x() <= max.x() && pos.y() >= min.y() && pos.y() <= max.y();
    }

    private static boolean stillGood(Vector pos) {
        return !(pos.y() <= min.y() || pos.x() >= max.x());
    }

    private static Vector applyGravityAndStuff(Vector move) {
        return new Vector(
                move.x() == 0 ? 0 : move.x() - 1,
//                move.x() == 0 ? 0 : move.x() - (Math.abs(move.x()) / move.x()),
                move.y() - 1
        );
    }


}

/*

 */