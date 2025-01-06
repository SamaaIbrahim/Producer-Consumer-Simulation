package com.ProducerConsumer.ProducerConsumer.model.memento;

import com.ProducerConsumer.ProducerConsumer.model.Impl.AssemblyLine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Machine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Product;
import com.ProducerConsumer.ProducerConsumer.model.Observer;
import lombok.Data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
public class SimulationOriginator {
    private List<Machine> machines;
    private List<AssemblyLine> assemblyLines;
    private List<Product> products;
    private long rate;

    CareTaker careTaker;
    @Autowired
    public SimulationOriginator(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    public void saveToCareTaker() {
        Map<String, AssemblyLine> newAssemblyLines = new HashMap<>();
        Map<String, Machine> newMachines = new HashMap<>();
        for(Machine machine: machines) {
            newMachines.put(machine.getId(), machine.clone());
        }
        for(AssemblyLine assemblyLine: assemblyLines) {
            newAssemblyLines.put(assemblyLine.getId(), assemblyLine.clone());
        }
        for(AssemblyLine assemblyLine: assemblyLines){
            AssemblyLine newAssemblyLine = assemblyLine.clone();
            for(Observer observer : assemblyLine.getObservers()) {
                Machine machine = newMachines.get(observer.getId());
                newAssemblyLine.addObserver(machine);
                machine.addSubject(newAssemblyLine);
            }
            newAssemblyLines.put(newAssemblyLine.getId(), newAssemblyLine);
        }
        for(Machine machine : machines){
            Machine machine1 = newMachines.get(machine.getId());
            machine1.setOutQueue(newAssemblyLines.get(machine.getOutQueue().getId()));
        }
        careTaker.addMemento(new Memento(this.products, new ArrayList<>(newAssemblyLines.values()), new ArrayList<>(newMachines.values()), rate));


    }

    public void loadFromCareTaker() {
        try{
            Memento memento = careTaker.getMemento();


            Map<String, AssemblyLine> newAssemblyLines = new HashMap<>();
            Map<String, Machine> newMachines = new HashMap<>();
            for(Machine machine: memento.machines) {
                newMachines.put(machine.getId(), machine.clone());
            }
            for(AssemblyLine assemblyLine: memento.assemblyLines) {
                newAssemblyLines.put(assemblyLine.getId(), assemblyLine.clone());
            }
            for(AssemblyLine assemblyLine: memento.assemblyLines){
                AssemblyLine newAssemblyLine = assemblyLine.clone();
                for(Observer observer : assemblyLine.getObservers()) {
                    Machine machine = newMachines.get(observer.getId());
                    newAssemblyLine.addObserver(machine);
                    machine.addSubject(newAssemblyLine);
                }
                newAssemblyLines.put(newAssemblyLine.getId(), newAssemblyLine);
            }
            for(Machine machine : memento.machines){
                Machine machine1 = newMachines.get(machine.getId());
                machine1.setOutQueue(newAssemblyLines.get(machine.getOutQueue().getId()));
            }
            this.machines = new ArrayList<>(newMachines.values());
            this.assemblyLines = new ArrayList<>(newAssemblyLines.values());
            this.products = memento.getProducts();
            this.rate = memento.rate;
//            System.out.println("samaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaa");
//            System.out.println(this.machines);
//            System.out.println(this.assemblyLines);
//            System.out.println(this.products);
//            System.out.println("samaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaa");
        } catch (EmptyStackException e) {
            System.out.println(e.getMessage());
        }

    }
    public void simulate(){
        for(Machine machine : machines){
            Thread thread =new Thread(machine);
            thread.start();
            machine.setRunning(true);
        }

    }
    public void stopSimulate(){
        if(machines != null){
            for(Machine machine : machines){
                machine.stop();
            }
        }
    }

}
