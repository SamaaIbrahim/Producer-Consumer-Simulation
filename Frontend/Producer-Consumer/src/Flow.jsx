import React, { useState, useEffect } from "react";
import "./App.css";
import ReactFlow, {
  addEdge,
  useEdgesState,
  useNodesState,
  Background,
  Controls,
  useReactFlow,
  ReactFlowProvider,
} from "reactflow";
import { Client } from "@stomp/stompjs";
import "reactflow/dist/style.css";
import MachineNode from "./MachineNode";
import QueueNode from "./QueueNode";
import queueicon from "./assets/queue.svg";
import counter from "./assets/counter.svg";
import redoicon from "./assets/redo.svg";
import newsim from "./assets/play.svg";
import machineicon from "./assets/machine.svg";
import deleteicon from "./assets/trash (1).svg";
import HandleSimulate from "./HandleSimulate";
import stopicon from './assets/stop.svg';
import Replay from "./Replay";
import Simulate from "./StopSimulate";

const nodeTypes = { machine: MachineNode, queue: QueueNode };

const SimulationFlow = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [machineID, setMachineId] = useState(1);
  const [QueueID, setQueueId] = useState(1);

  const [queuemenu, setqueuemenu] = useState(false); // Initialize shape state inside the component
  const [menu, setMenu] = useState(false); // Initialize as boolean
  const [numberOfProducts, setNumberOfProducts] = useState(0); // Correct state name

  useEffect(() => {
    const client = new Client({
      brokerURL: "ws://localhost:8080/ws",
      reconnectDelay: 1000,
      onConnect: () => {
        console.log("Connected to WebSocket");
        client.subscribe(`/Simulate/machine`, (message) => {
          const data = JSON.parse(message.body);
          console.log(data)
          setNodes((prevNodes) =>
            prevNodes.map((node) =>
              node.id === data.id
                ? { ...node, data: { ...node.data, background:"#"+ data.color } }
                : node
            )
          );
        });
        client.subscribe(`/Simulate/transfer`, (message) => {
          const data = JSON.parse(message.body);
          console.log(data);
          setEdges((prevEdges) =>
            prevEdges.map((edge) =>
              edge.source === data.from && edge.target === data.to
                ? {
                    ...edge,
                    markerEnd: { ...edge.markerEnd, color: "#" + data.color },
                    style: { ...edge.style, stroke: "#" + data.color }
                  }
                : edge
            )
          );
        
          // Set timeout to reset color after 1 second
          setTimeout(() => {
            setEdges((prevEdges) =>
              prevEdges.map((edge) =>
                edge.source === data.from && edge.target === data.to
                  ? {
                      ...edge,
                      markerEnd: { ...edge.markerEnd, color: "#808080" },
                      style: { ...edge.style, stroke: "#808080" }
                    }
                  : edge
              )
            );
          }, 1500); // 1000 ms = 1 second
        });
        
        client.subscribe(`/Simulate/queue`, (message) => {
          const data = JSON.parse(message.body);
          console.log(data)
          setNodes((prevNodes) =>
            prevNodes.map((node) =>
              node.id === data.id
                ? { ...node, data: { ...node.data, count: data.size } }
                : node
            )
          );
        });
      },
      onDisconnect: () => {
        console.log("Disconnected from WebSocket");
      },
      onStompError: (error) => {
        console.error("STOMP error:", error);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  const deleteall = () => {
    setNodes([]);
    setEdges([]);
    setMachineId(1);
    setQueueId(1); 
    Simulate();
  };

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
    const sourceedges = edges.filter((edge) => edge.source === source);
  if(sourceNode.type==="machine"&& sourceedges.length===1 ){
    alert(`Cannot connect a machine with two nodes`);
    return; 
 
  }
    // Check if source and target nodes are of the same type
    if (sourceNode.type === targetNode.type) {
      alert(`Cannot connect two nodes of the same type: ${sourceNode.type}`);
      return; // Prevent the connection
    }
  
    const animatedEdge = {
      ...params,
      style: {
        stroke: "#808080",
        strokeWidth: 2,
        strokeDasharray: "10 5", // Dashed pattern
        strokeDashoffset: 0,    // Initial offset
        animation: "dash-move 5s linear infinite", // Apply animation
      },
      markerEnd: { type: "arrowclosed", color: "#808080" },
    };
      setEdges((eds) => addEdge(animatedEdge, eds));
    
  };
  // State for floating node
  const [floatingNode, setFloatingNode] = useState(null);
  const { screenToFlowPosition } = useReactFlow();

  // Mouse move handler
  const handleMouseMove = (event) => {
    if (floatingNode) {
      const canvasPosition = screenToFlowPosition({
        x: event.clientX,
        y: event.clientY,
      });
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
    const canvasPosition = screenToFlowPosition({
      x: event.clientX,
      y: event.clientY,
    });
    console.log(canvasPosition);
    let machine = {
      id: `M${machineID}`,
      position: canvasPosition,
      data: { label: `M${machineID}` },
      type: "machine",
    };
    setFloatingNode(machine);
    setMachineId(machineID + 1);
    console.log(edges);
  };
  // creating queue node
  const handleCreateQueue = (event, type) => {
    const canvasPosition = screenToFlowPosition({
      x: event.clientX,
      y: event.clientY,
    });
    let queue = null;
    if (type === "start") {
      queue = {
        id: `StartQ`,
        position: canvasPosition,
        data: { label: `StartQ`, count: 0 },
        type: "queue",
      };
      const node = nodes.find((node)=> node.id === queue.id)
      if(node){
        alert(`Cannot have multible start`);
        return
      }
      else{
        setFloatingNode(queue)
      }
    } else if (type === "end") {
      queue = {
        id: `EndQ`,
        position: canvasPosition,
        data: { label: `EndQ`, count: 0 },
        type: "queue",
      };
      const node = nodes.find((node)=> node.id === queue.id)
      if(node){
        alert(`Cannot have multible end`);
        return
      }
      else{
        setFloatingNode(queue)
      }
    } else {
       queue = {
        id: `Q${QueueID}`,
        position: canvasPosition,
        data: { label: `Q${QueueID}`, count: 0 },
        type: "queue",
      };
      setFloatingNode(queue);
      setQueueId(QueueID + 1);
    }
    
  };
  return (
    <div
      className="flow"
      onMouseMove={handleMouseMove}
      onClick={handleMouseClick}
    >
      <div className="bar">
        <button
          className="icon"
          onClick={(event) => { setqueuemenu(false);setMenu(false);handleCreateMachine(event)}}
        >
          <img src={machineicon} alt="machineicon" />
        </button>

        <button
          className="icon"
          onClick={() => {
            setqueuemenu(!queuemenu);
            setMenu(false);
          }}
        >
          <img src={queueicon} alt="queue" />
        </button>
        {queuemenu && (
          <menu className="menudisplay">
            <button
              className="settype"
              onClick={(event) => {
                setqueuemenu(false);
                handleCreateQueue(event, "start");
              }}
            >
              Start
            </button>
            <button
              className="settype"
              onClick={(event) => {
                setqueuemenu(false);
                handleCreateQueue(event);
              }}
            >
              Normal
            </button>
            <button
              className="settype"
              onClick={(event) => {
                setqueuemenu(false);
                handleCreateQueue(event, "end");
              }}
            >
              End
            </button>
          </menu>
        )}

        <button
          className="icon"
          onClick={() => {
            setMenu(!menu);
            setqueuemenu(false);
          }}
        >
          <img src={counter} alt="counter" />
        </button>
        {/* Conditional rendering of the menu */}
        {menu && (
          <div className="inputmenu">
            <input
              id="products"
              type="number" // Changed to number input for product count
              value={numberOfProducts}
              onChange={handleNumberOfProductsChange}
              style={{
                background: "white",
                opacity: 0.7, // Set opacity to make input visible
                position: "absolute",
                width: "50%",
                zIndex: "10px",
                padding: "10px",
                pointerEvents: "auto",
                color: "black",
                border: "black",
              }}
            />
          </div>
        )}
        <button
          className="icon"
          onClick={() => {
            setMenu(false);
            setqueuemenu(false);
            HandleSimulate(nodes, edges, numberOfProducts,setNodes);
          }}
        >
          <img src={newsim} alt="new" />
        </button>
        <button
          className="icon"
          onClick={() => {
            setMenu(false);
            setqueuemenu(false);
            Replay(setNodes);
          }}
        >
          <img src={redoicon} alt="redo" />
        </button>
        <button className="icon"onClick={Simulate}>
        <img src={stopicon} alt="stop" />
        </button>
        <button className="icon" onClick={deleteall}>
          <img src={deleteicon} alt="delete" />
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
