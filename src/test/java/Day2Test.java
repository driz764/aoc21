import org.junit.jupiter.api.Test;
import util.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {

    @Test
    void part1() throws IOException {
        var input = Parser.parse("day_2_test.txt", Day2.Ordre::fromInput);
        assertEquals(150, Day2.part1(input));
    }

    @Test
    void part2() throws IOException {
        var input = Parser.parse("day_2_test.txt", Day2.Ordre::fromInput);
        assertEquals(900, Day2.part2(input));
    }
}