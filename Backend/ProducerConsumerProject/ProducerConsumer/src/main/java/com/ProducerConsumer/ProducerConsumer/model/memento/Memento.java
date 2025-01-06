package com.ProducerConsumer.ProducerConsumer.model.memento;

import com.ProducerConsumer.ProducerConsumer.model.Impl.AssemblyLine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Machine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memento implements Cloneable {
    List<Product> products;
    List<AssemblyLine> assemblyLines;
    List<Machine> machines;
    long rate;

    @Override
    public Memento clone() {
        Memento clone = new Memento();
        clone.setProducts(new ArrayList<>(products));
        clone.setMachines(new ArrayList<>(machines));
        clone.setAssemblyLines(new ArrayList<>(assemblyLines));
        clone.setRate(rate);
        return clone;
    }
}
