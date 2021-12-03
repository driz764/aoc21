import util.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws IOException {
        List<Integer> input = Parser.parse("day_1.txt", Integer::parseInt);

        System.out.println("Day 1, Part 1 : " + part1(input));
        System.out.println("Day 1, Part 2 : " + part2(input));
    }

    public static int part1(List<Integer> input) {
        return countIncreasing(input);
    }

    public static int part2(List<Integer> input) {
        List<Integer> summed = new ArrayList<>();
        for (int i = 2; i < input.size(); i++) {
            summed.add(input.get(i - 2) + input.get(i - 1) + input.get(i));
        }
        return countIncreasing(summed);
    }

    public static int countIncreasing(List<Integer> input) {
        var result = 0;
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i - 1) < input.get(i)) {
                result++;
            }
        }
        return result;
    }
}
