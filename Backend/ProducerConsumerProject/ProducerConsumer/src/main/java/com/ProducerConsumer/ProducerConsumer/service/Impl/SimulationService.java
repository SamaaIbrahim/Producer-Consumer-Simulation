package com.ProducerConsumer.ProducerConsumer.service.Impl;

import com.ProducerConsumer.ProducerConsumer.model.Dto.AssemblyLineDto;
import com.ProducerConsumer.ProducerConsumer.model.Dto.MachineDto;
import com.ProducerConsumer.ProducerConsumer.model.Dto.SimulationDto;
import com.ProducerConsumer.ProducerConsumer.model.Impl.AssemblyLine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Machine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Product;
import com.ProducerConsumer.ProducerConsumer.model.memento.CareTaker;
import com.ProducerConsumer.ProducerConsumer.model.memento.SimulationOriginator;
import com.ProducerConsumer.ProducerConsumer.service.ISimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

@Service
public class SimulationService implements ISimulationService {
    SimpMessagingTemplate messagingTemplate;
    SimulationOriginator simulationOriginator;
    RandomGenerator randomGenerator;
    CareTaker careTaker;

    @Autowired
    public SimulationService(SimulationOriginator simulationOriginator, RandomGenerator randomGenerator, SimpMessagingTemplate messagingTemplate, CareTaker careTaker) {
        this.simulationOriginator = simulationOriginator;
        this.randomGenerator = randomGenerator;
        this.messagingTemplate=messagingTemplate;
        this.careTaker = careTaker;
    }

    @Override
    public void simulate(SimulationDto simulationDto) throws IllegalArgumentException{

        // Map machines and assembly lines for quick lookup
        List<Machine> machines = simulationDto
                .getMachineDtos()
                .stream()
                .map(MachineDto::getMachine)
                .collect(Collectors.toList());
        Map<String, Machine> machinesMap = new HashMap<>();
        for (Machine machine : machines) {
            machinesMap.put(machine.getId(), machine);
        }

        List<AssemblyLine> assemblyLines = simulationDto
                .getAssemblyLineDtos()
                .stream()
                .map(AssemblyLineDto::getAssemblyLine)
                .collect(Collectors.toList());
        Map<String, AssemblyLine> assemblyLinesMap = new HashMap<>();
        for (AssemblyLine assemblyLine : assemblyLines) {
            assemblyLinesMap.put(assemblyLine.getId(), assemblyLine);
        }

        // Configure machines with their inQueues and outQueue
        for (MachineDto machineDto : simulationDto.getMachineDtos()) {
            Machine machine = machinesMap.get(machineDto.getId());
            if (machine == null) {
                throw new IllegalArgumentException("Machine with ID " + machineDto.getId() + " not found.");
            }

            // Set outQueue
            if (assemblyLinesMap.containsKey(machineDto.getOutQueueId())) {
                AssemblyLine outQueue = assemblyLinesMap.get(machineDto.getOutQueueId());
                if(outQueue.getObservers() == null)
                {
                    outQueue.setObservers(new ArrayList<>());
                }
                if(outQueue.getQueue() == null) {
                    outQueue.setQueue(new LinkedBlockingDeque<>());
                }
                if(outQueue.getMessagingTemplate() == null) {
                    outQueue.setMessagingTemplate(this.messagingTemplate);
                }
                machine.setOutQueue(outQueue);
            }

            // Add inQueues
            for (String assemblyLineId : machineDto.getInQueuesIds()) {
                if (assemblyLinesMap.containsKey(assemblyLineId)) {
                    if(machine.getInQueues() == null) {
                        machine.setInQueues(new ArrayList<>());
                    }

                    AssemblyLine inQueue = assemblyLinesMap.get(assemblyLineId);
                    if(inQueue.getObservers() == null)
                    {
                        inQueue.setObservers(new ArrayList<>());
                    }
                    if(inQueue.getQueue() == null) {
                        inQueue.setQueue(new LinkedBlockingDeque<>());
                    }
                    if(inQueue.getMessagingTemplate() == null) {
                        inQueue.setMessagingTemplate(this.messagingTemplate);
                    }
                    if(machine.getMessagingTemplate() == null) {
                        machine.setMessagingTemplate(this.messagingTemplate);
                    }

                    inQueue.addObserver(machine);
                    machine.addSubject(inQueue);
                } else {
                    throw new IllegalArgumentException("Assembly line with ID " + assemblyLineId + " not found for machine " + machineDto.getId());
                }
            }
        }





        // Validate start and end assembly lines
        AssemblyLine startAssemblyLine = null;
        AssemblyLine endAssemblyLine = null;
        for (AssemblyLineDto assemblyLineDto : simulationDto.getAssemblyLineDtos()) {
            if (assemblyLineDto.isStart()) {
                if (startAssemblyLine != null) {
                    throw new IllegalArgumentException("Multiple start queues detected: " + startAssemblyLine.getId() + " and " + assemblyLineDto.getId());
                }
                startAssemblyLine = assemblyLinesMap.get(assemblyLineDto.getId());
            }

            if (assemblyLineDto.isEnd()) {
                if (endAssemblyLine != null) {
                    throw new IllegalArgumentException("Multiple end queues detected: " + endAssemblyLine.getId() + " and " + assemblyLineDto.getId());
                }
                endAssemblyLine = assemblyLinesMap.get(assemblyLineDto.getId());
            }
        }

        if (startAssemblyLine == null) {
            throw new IllegalArgumentException("No starting queue specified.");
        }
        if (endAssemblyLine == null) {
            throw new IllegalArgumentException("No ending queue specified.");
        }

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < simulationDto.getNumberOfProducts(); i++) {
            products.add(new Product(Integer.toString(i), randomGenerator.generateColor()));
        }
        System.out.println(Arrays.deepToString(products.toArray()));

//        try {
//            startAssemblyLine.addProduct(products.toArray(new Product[0]));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }


//        for(Product product : products) {
//            try {
//                startAssemblyLine.addProduct(product);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        }

        simulationOriginator.setAssemblyLines(assemblyLines);
        simulationOriginator.setMachines(machines);
        simulationOriginator.setProducts(products);
        simulationOriginator.saveToCareTaker();


        long rate = randomGenerator.productRate();
        System.out.println("Product input Rate: " + rate);
        AssemblyLine finalStartAssemblyLine = startAssemblyLine;
        Thread puttingThread = new Thread(() -> {
            for (Product product : products) {
                try {
                    finalStartAssemblyLine.addProduct(product);
                    System.out.println("input product added");
                    System.out.println("start queue size : " + finalStartAssemblyLine.getQueue().size());
                    System.out.println(rate);
                    Thread.sleep(rate); // Simulate rate of product generation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    throw new RuntimeException("Thread interrupted during product generation.", e);
                }
            }
        });
        puttingThread.start();

        System.out.println(assemblyLines.toString());
        System.out.println(machines.toString());
        AssemblyLine finalEndAssemblyLine = endAssemblyLine;
        Thread finishThread = new Thread(() -> {
            try {
                while (true) {
                    synchronized (finalEndAssemblyLine) {
                        System.out.println(" check");
                        if (finalEndAssemblyLine.getQueue().size() == products.size()) { // Compare sizes
                            simulationOriginator.stopSimulate();
                            System.out.println("terminated");
                            break;
                        }
                    }
                    Thread.sleep(1000); // Avoid busy waiting
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                throw new RuntimeException("Finish thread interrupted.", e);
            }
        });

        finishThread.start();


        simulationOriginator.simulate(); // Trigger the simulation
    }

    public void replay() {
        simulationOriginator.loadFromCareTaker();
    }
}