import org.apache.commons.lang3.time.StopWatch;
import util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static util.Parser.splitToPair;

public class Day14 {

    static Map<Pair<Character, Character>, Character> insertionRules;

    public static void main(String[] args) throws IOException {
        // Parse
        var data = splitToPair(Files.readString(Paths.get("c:\\dev\\input\\day_14.txt")), "\\n\\n");
        var polymerTemplate = data.left();
        insertionRules = data.right().lines()
                .map(l -> splitToPair(l, " -> "))
                .collect(toMap(p -> new Pair<>(p.left().charAt(0), p.left().charAt(1)), p -> p.right().charAt(0)));

        StopWatch swRec = StopWatch.createStarted();
        System.out.println("Day 14, Part 1 : " + run(polymerTemplate, 10)); //          2602
        System.out.println("Day 14, Part 2 : " + run(polymerTemplate, 40)); // 2942885922173
        swRec.stop();

        StopWatch swNonRec = StopWatch.createStarted();
        System.out.println("\nDay 14, Part 1 : " + run2(polymerTemplate, 10));
        System.out.println("Day 14, Part 2 : " + run2(polymerTemplate, 40));
        swNonRec.stop();

        System.out.println("\nRec Mémoïsation " + swRec.getTime() + "ms");
        System.out.println("Group by pair   " + swNonRec.getTime() + "ms");
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
        char newChar = insertionRules.get(new Pair<>(c.a, c.b));
        var result = merge(
                combine(new Combinaison(c.a, newChar, c.step - 1)),
                combine(new Combinaison(newChar, c.b, c.step - 1))
        );
        memoizeCombine.put(c, result);
        return result;
    }

    private static Map<Character, Long> merge(Map<Character, Long> a, Map<Character, Long> b) {
        return Stream.of(a, b).flatMap(map -> map.entrySet().stream()).collect(toMap(
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

    public static Long run2(String seq, int step) {
        Map<Pair<Character, Character>, Long> result = new HashMap<>();
        Map<Character, Long> countByChar = new HashMap<>();
        countByChar.put(seq.charAt(0), 1L);
        for (int i = 1; i < seq.length(); i++) {
            result.put(new Pair<>(seq.charAt(i - 1), seq.charAt(i)), 1L);
            countByChar.merge(seq.charAt(i), 1L, Long::sum);
        }
        for (int cpt = 0; cpt < step; cpt++) {
            Map<Pair<Character, Character>, Long> next = new HashMap<>();
            for (Map.Entry<Pair<Character, Character>, Long> p : result.entrySet()) {
                char newChar = insertionRules.get(p.getKey());
                countByChar.merge(newChar, p.getValue(), Long::sum);
                next.merge(new Pair<>(p.getKey().left(), newChar), p.getValue(), Long::sum);
                next.merge(new Pair<>(newChar, p.getKey().right()), p.getValue(), Long::sum);
            }
            result = next;
        }
        return minMaxDiff(countByChar);
    }
}

/*

 */