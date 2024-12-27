import React from "react";
import ReactFlow, {
    addEdge,
    useEdgesState,
    useNodesState,
    Background,
    Controls,
} from "reactflow";
import "reactflow/dist/style.css";
import MachineNode from "./MachineNode";

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

    const onConnect = (params) => setEdges((eds) => addEdge(params, eds));

    return (
        <div className="flow">
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

