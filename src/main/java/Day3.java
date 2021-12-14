import util.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) throws IOException {
        List<String> input = Parser.parse("day_3.txt", a -> a);

        //Part 1
        System.out.println("Day 3, Part 1 : " + part1(input)); // 3895776

        // Part 2
        System.out.println("Day 3, Part 2 : " + part2(input)); // 7928162
    }

    public static int part1(List<String> input) {
        int length = input.get(0).length();
        String resultGammaString = "";
        for (int i = 0; i < length; i++) {
            int nb1 = 0;
            for (String s : input) {
                if (s.charAt(i) == '1') nb1++;
            }
            if (nb1 > (input.size() / 2)) {
                resultGammaString = resultGammaString + "1";
            } else {
                resultGammaString = resultGammaString + "0";
            }
        }
        var gamma = Integer.parseInt(resultGammaString, 2);
        // for fun
        var eps = (((1 << resultGammaString.length()) - 1) & (~gamma));
        return gamma * eps;
    }

    public static int part2(List<String> input) {
        return getOxygen(input) * getCO2(input);
    }

    public static int getOxygen(List<String> input) {
        int length = input.get(0).length();
        for (int i = 0; i < length; i++) {
            var split = splitByChar(input, i);
            List<String> with1 = split.get('1');
            List<String> with0 = split.get('0');
            if (with1.size() >= (input.size() / 2)) {
                input = with1;
            } else {
                input = with0;
            }
            if (input.size() == 1) break;
        }
        return Integer.parseInt(input.get(0), 2);
    }

    public static int getCO2(List<String> input) {
        int length = input.get(0).length();
        for (int i = 0; i < length; i++) {
            var split = splitByChar(input, i);
            List<String> with1 = split.get('1');
            List<String> with0 = split.get('0');
            if (with0.size() > (input.size() / 2)) {
                input = with1;
            } else {
                input = with0;
            }
            if (input.size() == 1) break;
        }
        return Integer.parseInt(input.get(0), 2);
    }

    public static Map<Character, List<String>> splitByChar(List<String> input, int i) {
        return input.stream().collect(Collectors.groupingBy(s -> s.charAt(i)));
    }

}
