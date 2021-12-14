package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Parser {

    public static <T> List<T> parse(String filename, Function<String, T> mapper) throws IOException {
        try (var br = new BufferedReader(new FileReader("c:\\dev\\input\\" + filename))) {
            return br.lines()
                    .map(mapper)
                    .collect(Collectors.toList());
        }
    }

    public static <A, B> Pair<A, B> splitToPair(String input, String separator, Function<String, A> mapLeft, Function<String, B> mapRight) {
        String[] split = input.split(separator);
        return new Pair<>(
                mapLeft.apply(split[0]),
                mapRight.apply(split[1])
        );
    }

    public static Pair<String, String> splitToPair(String input, String separator){
        return splitToPair(input, separator, Function.identity(), Function.identity());
    }
}
