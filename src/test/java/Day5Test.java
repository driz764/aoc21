import org.junit.jupiter.api.Test;
import util.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

    @Test
    void part1() throws IOException {
        var input  = Day5.getMoves("day_5_test.txt");
        assertEquals(5, Day5.part1(input));
    }

    @Test
    void part2() throws IOException {
        var input  = Day5.getMoves("day_5_test.txt");
        assertEquals(12, Day5.solve(input));
    }

    @Test
    void part2_full() throws IOException {
        var input  = Day5.getMoves("day_5.txt");
        assertEquals(22335, Day5.solve2(input));
    }
}