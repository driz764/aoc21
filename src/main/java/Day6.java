import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day6 {

    public static void main(String[] args) throws IOException {
        var data = Files.readString(Paths.get("c:\\dev\\input\\" + "day_6.txt"));
        long[] fishesCount = new long[9];
        Arrays.stream(data.split(",")).map(Integer::parseInt).forEach(i -> fishesCount[i]++);

        System.out.println("Day 6, Part 1 : " + solve(80, fishesCount)); // 350149
        System.out.println("Day 6, Part 2 : " + solve(256 - 80, fishesCount)); // 1590327954513
    }

    public static long solve(int iteration, long[] data) {
        for (int i = 0; i < iteration; i++) {
            long nb0 = data[0];
            System.arraycopy(data, 1, data, 0, data.length - 1);
            data[6] += nb0;
            data[8] = nb0;
        }
        return Arrays.stream(data).sum();
    }
}
