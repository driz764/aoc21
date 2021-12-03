import org.junit.jupiter.api.Test;
import util.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

    @Test
    void part1() throws IOException {
        var input = Parser.parse("day_3_test.txt", a -> a);
        assertEquals(198, Day3.part1(input));
    }

    @Test
    void part2() throws IOException {
        var input = Parser.parse("day_3_test.txt", a -> a);
        assertEquals(230, Day3.part2(input));
    }
}