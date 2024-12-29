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
    private List<AssemblyLine> inQueues;
    private AssemblyLine outQueue;


    @Override
    public void run() {

    }

    @Override
    public void addSubject(Subject subject) {

    }

    @Override
    public void update() {

    }
}
