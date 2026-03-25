package org.example.time;

import org.example.annotations.TimeMeasure;

class Test implements Timeable {
    @TimeMeasure
    public void time() {
        System.out.println("test");
    }
}