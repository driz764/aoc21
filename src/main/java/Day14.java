import util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.Parser.splitToPair;

public class Day14 {

    static Map<String, String> insertionRules;

    public static void main(String[] args) throws IOException {
        // Parse
        var data = splitToPair(Files.readString(Paths.get("c:\\dev\\input\\day_14.txt")), "\\n\\n");
        var polymerTemplate = data.left();
        insertionRules = data.right().lines()
                .map(l -> splitToPair(l, " -> "))
                .collect(Collectors.toMap(Pair::left, Pair::right));

        System.out.println("Day 14, Part 1 : " + run(polymerTemplate, 10)); //          2602
        System.out.println("Day 14, Part 2 : " + run(polymerTemplate, 40)); // 2942885922173
    }

    public static Long run(String seq, int step) {
        Map<Character, Long> countByChar = new HashMap<>();
        for (int i = 1; i < seq.length(); i++) {
            countByChar = merge(countByChar, combine(new Combinaison(seq.charAt(i - 1), seq.charAt(i), step)));
        }
        countByChar = merge(countByChar, Map.of(seq.charAt(seq.length() - 1), 1L));
        return minMaxDiff(countByChar);
    }

    public static Map<Combinaison, Map<Character, Long>> memoizeCombine = new HashMap<>();
    public static Map<Character, Long> combine(Combinaison c) {
        if (c.step == 0) {
            return Map.of(c.a, 1L);
        }
        if (memoizeCombine.containsKey(c)) return memoizeCombine.get(c);
        char newChar = insertionRules.get(c.a + "" + c.b).charAt(0);
        var result = merge(
                combine(new Combinaison(c.a, newChar, c.step - 1)),
                combine(new Combinaison(newChar, c.b, c.step - 1))
        );
        memoizeCombine.put(c, result);
        return result;
    }

    private static Map<Character, Long> merge(Map<Character, Long> a, Map<Character, Long> b) {
        return Stream.of(a, b).flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                Long::sum
        ));
    }

    private static long minMaxDiff(Map<Character, Long> in) {
        var max = in.values().stream().mapToLong(Long::longValue).max().getAsLong();
        var min = in.values().stream().mapToLong(Long::longValue).min().getAsLong();
        return max - min;
    }

    record Combinaison(char a, char b, int step) {
    }
}