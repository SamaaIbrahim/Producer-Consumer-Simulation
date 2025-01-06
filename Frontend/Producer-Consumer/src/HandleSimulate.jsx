import axios from "axios";
async function HandleSimulate(nodes,edges,numberOfProducts,setNodes){
    const machines = nodes.filter((node)=> node.type === "machine").map((machine)=> ({id : machine.id,inQueuesIds:[],outQueueId:""}))
    const queues = nodes.filter((node)=> node.type === "queue").map((queue)=>({id : queue.id , isStart:queue.id==="StartQ",isEnd:queue.id==="EndQ"}))
    edges.forEach((edge) => {
        const { source, target } = edge;
    
        if (source.startsWith("M") && (target.startsWith("Q") || target.startsWith("EndQ")) ) {
            const machine = machines.find((m) => m.id === source);
            if (machine) {
                machine.outQueueId = target;
            }
        } else  {
            const machine = machines.find((m) => m.id === target);
            if (machine) {
                machine.inQueuesIds.push(source);
            }
        }
    });
    setNodes((prevNodes) =>
        prevNodes.map((node) => {
          if (node.type === "machine") {
            return { ...node,data:{...node.data,background:"#808080"} }; 
          } else {
            if (node.id === "StartQ") {
            } else if (node.id === "EndQ") {
            }
            return { ...node, data: { ...node.data, count: 0 } };
          }
        })
      );

    

    const simulationDto = {numberOfProducts : numberOfProducts , machineDtos:machines,assemblyLineDtos:queues}
    console.log(edges)
    console.log(nodes)
    console.log(machines)
    console.log(queues)
    console.log(numberOfProducts)


       
        try{
            const response = await axios.post(`http://localhost:8080/simulate`,simulationDto);
            if(response.status === 200){
                console.log("done");
            }

        }catch(error){
            alert(error.response.data);
        }  
    
}
export default HandleSimulate;