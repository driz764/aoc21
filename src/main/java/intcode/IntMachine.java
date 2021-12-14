package intcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class IntMachine {

    protected String name = "IntMachine";
    protected Map<Long, Long> program = new HashMap<>();
    private long cursor = 0;
    private long relativeBase = 0;
    boolean shouldExit = false;
    protected BlockingQueue<Long> input = new ArrayBlockingQueue<>(100);
    protected BlockingQueue<Long> output = new ArrayBlockingQueue<>(100);
    ;


    public IntMachine(String data) {
        var splited = data.split(",");
        for (long i = 0; i < splited.length; i++) {
            program.put(i, Long.parseLong(splited[(int) i]));
        }
    }

    public IntMachine(String name, String data, BlockingQueue<Long> input, BlockingQueue<Long> output) {
        this(data);
        this.name = name;
        this.input = input;
        this.output = output;
    }

    public void run() {
        while (!shouldExit) {
            var op = fromInt(program.get(cursor));
            try {
                op.run(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Long getVal(ParameterMode parameterMode, long cursor) {
        var result = switch (parameterMode) {
            case POSITION -> program.get(program.get(cursor));
            case IMMEDIATE -> program.get(cursor);
            case RELATIVE -> program.get(program.get(cursor) + relativeBase);
        };
        if (result == null) result = 0L;
        return result;
    }

    void writeValAtAddress(Long cursor, ParameterMode mode, Long val) {
        //program.put(program.get(cursor), val);
        switch (mode) {
            case POSITION -> program.put(program.get(cursor), val);
            case IMMEDIATE -> throw new RuntimeException("Cant immediate write");
            case RELATIVE -> program.put(program.get(cursor) + relativeBase, val);
        }
    }

    Op fromInt(long i) {
        var op = i % 100;
        var arg1 = ParameterMode.of((i / 100) % 10);
        var arg2 = ParameterMode.of((i / 1000) % 10);
        var arg3 = ParameterMode.of((i / 10000) % 10);

        return switch ((int) op) {
            case 1 -> new MathBinary(Long::sum, arg1, arg2, arg3);
            case 2 -> new MathBinary((a, b) -> a * b, arg1, arg2, arg3);
            case 3 -> new Input(arg1);
            case 4 -> new Output(arg1);
            case 5 -> new If(true, arg1, arg2);
            case 6 -> new If(false, arg1, arg2);
            case 7 -> new Cmp((a, b) -> a < b, arg1, arg2, arg3);
            case 8 -> new Cmp(Long::equals, arg1, arg2, arg3);
            case 9 -> new MoveRelativeBase(arg1);
            case 99 -> new Exit();
            default -> throw new RuntimeException();
        };
    }

    public long getLastOutput() {
        var allOutput = new ArrayList<Long>();
        output.drainTo(allOutput);
        return allOutput.get(allOutput.size() - 1);
    }

    private enum ParameterMode {
        POSITION, IMMEDIATE, RELATIVE;

        static ParameterMode of(long i) {
            if (i == 0) return POSITION;
            if (i == 1) return IMMEDIATE;
            if (i == 2) return RELATIVE;
            throw new RuntimeException("oops " + i);
        }
    }

    private interface Op {
        void run(IntMachine im) throws InterruptedException;
    }

    private static class MathBinary implements Op {
        private final BiFunction<Long, Long, Long> mathStuff;
        private final ParameterMode mode1, mode2, mode3;

        private MathBinary(BiFunction<Long, Long, Long> mathStuff, ParameterMode mode1, ParameterMode mode2, ParameterMode mode3) {
            this.mathStuff = mathStuff;
            this.mode1 = mode1;
            this.mode2 = mode2;
            this.mode3 = mode3;
        }

        @Override
        public void run(IntMachine im) {
            var val1 = im.getVal(mode1, im.cursor + 1);
            var val2 = im.getVal(mode2, im.cursor + 2);
            im.writeValAtAddress(im.cursor + 3, mode3, mathStuff.apply(val1, val2));
            im.cursor += 4;
        }
    }

    private static class Input implements Op {
        private final ParameterMode mode1;

        public Input(ParameterMode arg1) {
            mode1 = arg1;
        }

        @Override
        public void run(IntMachine im) throws InterruptedException {
            var in = im.input.take();
            im.writeValAtAddress(im.cursor + 1, mode1, in);
            im.cursor += 2;
        }
    }

    private static class Output implements Op {

        private final ParameterMode mode1;

        public Output(ParameterMode mode1) {
            this.mode1 = mode1;
        }

        @Override
        public void run(IntMachine im) {
            var val = im.getVal(mode1, im.cursor + 1);
            System.out.println("Ouput : " + val);
            im.output.add(val);
            im.cursor += 2;
        }
    }

    private static class MoveRelativeBase implements Op {

        private final ParameterMode mode1;

        public MoveRelativeBase(ParameterMode mode1) {
            this.mode1 = mode1;
        }

        @Override
        public void run(IntMachine im) {
            var val = im.getVal(mode1, im.cursor + 1);
            im.relativeBase += val;
            im.cursor += 2;
        }
    }

    private static class If implements Op {

        private final ParameterMode mode1, mode2;
        private final boolean ifTrue;

        public If(boolean ifTrue, ParameterMode mode1, ParameterMode mode2) {
            this.ifTrue = ifTrue;
            this.mode1 = mode1;
            this.mode2 = mode2;
        }

        @Override
        public void run(IntMachine im) {
            var val = im.getVal(mode1, im.cursor + 1);
            if ((val != 0) == ifTrue) {
                im.cursor = im.getVal(mode2, im.cursor + 2);
            } else {
                im.cursor += 3;
            }
        }
    }

    private static class Cmp implements Op {

        private final ParameterMode mode1, mode2, mode3;
        private final BiPredicate<Long, Long> cmp;

        public Cmp(BiPredicate<Long, Long> cmp, ParameterMode mode1, ParameterMode mode2, ParameterMode mode3) {
            this.cmp = cmp;
            this.mode1 = mode1;
            this.mode2 = mode2;
            this.mode3 = mode3;
        }

        @Override
        public void run(IntMachine im) {
            var val1 = im.getVal(mode1, im.cursor + 1);
            var val2 = im.getVal(mode2, im.cursor + 2);
            im.writeValAtAddress(im.cursor + 3, mode3, (long) (cmp.test(val1, val2) ? 1 : 0));
            im.cursor += 4;
        }
    }


    private static class Exit implements Op {
        @Override
        public void run(IntMachine im) {
            im.shouldExit = true;
            System.out.printf("[%s] Exit%n", im.name);
        }
    }

}
