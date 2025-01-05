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


import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLine implements Subject{
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
        observers.add(observer);
    }

    @Override
    public String toString() {
        return "AssemblyLine{" +
                "id='" + id + '\'' +
                ", size=" + queue.size() +
                ", observers=" + (observers != null ? observers.stream().map(Observer::getId).toList(): null) +
//                ", messagingTemplate=" + messagingTemplate +
                '}';
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
        System.out.println("_______________________________________________________________________________");
        System.out.println("product in: " + this);
        System.out.println("_______________________________________________________________________________");

//        messagingTemplate.convertAndSend("/Simulate/queue/"+socketDto);
        notifyAllObservers();
    }

    public Product getProduct() throws InterruptedException {
        socketDto.setSize(queue.size());
        System.out.println("_______________________________________________________________________________");
        System.out.println("product out: " + this);
        System.out.println("_______________________________________________________________________________");

//        messagingTemplate.convertAndSend("/Simulate/queue/"+ socketDto);
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
