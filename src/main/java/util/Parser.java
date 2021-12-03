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
}
