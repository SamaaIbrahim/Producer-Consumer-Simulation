import React, { useState ,useEffect} from "react";
import './App.css'; 
import ReactFlow, {
    addEdge,
    useEdgesState,
    useNodesState,
    Background,
    Controls,useReactFlow, ReactFlowProvider
} from "reactflow";
import "reactflow/dist/style.css";
import MachineNode from "./MachineNode";
import QueueNode from "./QueueNode";
import queueicon from "./assets/queue.svg";
import counter from "./assets/counter.svg";
import redoicon from "./assets/redo.svg";
import newicon from "./assets/newicon.svg";
import machineicon from './assets/machine.svg';

const nodeTypes = { machine: MachineNode, queue : QueueNode};

const SimulationFlow = () => {


    const [nodes, setNodes, onNodesChange] = useNodesState([]);
    const [edges, setEdges, onEdgesChange] = useEdgesState([]);
    const [machineID, setMachineId] = useState(1);
    const [QueueID, setQueueId] = useState(1);
   
    const [shape, setShape] = useState("");  // Initialize shape state inside the component
    const [menu, setMenu] = useState(false);  // Initialize as boolean
    const [numberOfProducts, setNumberOfProducts] = useState(0);  // Correct state name
    const [redo, setRedo] = useState(false);  // Use a boolean for redo state




    const handleNumberOfProductsChange = (e) => {
        setNumberOfProducts(e.target.value);
    };

    const onConnect = (params) => {
        const { source, target } = params;

        const sourceNode = nodes.find((node) => node.id === source);
        const targetNode = nodes.find((node) => node.id === target);

        if (!sourceNode || !targetNode) {
            console.error("Source or target node not found.");
            return;
        }

        // Check if source and target nodes are of the same type
        if (sourceNode.type === targetNode.type) {
            alert(`Cannot connect two nodes of the same type: ${sourceNode.type}`);
            return; // Prevent the connection
        }

        // Add the edge if validation passes
        setEdges((eds) => addEdge(params, eds));
    };
    // State for floating node
    const [floatingNode, setFloatingNode] = useState(null);
    const { screenToFlowPosition } = useReactFlow();

    // Mouse move handler
    const handleMouseMove = (event) => {
        if (floatingNode) {
            const canvasPosition = screenToFlowPosition({ x: event.clientX, y: event.clientY });
            setFloatingNode({
                ...floatingNode,
                position: canvasPosition,
            });
        }
    };
    

    // Mouse click handler
    const handleMouseClick = () => {
        if (floatingNode) {
            setNodes((nodes) => [...nodes, floatingNode]);
            setFloatingNode(null);
        }
    };

    // creating machine node
    const handleCreateMachine = (event) => {
        const canvasPosition = screenToFlowPosition({ x: event.clientX, y: event.clientY })
        console.log(canvasPosition)
        let machine = {id : `M${machineID}` , position : canvasPosition , data : {label: `M${machineID}`, background:"grey"},type:"machine"}
        setFloatingNode(machine)
        setMachineId(machineID+1)
    
    };
    // creating queue node
    const handleCreateQueue = (event) => {
        const canvasPosition = screenToFlowPosition({ x: event.clientX, y: event.clientY })
        console.log(canvasPosition)
        console.log(edges)
        const queue = {id : `Q${QueueID}` , position :canvasPosition, data : {label: `Q${QueueID}`, count: 0 },type:"queue"}
        setFloatingNode(queue)
        setQueueId(QueueID+1)
    
    };
    return (
        <div className="flow"  
        onMouseMove={handleMouseMove}
        onClick={handleMouseClick}
        >
             <div className='bar'>
                <button className='icon' onClick={(event) => handleCreateMachine(event)}>
                    <img src={machineicon} alt="machineicon" />
                </button>
                <button className="icon" onClick={(event) => handleCreateQueue(event)}>
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
                nodes={floatingNode ? [...nodes, floatingNode] : nodes}
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

const WrappedSimulationFlow = () => (
    <ReactFlowProvider>
        <SimulationFlow />
    </ReactFlowProvider>
);

export default WrappedSimulationFlow;

