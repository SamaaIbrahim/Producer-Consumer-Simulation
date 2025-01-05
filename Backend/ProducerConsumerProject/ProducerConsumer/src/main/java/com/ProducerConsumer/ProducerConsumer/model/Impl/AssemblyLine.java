package com.ProducerConsumer.ProducerConsumer.model.Impl;

import com.ProducerConsumer.ProducerConsumer.model.DealingWithWebSocketsItem;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SocketDto;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import com.ProducerConsumer.ProducerConsumer.service.Impl.WebSocktingService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.BlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLine implements Subject{
    // Queue
    String id;
    BlockingDeque<Product> queue;
    List<Observer> observers;
    SocketDto socketDto;
    private  SimpMessagingTemplate messagingTemplate;
    @Autowired
    public AssemblyLine(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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
        socketDto=SocketDto.builder()
                .id(this.id)
                .size(queue.size())
                .build();
        messagingTemplate.convertAndSend("/Simulate/queue"+socketDto);
        notifyAllObservers();
    }

    public Product getProduct() throws InterruptedException {
        socketDto.setSize(queue.size());
        messagingTemplate.convertAndSend("/Simulate/queue"+socketDto);
        return queue.take();
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
