package com.ProducerConsumer.ProducerConsumer.service.Impl;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomGenerator {
    Random random;

    public RandomGenerator() {
        this.random = new Random();
    }

    public String generateColor() {

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return String.format("%02X%02X%02X", red, green, blue);
    }

    public long generateProcessTime() {
        return 1000 + random.nextLong(9001);
    }

    public long productRate() {
        return 500 + + random.nextLong(30001);
    }
}