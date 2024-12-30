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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineDto {
    private String id;
    private List<String> inQueuesIds;
    private String outQueueId;

    @Autowired
    @JsonIgnore
    private RandomGenerator randomGenerator;

    @JsonIgnore
    public Machine getMachine() {
        return Machine.builder()
                .id(this.id)
                .processTime(this.randomGenerator.generateProcessTime())
                .build();
    }
}
