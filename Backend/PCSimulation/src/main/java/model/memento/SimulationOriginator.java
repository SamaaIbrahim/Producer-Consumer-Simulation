package model.memento;

import lombok.Data;

import model.Impl.AssemblyLine;
import model.Impl.Machine;
import model.Impl.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class SimulationOriginator {
    List<Machine> machines;
    List<AssemblyLine> assemblyLines;
    List<Product> products;
}
