package com.ProducerConsumer.ProducerConsumer.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationDto {
    Long numberOfProducts;
    List<MachineDto> machineDtos;
    List<AssemblyLineDto> assemblyLineDtos;
}
