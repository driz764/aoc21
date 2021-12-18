import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day18Test {

    @Test
    public void parsing() throws IOException {
        String s = "[1,2]";
        var pairNode = Day18.Node.buildFromString(s);
        assertEquals(s, pairNode.toString());
    }

    @Test
    public void parsing2() throws IOException {
        String s = "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]";
        var pairNode = Day18.Node.buildFromString(s);
        assertEquals(s, pairNode.toString());
    }

    @Test
    public void add() throws IOException {
        var a = Day18.Node.buildFromString("[1,2]");
        var b = Day18.Node.buildFromString("[[3,4],5]");
        assertEquals("[[1,2],[[3,4],5]]", a.add(b).toString());
    }

    @Test
    public void reduce() throws IOException {
        var pairNode = Day18.Node.buildFromString("[[[[[9,8],1],2],3],4]");
        pairNode.reduce(4);
        assertEquals("[[[[0,9],2],3],4]", pairNode.toString());
    }

    @Test
    public void reduce2() throws IOException {
        var pairNode = Day18.Node.buildFromString("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
        pairNode.reduce(4);
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", pairNode.toString());
    }

    @Test
    public void reduce3() throws IOException {
        var pairNode = Day18.Node.buildFromString("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]");
        pairNode.reduce(4);
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", pairNode.toString());
    }

    @Test
    public void split() throws IOException {
        var a = Day18.Node.buildFromString("[[[[4,3],4],4],[7,[[8,4],9]]]");
        var b = Day18.Node.buildFromString("[1,1]");
        var result = a.add(b);
        assertEquals("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", result.toString());

        result.reduce(4);
        assertEquals("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", result.toString());

        result.reduce(4);
        assertEquals("[[[[0,7],4],[15,[0,13]]],[1,1]]", result.toString());

        result.split();
        assertEquals("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", result.toString());

        result.split();
        assertEquals("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", result.toString());

        result.reduce(4);
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", result.toString());
    }

    @Test
    public void magnitude(){
        assertEquals(3488, Day18.Node.buildFromString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude());
    }
}