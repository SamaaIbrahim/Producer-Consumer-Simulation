package com.ProducerConsumer.ProducerConsumer.model.Impl;

import com.ProducerConsumer.ProducerConsumer.model.Dto.SocketDto;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import com.ProducerConsumer.ProducerConsumer.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine implements Runnable, Observer{
    private volatile boolean isRunning = true;
    private String id;
    private long processTime;
    private boolean isProcessing;
    private List<AssemblyLine> inQueues;
    private AssemblyLine outQueue;
    private Product product;
    private SimpMessagingTemplate messagingTemplate;
   private SocketDto socketDto;
    @Autowired
    public Machine(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", processTime=" + processTime +
                ", inQueues=" + inQueues.stream().map(AssemblyLine::getId).toList() +
                ", outQueue=" + outQueue.getId() +
                ", color= " + (isProcessing ? product.getColor() : "not processing") +
//                ", messagingTemplate=" + messagingTemplate +
                '}';
    }

    @Override
    public void addSubject(Subject subject) {
        inQueues.add((AssemblyLine) subject);
    }

    @Override
    public void update(Subject subject) {
        try {
            this.product = ((AssemblyLine) subject).getProduct();
            this.isProcessing = true;
            this.socketDto = SocketDto.builder()
                    .id(this.id)
                    .color(product.getColor())
                    .build();
            System.out.println("_______________________________________________________________________________");
            System.out.println("machine process start: " + this);
            System.out.println("_______________________________________________________________________________");

            messagingTemplate.convertAndSend("/Simulate/machine", socketDto);


            Thread.sleep(processTime);

            this.isProcessing = false;
            outQueue.addProduct(product);

            this.product = null;
            socketDto.setColor(null);
            messagingTemplate.convertAndSend("/Simulate/machine", socketDto);
            System.out.println("Machine process completed: " + this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
//            try {
//                synchronized (this) {
//                    if (product != null) {
//                        outQueue.addProduct(this.product);
//                        this.product = null;
//                        socketDto.setColor(null);
//                        System.out.println("_______________________________________________________________________________");
//                        System.out.println("machine process end: " + this);
//                        System.out.println("_______________________________________________________________________________");
//
//                        messagingTemplate.convertAndSend("/Simulate/machine", socketDto);
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                throw new RuntimeException(e);
//            }
        }
    }

    public void stop() {
     this.isRunning=false;
    }


}
