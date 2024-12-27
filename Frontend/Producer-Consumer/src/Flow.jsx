import React, { useState } from "react";
import './App.css'; 
import machineicon from './assets/machine.svg';
import ReactFlow, {
    addEdge,
    useEdgesState,
    useNodesState,
    Background,
    Controls,
} from "reactflow";
import "reactflow/dist/style.css";
import MachineNode from "./MachineNode";
import queueicon from "./assets/queue.svg";
import counter from "./assets/counter.svg";
import redoicon from "./assets/redo.svg";
import newicon from "./assets/newicon.svg";

// Initial nodes and edges
const initialNodes = [
    { id: "machine-1", position: { x: 100, y: 100 }, data: { label: "M1" }, type: "machine" },
    { id: "machine-2", position: { x: 200, y: 200 }, data: { label: "M2" }, type: "machine" },
    { id: "queue-1", position: { x: 300, y: 100 }, data: { label: "Queue 1" }, type: "default" },
    { id: "queue-2", position: { x: 500, y: 100 }, data: { label: "Queue 2" }, type: "default" },
];

const initialEdges = [];

const nodeTypes = { machine: MachineNode };

const SimulationFlow = () => {
    const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
    const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
    const [shape, setShape] = useState("");  // Initialize shape state inside the component
    const [menu, setMenu] = useState(false);  // Initialize as boolean
    const [numberOfProducts, setNumberOfProducts] = useState(0);  // Correct state name
    const [redo, setRedo] = useState(false);  // Use a boolean for redo state

    const onConnect = (params) => setEdges((eds) => addEdge(params, eds));

    const handleNumberOfProductsChange = (e) => {
        setNumberOfProducts(e.target.value);
    };

    return (
        <div className="flow">
            <div className='bar'>
                <button className='icon' onClick={() => {setShape("machine");setMenu(false)}}>
                    <img src={machineicon} alt="machineicon" />
                </button>
                <button className="icon" onClick={() => {setShape("queue");setMenu(false);}}>
                    <img src={queueicon} alt="queue" />  
                </button>
                <button className='icon' onClick={() => setMenu(!menu)}>
                    <img src={counter} alt="counter" />
                </button>
                {/* Conditional rendering of the menu */}
                {menu && (
                    <div className="inputmenu">
                        <input
                            id="products"
                            type="number"  // Changed to number input for product count
                            value={numberOfProducts}
                            onChange={handleNumberOfProductsChange}
                            style={{
                                background:"white",
                                opacity: 0.7,  // Set opacity to make input visible
                                position: "absolute",
                                width: "50%",
                                zIndex:"10px",
                                padding: "10px",
                                pointerEvents: "auto",
                                color:"black"
                            }}
                        />
                    </div>
                )}
                <button className="icon" onClick={() => {setRedo(false);setMenu(false)}}>
                    <img src={newicon} alt="new" />  
                </button>
                <button  className="icon" onClick={() => {setRedo(true);setMenu(false)}}>
                    <img src={redoicon} alt="redo" />  
                </button>
            </div>

            <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                onConnect={onConnect}
                nodeTypes={nodeTypes}
                fitView
            >
                <Background />
                <Controls />
            </ReactFlow>
        </div>
    );
};

export default SimulationFlow;
