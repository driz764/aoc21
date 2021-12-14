package intcode;

import java.util.concurrent.*;

public class IntNetwork {

    IntMachine[] machines;

    public IntNetwork(int nbMachine, String data, boolean loop) {
        this.machines = new IntMachine[nbMachine];
        BlockingQueue<Long> in = new ArrayBlockingQueue<>(10);
        BlockingQueue<Long> out = new ArrayBlockingQueue<>(10);
        for (int i = 0; i < nbMachine; i++) {
            this.machines[i] = new IntMachine("" + i, data, in, out);
            in = out;
            out = new ArrayBlockingQueue<>(10);
        }
        if (loop) {
            machines[0].input = in;
        }
    }

    public void run() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(machines.length);
        for (IntMachine machine : machines) {
            executorService.submit(machine::run);
        }
        executorService.shutdown();
        boolean b = executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
