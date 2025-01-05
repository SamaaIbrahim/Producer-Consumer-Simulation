package com.ProducerConsumer.ProducerConsumer.model.Dto;

import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketDto {
    String id;
    String color;
    int size;


}
