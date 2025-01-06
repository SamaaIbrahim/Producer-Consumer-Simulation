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
        List<AssemblyLine> newAssemblyLines = new ArrayList<>();
        Map<String, Machine> newMachines = new HashMap<>();
        for(Machine machine: machines) {
            newMachines.put(machine.getId(), machine.clone());
        }
        for(AssemblyLine assemblyLine: assemblyLines){
            AssemblyLine newAssemblyLine = assemblyLine.clone();
            for(Observer observer : assemblyLine.getObservers()) {
                Machine machine = newMachines.get(observer.getId());
                newAssemblyLine.addObserver(machine);
                machine.addSubject(newAssemblyLine);
            }
            newAssemblyLines.add(newAssemblyLine);
        }
        careTaker.addMemento(new Memento(this.products, newAssemblyLines, new ArrayList<>(newMachines.values()), rate));


    }

    public void loadFromCareTaker() {
        try{
            Memento memento = careTaker.getMemento();
            this.machines = memento.getMachines();
            this.assemblyLines = memento.getAssemblyLines();
            this.products = memento.getProducts();
            System.out.println("samaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaa");
            System.out.println(this.machines);
            System.out.println(this.assemblyLines);
            System.out.println(this.products);
            System.out.println("samaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaasamaa");
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
        for(Machine machine : machines){
            machine.stop();

        }

    }

}
