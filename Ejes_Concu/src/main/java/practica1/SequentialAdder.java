package practica1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class SequentialAdder {

    public static final int N_THREADS = 5;
    public static final int DATA_SAMPLE_SIZE = 16;

    private static int [] data;

    private static int add(int a, int b) {
        sleepRandom(1000);
        return a + b;
    }

    private static void initDataset() {
        data = ThreadLocalRandom.current()
                .ints(DATA_SAMPLE_SIZE, 1, 10)
                .toArray();
        println("Sample dataset: " + Arrays.toString(SequentialAdder.data));
    }

    public static void main(String[] args) {
        initDataset();

        LocalDateTime start = LocalDateTime.now();
        int sum = 0;
        for (int i = 0; i < DATA_SAMPLE_SIZE; i++) {
            sum = add(sum, data[i]);
        }
        Duration time = Duration.between(start, LocalDateTime.now());
        println("Total sum : " + sum);

        println("SequentialAdder computed sum in " + Duration.between(start, LocalDateTime.now()));
    }
}
