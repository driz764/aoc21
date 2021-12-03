import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import util.Parser;

import java.io.IOException;
import java.util.List;

class Day1Test {

    @Test
    void part1() throws IOException {
        List<Integer> input = Parser.parse("day_1_test.txt", Integer::parseInt);
        assertEquals(7, Day1.part1(input));
    }

    @Test
    void part2() throws IOException {
        List<Integer> input = Parser.parse("day_1_test.txt", Integer::parseInt);
        assertEquals(5, Day1.part2(input));
    }
}