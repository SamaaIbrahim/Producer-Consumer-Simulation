package com.ProducerConsumer.ProducerConsumer.model.Impl;

import ch.qos.logback.core.joran.conditional.ThenAction;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SocketDto;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Runnable, Observer{
    private volatile boolean isRunning = false;
    private Map<String, Boolean> queueHasProduct;
    private String id;
    private long processTime;
    private List<AssemblyLine> inQueues;
    private AssemblyLine outQueue;
    private Product product;
    private SimpMessagingTemplate messagingTemplate;
   private SocketDto socketDto;
   final Object obj = new Object();
    @Autowired
    public Machine(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", processTime=" + processTime +
                ", inQueues=" + (inQueues!= null? inQueues.stream().map(AssemblyLine::getId).toList() : null)  +
                ", outQueue=" + outQueue.getId() +
                ", color= " + (product != null ? product.getColor() : "grey") +
//                ", messagingTemplate=" + messagingTemplate +
                '}';
    }

    @Override
    public void addSubject(Subject subject) {
        inQueues.add((AssemblyLine) subject);
        this.isRunning = true;
        if(queueHasProduct == null){queueHasProduct = new HashMap<>();}
        queueHasProduct.put(((AssemblyLine) subject).getId(), false);
    }

    @Override
    public void update(Subject subject, Boolean hasProduct) {
        queueHasProduct.put(((AssemblyLine) subject).getId(), hasProduct);
//        if(hasProduct){
//            isRunning = true;
//        }
    }

    @Override
    public void run() {
        System.out.println("machine thread " + this.id + " is created");
        while (isRunning) {
            try {
//                synchronized (this) {
                    System.out.println("ahmed" + this.id);
                    if(product == null) {
                        for(AssemblyLine queue : inQueues){
                            if(queueHasProduct.get(queue.getId())) {
                                this.product = queue.getProduct();
                                break;
                            }
                        }
                        System.out.println("stop");
//                        if(product == null) {
//                            stop();
//                            break;
//                        }
                        synchronized (obj){
                            if(product != null) {

                                // send Queue to Machine


                                Thread.sleep(1000);
                                this.socketDto = SocketDto.builder()
                                        .id(this.id)
                                        .color(product.getColor())
                                        .build();
                                System.out.println("_______________________________________________________________________________");
                                System.out.println("machine process start: " + this);
                                System.out.println("_______________________________________________________________________________");
                                messagingTemplate.convertAndSend("/Simulate/machine", socketDto);

                            }

                        }


                    }
                    else {
                        Thread.sleep(this.processTime);

                        socketDto.setColor("808080");
                        messagingTemplate.convertAndSend("/Simulate/machine", socketDto);
                        System.out.println("Machine process completed: " + this);
                        Thread.sleep(1000);

                        outQueue.addProduct(product);
                        this.product = null;



                    }
//                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void stop() {
        this.isRunning=false;
    }


}
