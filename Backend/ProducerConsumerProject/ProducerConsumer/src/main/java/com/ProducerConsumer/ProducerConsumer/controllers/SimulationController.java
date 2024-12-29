package com.ProducerConsumer.ProducerConsumer.controllers;

import com.ProducerConsumer.ProducerConsumer.model.Dto.SimulationDto;
import com.ProducerConsumer.ProducerConsumer.service.Impl.SimulationService;
import org.springframework.web.bind.annotation.*;

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

