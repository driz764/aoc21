import org.junit.jupiter.api.Test;
import util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    @Test
    public void foldTest(){
        assertEquals(new Vector(0,0), Day13.fold('y', 7, new Vector(0,14)));
        assertEquals(new Vector(0,1), Day13.fold('y', 7, new Vector(0,13)));
    }

}