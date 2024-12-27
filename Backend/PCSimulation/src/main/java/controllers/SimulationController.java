package controllers;

import model.Dto.SimulationDto;
import org.springframework.web.bind.annotation.*;
import service.Impl.SimulationService;

@RestController

@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:5173")
public class SimulationController {
    SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/simulate")
    public void simulate(@RequestBody SimulationDto simulationDto) {
        simulationService.simulate(simulationDto);
    }

}

