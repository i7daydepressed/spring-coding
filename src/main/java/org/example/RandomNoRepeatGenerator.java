package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.example.exceptions.NoMoreNumbersAvailableException;
import org.example.interfaces.RandomGenerator;

public class RandomNoRepeatGenerator implements RandomGenerator {
    private int min;
    private int max;
    private List<Integer> generatedNumbers;

    public RandomNoRepeatGenerator(int min, int max) {
        this.min = min;
        this.max = max;
        this.generateNumbers();
    }

    @Override
    public Integer next() throws NoMoreNumbersAvailableException {
        if (this.generatedNumbers.isEmpty()) {
            throw new NoMoreNumbersAvailableException("No more numbers available in the specified range.");
        }
        return this.generatedNumbers.remove(ThreadLocalRandom.current().nextInt(this.generatedNumbers.size()));
    }

    private void generateNumbers() {
        this.generatedNumbers = IntStream.rangeClosed(min, max)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
