package com.ProducerConsumer.ProducerConsumer.model.Dto;

import com.ProducerConsumer.ProducerConsumer.model.Impl.AssemblyLine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.LinkedBlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLineDto {
    private String id;
    @JsonProperty("isStart")
    private boolean isStart;
    @JsonProperty("isEnd")
    private boolean isEnd;

    @JsonIgnore
    public AssemblyLine getAssemblyLine() {
        return AssemblyLine.builder()
                .id(this.id)
                .queue(new LinkedBlockingDeque<>())
                .build();
    }
}
