import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    @Test
    public void test(){
        assertEquals(288957, Day10.complete("[({(<(())[]>[[{[]{<()<>>"));
        assertEquals(5566, Day10.complete("[(()[<>])]({[<{<<[]>>("));
        assertEquals(1480781, Day10.complete("(((({<>}<{<{<>}{[]{[]{}"));
        assertEquals(995444, Day10.complete("{<[[]]>}<{[{[{[]{()[[[]"));
        assertEquals(294, Day10.complete("<{([{{}}[<[[[<>{}]]]>[]]"));
    }

}