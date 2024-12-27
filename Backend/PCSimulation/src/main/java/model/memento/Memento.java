package model.memento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Impl.AssemblyLine;
import model.Impl.Machine;
import model.Impl.Product;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memento {
    List<Product> products;
    List<AssemblyLine> assemblyLines;
    List<Machine> machines;
}
