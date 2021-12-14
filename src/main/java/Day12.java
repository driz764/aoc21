import util.Pair;
import util.Parser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Day12 {

    public static void main(String[] args) throws IOException {
        // Parse
        var data = Parser.parse("day_12.txt", l -> {
            String[] split = l.split("-");
            return new Pair<>(split[0], split[1]);
        });
        Map<String, List<String>> graph = new HashMap<>();
        for (Pair<String, String> edge : data) {
            graph.merge(edge.left(), List.of(edge.right()), (la, lb) -> Stream.of(la, lb).flatMap(Collection::stream).toList());
            graph.merge(edge.right(), List.of(edge.left()), (la, lb) -> Stream.of(la, lb).flatMap(Collection::stream).toList());
        }
        var start = new LinkedList<String>();
        start.add("start");

        //System.out.println("Day 12, Part 1 : " + part1stream(graph, start).size()); // 3485
        System.out.println("Day 12, Part 1 : " + part1mutable(graph, start)); // 3485
//        System.out.println("Day 12, Part 2 : " + part2stream(graph, start, false).size()); // 85062
        start = new LinkedList<String>();
        start.add("start");
        System.out.println("Day 12, Part 2 : " + part2mutable(graph, start, false)); // 85062
    }

    public static List<List<String>> part1stream(Map<String, List<String>> graph, LinkedList<String> path) {
        var currentNode = path.peekLast();
        if (currentNode.equals("end")) return List.of(path);
        return graph.get(currentNode).stream()
                .filter(s -> isUpper(s) || !path.contains(s))
                .map(s -> part1stream(graph, cloneAppend(path, s)))
                .flatMap(Collection::stream)
                .toList();
    }

    public static List<List<String>> part2stream(Map<String, List<String>> graph, LinkedList<String> path, boolean alreadyVisitTwice) {
        var currentNode = path.peekLast();
        if (currentNode.equals("end")) return List.of(path);
        return graph.get(currentNode).stream()
                .filter(s -> isUpper(s) || !s.equals("start") && (!alreadyVisitTwice || !path.contains(s)))
                .map(s -> part2stream(graph, cloneAppend(path, s), alreadyVisitTwice || (!isUpper(s) && path.contains(s))))
                .flatMap(Collection::stream)
                .toList();
    }

    public static int part1mutable(Map<String, List<String>> graph, LinkedList<String> path) {
        var currentNode = path.peekLast();
        if (currentNode.equals("end")) return 1;
        var result = 0;
        for (String s : graph.get(currentNode)) {
            if (isUpper(s) || !path.contains(s)) {
                path.add(s);
                result += part1mutable(graph, path);
                path.removeLast();
            }
        }
        return result;
    }

    public static int part2mutable(Map<String, List<String>> graph, LinkedList<String> path, boolean alreadyVisitTwice) {
        var currentNode = path.peekLast();
        if (currentNode.equals("end")) return 1;
        var result = 0;
        for (String s : graph.get(currentNode)) {
            if (isUpper(s) || !s.equals("start") && (!alreadyVisitTwice || !path.contains(s))) {
                var newVisitFlag = alreadyVisitTwice || (!isUpper(s) && path.contains(s));
                path.add(s);
                result += part2mutable(graph, path, newVisitFlag);
                path.removeLast();
            }
        }
        return result;
    }

    public static <T> LinkedList<T> cloneAppend(LinkedList<T> src, T add) {
        LinkedList<T> result = new LinkedList<>(src);
        result.add(add);
        return result;
    }

    public static boolean isUpper(String s) {
        return s.codePoints().allMatch(Character::isUpperCase);
    }
}