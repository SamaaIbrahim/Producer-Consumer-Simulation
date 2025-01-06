package com.ProducerConsumer.ProducerConsumer.model.memento;

import com.ProducerConsumer.ProducerConsumer.model.memento.Memento;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Stack;

@Component
public class CareTaker {
    private Memento memento;


    public void addMemento(Memento memento) {
        this.memento = memento;
    }

    public Memento getMemento() throws EmptyStackException {
        return memento.clone();
    }
}
