import util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day4 {

    public static void main(String[] args) throws IOException {
        var data = Files.readString(Paths.get("c:\\dev\\input\\" + "day_4.txt"));
        var dataSplitted = data.split("\n\n");
        var tirage = Arrays.stream(dataSplitted[0].split(",")).map(Integer::parseInt).toList();
        List<Board> boards = new ArrayList<>();
        for (int i = 1; i < dataSplitted.length; i++) {
            boards.add(new Board(dataSplitted[i]));
        }
        var score = -1;
        for (Integer tir : tirage) {
            for (Board b : boards) {
                if (b.check(tir)) {
                    if (score == -1) {
                        System.out.println("Day 4, part 1 : " + b.score);
                    }
                    score = b.score;
                }
            }
        }
        System.out.println("Day 4, part 2 : " + score);
    }

    public static class Board {

        private Map<Integer, Vector> grid = new HashMap<>();
        private int[] markedByX = new int[]{0, 0, 0, 0, 0};
        private int[] markedByY = new int[]{0, 0, 0, 0, 0};
        private int score = -1;

        public Board(String input) {
            String[] lines = input.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                List<Integer> cell = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList();
                for (int j = 0; j < cell.size(); j++) {
                    grid.put(cell.get(j), new Vector(i, j));
                }
            }
        }

        public boolean check(int check) {
            Vector cell = grid.get(check);
            if (cell == null || score != -1) return false;
            markedByX[cell.x()] += 1;
            markedByY[cell.y()] += 1;
            grid.remove(check);
            if (markedByX[cell.x()] == 5 || markedByY[cell.y()] == 5) {
                computeScore(check);
                return true;
            }
            return false;
        }

        private void computeScore(int check) {
            var sumExisting = grid.keySet().stream().mapToInt(Integer::intValue).sum();
            score = sumExisting * check;
        }
    }

}
