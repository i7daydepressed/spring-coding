package org.example.interfaces;

import org.example.exceptions.RandomGeneratorException;

public interface RandomGenerator {
    public Integer next() throws RandomGeneratorException;
}