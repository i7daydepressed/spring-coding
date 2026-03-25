package org.example;

import java.util.Date;
import java.util.function.Predicate;

import org.example.exceptions.RandomGeneratorException;
import org.example.interfaces.RandomGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class Config {
    
    @Bean
    public String hello(){
        return "hello";
    }
    @Bean
    @Scope("prototype")
    public Integer random(@Qualifier("randomNoRepeatGenerator") RandomGenerator randomGenerator) throws RandomGeneratorException {
        return randomGenerator.next();
    }
    @Bean
    // синглтон
    @Qualifier("randomNoRepeatGenerator")
    public RandomNoRepeatGenerator randomNoRepeatGenerator(@Qualifier("min") int min, @Qualifier("max") int max) {
        return new RandomNoRepeatGenerator(min, max);
    }

    @Bean
    @Lazy
    public Date firstLoginDate() {
        return new Date();
    }

    @Bean
    public Predicate<Integer> range() {
        return x -> x >= 2 && x <= 5;
    }

    @Bean
    public int max() {
        return 100;
    }

    @Bean
    public int min() {
        return 0;
    }
}
