package intcode;

import org.junit.jupiter.api.Test;
import util.Permutations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntMachineTest {

    @Test
    public void Day2Part1() throws InterruptedException {
        IntMachine intMachine = new IntMachine(DAY_2_INPUT);
        intMachine.program.put(1L, 12L);
        intMachine.program.put(2L, 2L);
        intMachine.run();
        assertEquals(4930687, intMachine.program.get(0L));
    }

    @Test
    public void Day2Part2() throws InterruptedException {
        long noun, verb = 0;
        loop:
        for (noun = 0; noun < 100; noun++) {
            for (verb = 0; verb < 100; verb++) {
                IntMachine intMachine = new IntMachine(DAY_2_INPUT);
                intMachine.program.put(1L, noun);
                intMachine.program.put(2L, verb);
                intMachine.run();
                if (intMachine.program.get(0L) == 19690720) {
                    break loop;
                }
            }
        }
        assertEquals(5335, 100 * noun + verb);
    }

    @Test
    public void Day5Part1() throws InterruptedException {
        IntMachine intMachine = new IntMachine(DAY_5_INPUT);
        intMachine.input.add(1L);
        intMachine.run();
        assertEquals(13294380, intMachine.getLastOutput());
    }

    @Test
    public void Day5Part2() throws InterruptedException {
        IntMachine intMachine = new IntMachine(DAY_5_INPUT);
        intMachine.input.add(5L);
        intMachine.run();
        assertEquals(11460760, intMachine.getLastOutput());
    }

    @Test
    public void Day7Part1Test() throws InterruptedException {
        String input = DAY_7_TEST;
        IntNetwork intNetwork = new IntNetwork(5, input, false);
        intNetwork.machines[0].input.add(4L);
        intNetwork.machines[0].input.add(0L);
        intNetwork.machines[1].input.add(3L);
        intNetwork.machines[2].input.add(2L);
        intNetwork.machines[3].input.add(1L);
        intNetwork.machines[4].input.add(0L);

        intNetwork.run();

        assertEquals(43210, intNetwork.machines[4].output.take());
    }

    @Test
    public void Day7Part1() throws InterruptedException {
        String input = DAY_7_INPUT;

        var result = Permutations.of(List.of(0, 1, 2, 3, 4)).map(order -> {
                    IntNetwork intNetwork = new IntNetwork(5, input, false);
                    var o = order.toList();
                    for (int i = 0; i < 5; i++) {
                        intNetwork.machines[i].input.add(Long.valueOf(o.get(i)));
                    }
                    intNetwork.machines[0].input.add(0L);
                    try {
                        intNetwork.run();
                        return intNetwork.machines[4].output.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("oups");
                    }
                }).mapToLong(i -> i)
                .max().getAsLong();
        assertEquals(422858, result);
    }

    @Test
    public void Day7Part2() throws InterruptedException {
        String input = DAY_7_INPUT;

        var result = Permutations.of(List.of(5, 6, 7, 8, 9)).map(order -> {
                    IntNetwork intNetwork = new IntNetwork(5, input, true);
                    var o = order.toList();
                    for (int i = 0; i < 5; i++) {
                        intNetwork.machines[i].input.add(Long.valueOf(o.get(i)));
                    }
                    intNetwork.machines[0].input.add(0L);
                    try {
                        intNetwork.run();
                        return intNetwork.machines[4].output.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("oups");
                    }
                }).mapToLong(i -> i)
                .max().getAsLong();
        assertEquals(14897241, result);
    }

    @Test
    public void Day9Part1() throws InterruptedException {
        IntMachine intMachine = new IntMachine(DAY_9_INPUT);
        intMachine.input.add(1L);
        intMachine.run();
        assertEquals(4261108180L, intMachine.getLastOutput());
    }

    @Test
    public void Day9Part2() throws InterruptedException {
        IntMachine intMachine = new IntMachine(DAY_9_INPUT);
        intMachine.input.add(2L);
        intMachine.run();
        assertEquals(77944L, intMachine.getLastOutput());
    }

    @Test
    public void Day9Test(){
        IntMachine im = new IntMachine("104,1125899906842624,99");
        im.run();
    }


    static String DAY_2_INPUT = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,10,19,23,1,23,9,27,1,5,27,31,2,31,13,35,1,35,5,39,1,39,5,43,2,13,43,47,2,47,10,51,1,51,6,55,2,55,9,59,1,59,5,63,1,63,13,67,2,67,6,71,1,71,5,75,1,75,5,79,1,79,9,83,1,10,83,87,1,87,10,91,1,91,9,95,1,10,95,99,1,10,99,103,2,103,10,107,1,107,9,111,2,6,111,115,1,5,115,119,2,119,13,123,1,6,123,127,2,9,127,131,1,131,5,135,1,135,13,139,1,139,10,143,1,2,143,147,1,147,10,0,99,2,0,14,0";
    static String DAY_5_INPUT = "3,225,1,225,6,6,1100,1,238,225,104,0,1101,37,34,224,101,-71,224,224,4,224,1002,223,8,223,101,6,224,224,1,224,223,223,1002,113,50,224,1001,224,-2550,224,4,224,1002,223,8,223,101,2,224,224,1,223,224,223,1101,13,50,225,102,7,187,224,1001,224,-224,224,4,224,1002,223,8,223,1001,224,5,224,1,224,223,223,1101,79,72,225,1101,42,42,225,1102,46,76,224,101,-3496,224,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,1102,51,90,225,1101,11,91,225,1001,118,49,224,1001,224,-140,224,4,224,102,8,223,223,101,5,224,224,1,224,223,223,2,191,87,224,1001,224,-1218,224,4,224,1002,223,8,223,101,4,224,224,1,224,223,223,1,217,83,224,1001,224,-124,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1101,32,77,225,1101,29,80,225,101,93,58,224,1001,224,-143,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1101,45,69,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,7,226,226,224,102,2,223,223,1005,224,329,101,1,223,223,108,677,226,224,102,2,223,223,1005,224,344,1001,223,1,223,1108,226,677,224,102,2,223,223,1005,224,359,1001,223,1,223,8,677,226,224,102,2,223,223,1006,224,374,1001,223,1,223,107,226,226,224,102,2,223,223,1006,224,389,101,1,223,223,1108,677,226,224,1002,223,2,223,1005,224,404,1001,223,1,223,108,677,677,224,102,2,223,223,1005,224,419,101,1,223,223,7,226,677,224,1002,223,2,223,1006,224,434,1001,223,1,223,107,226,677,224,102,2,223,223,1005,224,449,101,1,223,223,1108,677,677,224,1002,223,2,223,1006,224,464,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,479,101,1,223,223,1007,677,677,224,1002,223,2,223,1005,224,494,101,1,223,223,1008,226,226,224,102,2,223,223,1006,224,509,1001,223,1,223,107,677,677,224,102,2,223,223,1006,224,524,1001,223,1,223,8,226,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,1007,677,226,224,102,2,223,223,1006,224,554,1001,223,1,223,1007,226,226,224,1002,223,2,223,1005,224,569,1001,223,1,223,8,226,677,224,1002,223,2,223,1006,224,584,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,599,101,1,223,223,1107,677,226,224,1002,223,2,223,1005,224,614,1001,223,1,223,1107,226,677,224,102,2,223,223,1006,224,629,1001,223,1,223,1008,226,677,224,102,2,223,223,1005,224,644,101,1,223,223,1107,226,226,224,102,2,223,223,1006,224,659,1001,223,1,223,1008,677,677,224,102,2,223,223,1006,224,674,1001,223,1,223,4,223,99,226";
    static String DAY_7_TEST = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0";
    static String DAY_7_INPUT = "3,8,1001,8,10,8,105,1,0,0,21,34,51,76,101,126,207,288,369,450,99999,3,9,102,4,9,9,1001,9,2,9,4,9,99,3,9,1001,9,2,9,1002,9,3,9,101,3,9,9,4,9,99,3,9,102,5,9,9,1001,9,2,9,102,2,9,9,101,3,9,9,1002,9,2,9,4,9,99,3,9,101,5,9,9,102,5,9,9,1001,9,2,9,102,3,9,9,1001,9,3,9,4,9,99,3,9,101,2,9,9,1002,9,5,9,1001,9,5,9,1002,9,4,9,101,5,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,99";
    static String DAY_9_INPUT = "1102,34463338,34463338,63,1007,63,34463338,63,1005,63,53,1102,1,3,1000,109,988,209,12,9,1000,209,6,209,3,203,0,1008,1000,1,63,1005,63,65,1008,1000,2,63,1005,63,904,1008,1000,0,63,1005,63,58,4,25,104,0,99,4,0,104,0,99,4,17,104,0,99,0,0,1102,1,21,1004,1101,28,0,1016,1101,0,27,1010,1102,36,1,1008,1102,33,1,1013,1101,0,22,1012,1101,0,37,1011,1102,34,1,1017,1102,466,1,1027,1102,1,484,1029,1102,1,699,1024,1102,1,1,1021,1101,0,0,1020,1102,1,24,1015,1101,0,473,1026,1101,653,0,1022,1102,26,1,1007,1102,25,1,1006,1101,0,39,1014,1102,646,1,1023,1101,690,0,1025,1102,1,29,1019,1101,32,0,1018,1101,30,0,1002,1101,0,20,1001,1102,1,38,1005,1102,1,23,1003,1101,0,31,1000,1101,35,0,1009,1101,0,493,1028,109,5,1208,0,37,63,1005,63,201,1001,64,1,64,1106,0,203,4,187,1002,64,2,64,109,-4,2107,36,8,63,1005,63,223,1001,64,1,64,1105,1,225,4,209,1002,64,2,64,109,18,21107,40,41,-9,1005,1010,243,4,231,1105,1,247,1001,64,1,64,1002,64,2,64,109,6,21107,41,40,-9,1005,1016,267,1001,64,1,64,1106,0,269,4,253,1002,64,2,64,109,-19,21102,42,1,5,1008,1011,42,63,1005,63,291,4,275,1105,1,295,1001,64,1,64,1002,64,2,64,109,15,1205,0,309,4,301,1105,1,313,1001,64,1,64,1002,64,2,64,109,-27,2101,0,9,63,1008,63,20,63,1005,63,333,1106,0,339,4,319,1001,64,1,64,1002,64,2,64,109,19,21102,43,1,6,1008,1019,45,63,1005,63,363,1001,64,1,64,1105,1,365,4,345,1002,64,2,64,109,1,21108,44,47,-3,1005,1011,385,1001,64,1,64,1106,0,387,4,371,1002,64,2,64,109,-22,1201,9,0,63,1008,63,21,63,1005,63,411,1001,64,1,64,1106,0,413,4,393,1002,64,2,64,109,9,1207,0,19,63,1005,63,433,1001,64,1,64,1106,0,435,4,419,1002,64,2,64,109,-9,2107,30,8,63,1005,63,453,4,441,1105,1,457,1001,64,1,64,1002,64,2,64,109,25,2106,0,10,1001,64,1,64,1106,0,475,4,463,1002,64,2,64,109,11,2106,0,0,4,481,1001,64,1,64,1105,1,493,1002,64,2,64,109,-18,2108,21,-6,63,1005,63,511,4,499,1106,0,515,1001,64,1,64,1002,64,2,64,109,-12,2108,18,6,63,1005,63,535,1001,64,1,64,1106,0,537,4,521,1002,64,2,64,109,19,21101,45,0,-7,1008,1010,45,63,1005,63,563,4,543,1001,64,1,64,1105,1,563,1002,64,2,64,109,-10,1207,-5,31,63,1005,63,581,4,569,1106,0,585,1001,64,1,64,1002,64,2,64,109,-8,2102,1,5,63,1008,63,21,63,1005,63,611,4,591,1001,64,1,64,1105,1,611,1002,64,2,64,109,5,1201,0,0,63,1008,63,21,63,1005,63,633,4,617,1106,0,637,1001,64,1,64,1002,64,2,64,109,13,2105,1,6,1001,64,1,64,1106,0,655,4,643,1002,64,2,64,109,-7,1202,-3,1,63,1008,63,26,63,1005,63,681,4,661,1001,64,1,64,1106,0,681,1002,64,2,64,109,12,2105,1,2,4,687,1001,64,1,64,1105,1,699,1002,64,2,64,109,-28,1208,8,30,63,1005,63,717,4,705,1106,0,721,1001,64,1,64,1002,64,2,64,109,10,1202,1,1,63,1008,63,40,63,1005,63,745,1001,64,1,64,1105,1,747,4,727,1002,64,2,64,109,10,21108,46,46,-2,1005,1012,765,4,753,1105,1,769,1001,64,1,64,1002,64,2,64,109,-2,1205,8,781,1106,0,787,4,775,1001,64,1,64,1002,64,2,64,109,-9,2101,0,0,63,1008,63,23,63,1005,63,809,4,793,1105,1,813,1001,64,1,64,1002,64,2,64,109,9,1206,8,831,4,819,1001,64,1,64,1106,0,831,1002,64,2,64,109,-9,2102,1,-2,63,1008,63,22,63,1005,63,855,1001,64,1,64,1106,0,857,4,837,1002,64,2,64,109,4,21101,47,0,10,1008,1017,50,63,1005,63,877,1105,1,883,4,863,1001,64,1,64,1002,64,2,64,109,18,1206,-4,895,1105,1,901,4,889,1001,64,1,64,4,64,99,21101,0,27,1,21102,915,1,0,1106,0,922,21201,1,56639,1,204,1,99,109,3,1207,-2,3,63,1005,63,964,21201,-2,-1,1,21102,1,942,0,1106,0,922,22102,1,1,-1,21201,-2,-3,1,21101,0,957,0,1106,0,922,22201,1,-1,-2,1106,0,968,22102,1,-2,-2,109,-3,2106,0,0";

}