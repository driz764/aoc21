import util.Parser;
import util.Vector;

import java.io.IOException;
import java.util.*;

import static util.Vector.ALL_DIR_WITH_DIAG;

public class Day11 {

    public static void main(String[] args) throws IOException {
        // Parse
        var data = Parser.parse("day_11.txt", l -> l);
        HashMap<Vector, Integer> cave = new HashMap<>();
        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                cave.put(new Vector(x, y), Integer.parseInt(data.get(y).charAt(x) + ""));
            }
        }

        var explosionCount = 0L;
        var step = 0;
        while (true) {
            step++;
            Queue<Vector> mustExplode = new LinkedList<>();
            var alreadyExplode = new HashSet<Vector>();

            for (Map.Entry<Vector, Integer> c : cave.entrySet()) {
                c.setValue(c.getValue() + 1);
                if (c.getValue() == 10) mustExplode.add(c.getKey());
            }
            while (!mustExplode.isEmpty()) {
                var c = mustExplode.poll();
                if (alreadyExplode.contains(c)) continue;
                alreadyExplode.add(c);
                explosionCount++;
                for (Vector move : ALL_DIR_WITH_DIAG) {
                    Vector neig = c.add(move);
                    if (!cave.containsKey(neig)) continue;
                    int newVal = cave.get(neig) + 1;
                    cave.put(neig, newVal);
                    if (newVal > 9) mustExplode.add(neig);
                }
            }
            alreadyExplode.forEach(v -> cave.put(v, 0));
            if (step == 100) {
                System.out.println("Day 11, Part 1 : " + explosionCount); // 1585
            }
            if (alreadyExplode.size() == 100) {
                System.out.println("Day 11, Part 2 : " + step); // 382
                break;
            }
        }
    }
}