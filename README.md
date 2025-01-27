# Producer-Consumer Simulation


[Demo Video](https://drive.google.com/file/d/1wF6-tbwQ6jbp9eKXrqbccVvImaZwmICK/view?usp=sharing)
---

## Project Overview

The **Producer-Consumer Simulation** is an object-oriented application designed to simulate a production line consisting of multiple machines (producers/consumers) and queues (intermediaries). The simulation is visualized through a user-friendly UI and employs several design patterns to ensure efficient operation, synchronization, and real-time feedback.

---

## Key Features
- **Graphical UI**: Users can add and connect machines and queues dynamically.
- **Random Input and Service Times**: Products arrive at random intervals and are processed by machines with random service times.
- **Real-Time Simulation**: Machines operate in separate threads, and queue statuses update live on the UI.
- **Replayability**: Save and replay previous simulations or start new ones.
- **Visual Indicators**: Machines flash when processing products, and each product is assigned a unique color for easy tracking.

---

## Technologies Used
- **Backend**: Spring Boot (Java)
- **Frontend**: React.js
- **Build Tools**: Maven for backend, npm for frontend
- **Real-Time Communication**: WebSockets

---

## Design Patterns Used
1. **Producer-Consumer Design Pattern**:
   - Machines act as producers and consumers.
   - Queues function as thread-safe intermediaries.
   - Synchronization ensures smooth product flow between machines and queues.

2. **Observer Design Pattern**:
   - **Subject**: Queues notify subscribed machines when products are available.
   - **Observers**: Machines react to these notifications to process products.

3. **Memento Design Pattern**:
   - Saves and restores the simulation's state, including machines, queues, and product data.
   - Enables replaying past simulations seamlessly.

4. **Singleton Pattern**:
   - Ensures a single instance for service or repository classes using Spring's dependency injection.

5. **Builder Design Pattern**:
   - Constructs complex objects like DTOs and entities step-by-step for clarity and reusability.

6. **Prototype Design Pattern**:
   - Allows cloning of machines, queues, and simulation states for flexible operations.

---

## How to Run the Project
### Backend Setup
1. Navigate to the `producer_consumer` directory.
2. Ensure all dependencies are loaded and Lombok is installed.
3. Run the following commands:
   ```bash
   ./mvnw clean
   ./mvnw install
   ./mvnw spring-boot:run
