package com.ProducerConsumer.ProducerConsumer.model;

public interface Observer {
    String getId();
    void addSubject(Subject subject);
    void update(Subject subject, Boolean hasProduct);
}
