package com.ProducerConsumer.ProducerConsumer.model.Impl;

import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SocketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLine implements Subject, Cloneable {
    // Queue
    private String id;
    private BlockingDeque<Product> queue = new LinkedBlockingDeque<>();
    private List<Observer> observers = new ArrayList<>();
    private SocketDto socketDto;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AssemblyLine(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void addObserver(Observer observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyAllObservers() {
        synchronized (observers) {
            for (Observer observer : observers) {
                observer.update(this, !queue.isEmpty());
            }
        }
    }

    @Override
    public String toString() {
        return "AssemblyLine{" +
                "id='" + id + '\'' +
                ", size=" + queue.size() +
                ", observers=" + (observers != null ? observers.stream().map(Observer::getId).toList() : null) +
                '}';
    }

    public void addProduct(Product... products) throws InterruptedException {
        for(Product product : products) {
            queue.put(product);
            socketDto = SocketDto.builder()
                    .id(this.id)
                    .size(queue.size())
                    .build();


            // Log to console for debugging
            System.out.println("_______________________________________________________________________________");
            System.out.println("Product added to the queue: " + this);
            System.out.println("_______________________________________________________________________________");

            // Send queue state to subscribers (via WebSocket)
            messagingTemplate.convertAndSend("/Simulate/queue", socketDto);

            // Notify all observers (observers should update their state)
        }
        notifyAllObservers();


    }

    public Product getProduct() throws InterruptedException {
        Product product = queue.take();

        socketDto.setSize(queue.size());

        // Log to console for debugging
        System.out.println("_______________________________________________________________________________");
        System.out.println("Product removed from the queue: " + this);
        System.out.println("_______________________________________________________________________________");

        // Send queue state to subscribers (via WebSocket)
        messagingTemplate.convertAndSend("/Simulate/queue", socketDto);
        notifyAllObservers();

        return product;
    }

    @Override
    public AssemblyLine clone() {
        AssemblyLine clone = new AssemblyLine();
        clone.setId(id);
        // Deep clone queue
        clone.queue = new LinkedBlockingDeque<>();
        clone.setMessagingTemplate(messagingTemplate);
        // Deep clone observers if necessary

        return clone;
    }

}
