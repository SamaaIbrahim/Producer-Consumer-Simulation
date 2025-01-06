package com.ProducerConsumer.ProducerConsumer.model.Impl;

import ch.qos.logback.core.joran.conditional.ThenAction;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SocketDto;
import com.ProducerConsumer.ProducerConsumer.model.Dto.TransferDto;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Runnable, Observer, Cloneable{
    private volatile boolean isRunning = false;
    private Map<String, Boolean> queueHasProduct;
    private String id;
    private long processTime;
    private List<AssemblyLine> inQueues;
    private AssemblyLine outQueue;
    private Product product;
    private SimpMessagingTemplate messagingTemplate;
   private SocketDto socketDto;
    TransferDto transferDto;
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

//                System.out.println("ahmed" + this.id);
//                System.out.println(inQueues);
                if(!isRunning) break;

                if(product == null) {
                        for(AssemblyLine queue : inQueues){
                            if(queueHasProduct.get(queue.getId())) {
                                if(!isRunning) break;
                                this.product = queue.getProduct();
                                if(product != null) {
                                    transferDto = new TransferDto(queue.getId(),this.id, product.getColor());
                                }
                                messagingTemplate.convertAndSend("/Simulate/transfer", transferDto);
                                Thread.sleep(1500);

                                break;
                            }
                        }
//                        if(product == null) {
//                            stop();
//                            break;
//                        }
                        synchronized (obj){
                            if(product != null) {

                                if(!isRunning) break;
                                // send Queue to Machine
                            //    messagingTemplate.convertAndSend("/Simulate/transfer", transferDto);

                                this.socketDto = SocketDto.builder()
                                        .id(this.id)
                                        .color(product.getColor())
                                        .build();
                                System.out.println("_______________________________________________________________________________");
                                System.out.println("machine process start: " + this);
                                System.out.println("_______________________________________________________________________________");

                                if(!isRunning) break;
                                messagingTemplate.convertAndSend("/Simulate/machine", socketDto);
                            }

                        }


                    }
                    else {
                        Thread.sleep(this.processTime);
                        if(!isRunning) break;

                        socketDto.setColor("808080");
                        messagingTemplate.convertAndSend("/Simulate/machine", socketDto);
                        System.out.println("Machine process completed: " + this);

                        this.transferDto =  new TransferDto(this.id, outQueue.getId(), product.getColor());
                        messagingTemplate.convertAndSend("/Simulate/transfer",transferDto);


                        if(!isRunning) break;
                        Thread.sleep(1500);
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


    @Override
    public Machine clone() {
        try {
            Machine clone = (Machine) super.clone();
            clone.queueHasProduct = new HashMap<>(this.queueHasProduct); // Deep copy map
            clone.inQueues = new ArrayList<>(); // Deep copy list
            clone.socketDto = this.socketDto != null ? this.socketDto : null; // Assuming SocketDto implements clone
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
