package com.ProducerConsumer.ProducerConsumer.model.Impl;

import com.ProducerConsumer.ProducerConsumer.model.DealingWithWebSocketsItem;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.BlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLine implements Subject, DealingWithWebSocketsItem { // Queue
    String id;
    BlockingDeque<Product> queue;
    List<Observer> observers;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void addProduct(Product product) throws InterruptedException {
        queue.put(product);
        sendWebSocket();

        notifyAllObservers();
    }

    public Product getProduct() throws InterruptedException {
        sendWebSocket();
        return queue.take();
    }

    @Override
    public void sendWebSocket() {

    }

    @Override
    public void notifyAllObservers() {
        synchronized (observers) {
            for (Observer observer : observers) {
                observer.update(this);
            }
        }
    }
}
