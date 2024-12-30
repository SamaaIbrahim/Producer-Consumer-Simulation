package com.ProducerConsumer.ProducerConsumer.model.Impl;

import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Runnable, Observer {
    private String id;
    private long processTime;
    private boolean isProcessing;
    private List<AssemblyLine> inQueues;
    private AssemblyLine outQueue;
    private Product product;

    @Override
    public void addSubject(Subject subject) {
        inQueues.add((AssemblyLine) subject);
    }

    @Override
    public void update(Subject subject) {
        try {
            this.product = ((AssemblyLine) subject).getProduct();
            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(product != null) {
                    outQueue.addProduct(this.product);
                    this.product = null;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
