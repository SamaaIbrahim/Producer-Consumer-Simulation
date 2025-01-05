package com.ProducerConsumer.ProducerConsumer.controllers;

import com.ProducerConsumer.ProducerConsumer.model.Dto.SimulationDto;
import com.ProducerConsumer.ProducerConsumer.service.Impl.SimulationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> simulate(@RequestBody SimulationDto simulationDto) {

        System.out.println("_____________________________________________________________________________");
        System.out.println(simulationDto.toString());
        System.out.println("_____________________________________________________________________________");
        try {
            simulationService.simulate(simulationDto);
            return new  ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/replay")
    public ResponseEntity<?> replay() {
        try{
            simulationService.replay();
            return ResponseEntity.ok("Simulation replay started");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}

