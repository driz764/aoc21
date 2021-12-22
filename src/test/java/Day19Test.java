import org.junit.jupiter.api.Test;
import util.Vector;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day19Test {

    @Test
    public void rotation(){
        List<Vector> vectors = Day19.allRotationOf(new Vector(1, 2, 3));
        assertEquals(vectors.size(), new HashSet<>(vectors).size());
    }

}