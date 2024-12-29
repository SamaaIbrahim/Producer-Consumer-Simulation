package com.ProducerConsumer.ProducerConsumer.model.Dto;

import com.ProducerConsumer.ProducerConsumer.model.Impl.Machine;
import com.ProducerConsumer.ProducerConsumer.service.Impl.RandomGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineDto {
    private String id;
    private long processTime;
    private List<AssemblyLineDto> inQueues;
    private AssemblyLineDto outQueue;

    @Autowired
    @JsonIgnore
    private RandomGenerator randomGenerator;

    @JsonIgnore
    public Machine getMachine() {
        return Machine.builder()
                .id(this.id)
                .processTime(this.randomGenerator.generateProcessTime())
                .inQueues(this.inQueues.stream().map(AssemblyLineDto::getAssemblyLine).collect(Collectors.toList()))
                .outQueue(this.outQueue.getAssemblyLine())
                .build();
    }
}
