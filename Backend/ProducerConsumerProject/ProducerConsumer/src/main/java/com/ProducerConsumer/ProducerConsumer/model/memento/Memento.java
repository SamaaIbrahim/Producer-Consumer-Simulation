package com.ProducerConsumer.ProducerConsumer.model.memento;

import com.ProducerConsumer.ProducerConsumer.model.Impl.AssemblyLine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Machine;
import com.ProducerConsumer.ProducerConsumer.model.Impl.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Memento {
    List<Product> products;
    List<AssemblyLine> assemblyLines;
    List<Machine> machines;
}
