package com.ProducerConsumer.ProducerConsumer.service.Impl;

import com.ProducerConsumer.ProducerConsumer.model.Dto.AssemblyLineDto;
import com.ProducerConsumer.ProducerConsumer.model.Dto.MachineDto;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SimulationDto;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Product;
import com.ProducerConsumer.ProducerConsumer.model.memento.SimulationOriginator;
import com.ProducerConsumer.ProducerConsumer.service.ISimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationService implements ISimulationService {
    SimpMessagingTemplate messagingTemplate;

    SimulationOriginator simulationOriginator;
    RandomGenerator randomGenerator;

    @Autowired
    public SimulationService(SimulationOriginator simulationOriginator, RandomGenerator randomGenerator, SimpMessagingTemplate messagingTemplate) {
        this.simulationOriginator = simulationOriginator;
        this.randomGenerator = randomGenerator;
        this.messagingTemplate=messagingTemplate;
    }

    @Override
    public void simulate(SimulationDto simulationDto) {

        simulationOriginator.setMachines(
                simulationDto
                        .getMachineDtos()
                        .stream()
                        .map(MachineDto::getMachine)
                        .collect(Collectors.toList())
        );

        simulationOriginator.setAssemblyLines(
                simulationDto
                        .getAssemblyLineDtos()
                        .stream()
                        .map(AssemblyLineDto::getAssemblyLine)
                        .collect(Collectors.toList())
        );

        List<Product> products = new ArrayList<>();
        for(int i = 0; i < simulationDto.getNumberOfProducts(); i++) {
            products.add(new Product(Integer.toString(i), randomGenerator.generateColor()));
        }
        simulationOriginator.setProducts(products);
        simulationOriginator.simulate();
    }

}