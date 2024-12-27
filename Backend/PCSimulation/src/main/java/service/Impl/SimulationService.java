package service.Impl;

import model.Dto.AssemblyLineDto;
import model.Dto.MachineDto;
import model.Dto.SimulationDto;
import model.Impl.Product;
import model.memento.SimulationOriginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ISimulationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationService implements ISimulationService {
    SimulationOriginator simulationOriginator;
    RandomGenerator randomGenerator;

    @Autowired
    public SimulationService(SimulationOriginator simulationOriginator, RandomGenerator randomGenerator) {
        this.simulationOriginator = simulationOriginator;
        this.randomGenerator = randomGenerator;
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

    }
}