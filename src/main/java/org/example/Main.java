package org.example;

import java.util.Date;
import java.util.function.Predicate;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(Config.class)) {

                String hello = context.getBean("hello", String.class);
                Integer random = context.getBean("random", Integer.class);
                Date firstLoginDate = context.getBean("firstLoginDate", Date.class);
                Predicate<Integer> range = context.getBean("range", Predicate.class);
                Integer max = context.getBean("max", Integer.class);
                Integer min = context.getBean("min", Integer.class);

                System.out.println(hello);
                System.out.println(random);
                System.out.println(firstLoginDate);
                System.out.println(range.test(1));
                System.out.println(max);
                System.out.println(min);
        }
    }
}
