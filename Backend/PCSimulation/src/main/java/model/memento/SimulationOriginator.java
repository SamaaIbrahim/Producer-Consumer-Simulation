package model.memento;

import lombok.Data;

import model.Impl.AssemblyLine;
import model.Impl.Machine;
import model.Impl.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.List;

@Data
@Component
public class SimulationOriginator {
    List<Machine> machines;
    List<AssemblyLine> assemblyLines;
    List<Product> products;

    CareTaker careTaker;
    @Autowired
    public SimulationOriginator(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    void SaveToCareTaker() {
        careTaker.addMemento(new Memento(this.products, this.assemblyLines, this.machines));
    }

    void loadFromCareTaker() {
        try{
            Memento memento = careTaker.getMemento();
            this.machines = memento.getMachines();
            this.assemblyLines = memento.getAssemblyLines();
            this.products = memento.getProducts();
        } catch (EmptyStackException e) {
            System.out.println(e.getMessage());
        }
    }
}
