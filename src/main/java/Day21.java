import util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day21 {
    static final int[] board = new int[]{10, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static void main(String[] args) throws IOException {
        // Parse
        int[] player1ps = new int[]{9, 0};
        int[] player2ps = new int[]{6, 0};

//        int[] player1ps = new int[]{4, 0};
//        int[] player2ps = new int[]{8, 0};

        int[][] players = new int[][]{player1ps, player2ps};
        int[][] playerspart2 = clone(players);
        D100 dice = new D100();
        int currentPlayer = 0;
        while (players[0][1] < 1000 && players[1][1] < 1000) {
            int roll = dice.roll() + dice.roll() + dice.roll();
            players[currentPlayer][0] = (players[currentPlayer][0] + roll) % board.length;
            players[currentPlayer][1] += board[players[currentPlayer][0]];
            currentPlayer = currentPlayer == 0 ? 1 : 0;
        }

        var part1 = dice.nbRoll * players[currentPlayer][1];
        System.out.println("Day 21, part 1 : " + part1); // 1004670

        var result2 = play(playerspart2, 0);
        var part2 = result2.left() > result2.right() ? result2.left() : result2.right();
        System.out.println("Day 21, part 2 : " + part2); // > 56596695

    }

    static int[][] clone(int[][] players) {
        return new int[][]{
                players[0].clone(),
                players[1].clone()
        };
    }

    static Map<String, Pair<Long, Long>> memoise = new HashMap<>();

    static Pair<Long, Long> play(int[][] players, int currentPlayer) {
        if (players[0][1] > 20) return new Pair<>(1L, 0L);
        if (players[1][1] > 20) return new Pair<>(0L, 1L);
        String stringify = stringify(players, currentPlayer);
        if (memoise.containsKey(stringify)) {
            return memoise.get(stringify);
        }
        Pair<Long, Long> resultat = new Pair<>(0L, 0L);
        for (var a = 1; a <= 3; a++) {
            for (var b = 1; b <= 3; b++) {
                for (var c = 1; c <= 3; c++) {
                    var roll = a + b + c;
                    int[][] clone = clone(players);
                    clone[currentPlayer][0] = (clone[currentPlayer][0] + roll) % board.length;
                    clone[currentPlayer][1] += board[clone[currentPlayer][0]];
                    Pair<Long, Long> res = play(clone, currentPlayer == 0 ? 1 : 0);
                    resultat = new Pair<>(resultat.left() + res.left(), resultat.right() + res.right());
                }
            }
        }
        memoise.put(stringify, resultat);
        return resultat;
    }

    private static String stringify(int[][] players, int currentPlayer) {
        return players[0][0] + "-" + players[0][1] + "-" + players[1][0] + "-" + players[1][1] + "-" + currentPlayer;
    }


    static class D100 {
        int nbRoll = 0;
        int val = 1;

        int roll() {
            var result = val;
            nbRoll++;
            val++;
            if (val == 101) {
                val = 1;
            }
            return result;
        }
    }


}
