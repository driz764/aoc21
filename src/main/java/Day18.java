import util.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Day18 {


    public static void main(String[] args) throws IOException {
        // Parse
        List<String> input = Parser.parse("day_18.txt", l -> l);
        List<PairNode> data = input.stream().map(Node::buildFromString).toList();

        Node result = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            result = result.add(data.get(i));
            result.normalize();
        }
        System.out.println("Day 18, Part 1 : " + result.magnitude()); // 4072

        var max = -1;
        for (var a : input) {
            for (var b : input) {
                if (a == b) continue;
                var m = Node.buildFromString(a).add(Node.buildFromString(b)).normalize().magnitude();
                if (m > max) max = m;
            }
        }
        System.out.println("Day 18, Part 2 : " + max); // 4483
    }


    abstract static class Node {
        PairNode parent;

        Node add(Node o) throws IOException {
            return new PairNode(this, o);
        }

        static PairNode buildFromString(String s) {
            try {
                StringReader reader = new StringReader(s);
                reader.read();
                return new PairNode(reader, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        abstract boolean reduce(int depth);

        abstract public boolean split();

        abstract int magnitude();

        abstract List<LiteralNode> asLiteralList();

        Node normalize() {
            while (true) {
                if (this.reduce(4) || this.split()) continue;
                break;
            }
            return this;
        }

    }

    static class PairNode extends Node {
        Node left, right;

        public PairNode(StringReader sr, PairNode parent) throws IOException {
            this.parent = parent;
            this.left = readNodeInternal(sr);
            if (sr.read() != ',') throw new RuntimeException("Parse ex");
            this.right = readNodeInternal(sr);
            if (sr.read() != ']') throw new RuntimeException("Parse ex");
        }

        private Node readNodeInternal(StringReader sr) throws IOException {
            char c = (char) sr.read();
            if (c == '[') {
                return new PairNode(sr, this);
            }
            return new LiteralNode(c, this);
        }

        public PairNode(Node left, Node right) {
            this.parent = null;
            this.left = left;
            this.right = right;
            left.parent = this;
            right.parent = this;
        }

        public boolean reduce(int depth) {
            if (depth == 0) {
                LiteralNode previousNode = null;
                Iterator<LiteralNode> it = root().asLiteralList().iterator();
                while (it.hasNext()) {
                    var current = it.next();
                    if (current == this.left) break;
                    previousNode = current;
                }
                if (previousNode != null) {
                    previousNode.val += ((LiteralNode) this.left).val;
                }
                it.next();
                if (it.hasNext()) {
                    it.next().val += ((LiteralNode) this.right).val;
                }
                parent.replaceMeBy(this, new LiteralNode(0, this.parent));
                this.parent = null;
                return true;
            }
            return this.left.reduce(depth - 1) || this.right.reduce(depth - 1);
        }

        public void replaceMeBy(Node me, Node by) {
            by.parent = this;
            if (this.left == me) this.left = by;
            else this.right = by;
        }

        public boolean split() {
            return this.left.split() || this.right.split();
        }

        int magnitude() {
            return this.left.magnitude() * 3 + this.right.magnitude() * 2;
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + "]";
        }

        List<LiteralNode> asLiteralList() {
            List<LiteralNode> l = this.left.asLiteralList();
            List<LiteralNode> r = this.right.asLiteralList();
            return Stream.concat(l.stream(), r.stream()).toList();
        }

        public Node root() {
            if (parent == null) return this;
            return parent.root();
        }
    }

    static class LiteralNode extends Node {
        int val;

        public LiteralNode(char c, PairNode parent) {
            this.parent = parent;
            val = Integer.parseInt(c + "");
        }

        public LiteralNode(int i, PairNode parent) {
            this.parent = parent;
            val = i;
        }

        @Override
        public boolean reduce(int depth) {
            return false;
        }

        @Override
        public boolean split() {
            if (val < 10) return false;
            var l = new LiteralNode(val / 2, null);
            var r = new LiteralNode(val - (val / 2), null);
            var result = new PairNode(l, r);
            parent.replaceMeBy(this, result);
            this.parent = null;
            return true;
        }

        int magnitude() {
            return val;
        }

        List<LiteralNode> asLiteralList() {
            return List.of(this);
        }

        @Override
        public String toString() {
            return Integer.toString(val);
        }
    }
}
