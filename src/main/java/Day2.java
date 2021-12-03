import util.Parser;
import util.Vector;

import java.io.IOException;
import java.util.List;

public class Day2 {

    record Ordre(Direction direction, int offset) {
        static Ordre fromInput(String input) {
            String[] ss = input.split(" ");
            return new Ordre(Direction.valueOf(ss[0].toUpperCase()), Integer.parseInt(ss[1]));
        }

        public Vector toMoveDay1() {
            return switch (direction) {
                case FORWARD -> new Vector(offset, 0);
                case DOWN -> new Vector(0, offset);
                case UP -> new Vector(0, -offset);
            };
        }
    }

    enum Direction {FORWARD, DOWN, UP}


    public static void main(String[] args) throws IOException {
        List<Ordre> input = Parser.parse("day_2.txt", Ordre::fromInput);


        System.out.println("Day 2, Part 1 : " + part1(input));

        // Part 2
        // Dont do this : will not work on // stream
        // coz reducer depend on previous reduce
        System.out.println("Day 2, Part 2 : " + part2(input));
    }

    public static int part1(List<Ordre> input) {
        return input.stream()
                .map(Ordre::toMoveDay1)
                .reduce(Vector.ZERO, Vector::add)
                .xy();
    }

    public static int part2(List<Ordre> input) {
        // Dont do this : will not work on // stream
        // coz reducer depend on previous reduce
        return input.stream()
                .reduce(Vector.ZERO,
                        (position, ordre) -> position.add(
                                switch (ordre.direction()) {
                                    case FORWARD -> new Vector(ordre.offset(), position.z() * ordre.offset());
                                    case DOWN -> new Vector(0, 0, ordre.offset());
                                    case UP -> new Vector(0, 0, -ordre.offset());
                                }),
                        (a, b) -> a)
                .xy();
    }

}
