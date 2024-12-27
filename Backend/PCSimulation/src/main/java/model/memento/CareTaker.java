package model.memento;

import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Stack;

@Component
public class CareTaker {
    Stack<Memento> mementos;

    public CareTaker() {
        this.mementos = new Stack<>();
    }

    public void addMemento(Memento memento) {
        mementos.push(memento);
    }

    public Memento getMemento() throws EmptyStackException {
        return mementos.pop();
    }
}
