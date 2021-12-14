import util.Pair;
import util.Parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) throws IOException {
        // PArse
        var data = Parser.parse("day_8.txt", l -> {
            String[] split = l.split("\\s\\|\\s");
            return new Pair<>(
                    Arrays.stream(split[0].split("\\s"))
                            .map(Day8::sortedString)
                            .collect(Collectors.toList()),
                    Arrays.stream(split[1].split("\\s"))
                            .map(Day8::sortedString)
                            .toList()
            );
        });

        // Part 1
        long count = data.stream()
                .map(Pair::right)
                .flatMap(Collection::stream)
                .filter(o -> o.length() <= 4 || o.length() == 7)
                .count();
        System.out.println("Day 8, Part 1 : " + count);//   539

        // Part 2
        var part2 = 0;
        for (Pair<List<String>, List<String>> d : data) {
            String[] mapping = new String[10];
            mapping[1] = d.left().stream().filter(o -> o.length() == 2).findFirst().get();
            mapping[7] = d.left().stream().filter(o -> o.length() == 3).findFirst().get();
            mapping[4] = d.left().stream().filter(o -> o.length() == 4).findFirst().get();
            mapping[8] = d.left().stream().filter(o -> o.length() == 7).findFirst().get();
            mapping[3] = d.left().stream().filter(o -> o.length() == 5 && countSimilarChar(o, mapping[1]) == 2).findFirst().get();
            d.left().remove(mapping[3]);
            mapping[6] = d.left().stream().filter(o -> o.length() == 6 && countSimilarChar(o, mapping[1]) == 1).findFirst().get();
            d.left().remove(mapping[6]);
            mapping[9] = d.left().stream().filter(o -> o.length() == 6 && countSimilarChar(o, mapping[4]) == 4).findFirst().get();
            d.left().remove(mapping[9]);
            mapping[0] = d.left().stream().filter(o -> o.length() == 6).findFirst().get();
            mapping[5] = d.left().stream().filter(o -> o.length() == 5 && countSimilarChar(o, mapping[4]) == 3).findFirst().get();
            d.left().remove(mapping[5]);
            mapping[2] = d.left().stream().filter(o -> o.length() == 5).findFirst().get();

            var mappingList = Arrays.asList(mapping);
            for (int i = 0; i < d.right().size(); i++) {
                var indexOf = mappingList.indexOf(d.right().get(i));
                part2 +=  indexOf * Math.pow(10, 3 - i);
            }
        }
        System.out.println("Day 8, Part 2 : " + part2);//   1084606

    }

    public static int countSimilarChar(String a, String b) {
        Set<Integer> aSet = a.chars().boxed().collect(Collectors.toSet());
        Set<Integer> bSet = b.chars().boxed().collect(Collectors.toSet());
        aSet.retainAll(bSet);
        return aSet.size();
    }

    public static String sortedString(String s) {
        char[] tmp = s.toCharArray();
        Arrays.sort(tmp);
        return new String(tmp);
    }

}
