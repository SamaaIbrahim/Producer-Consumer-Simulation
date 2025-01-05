import axios from "axios";
function HandleSimulate(nodes,edges,numberOfProducts){
    const machines = nodes.filter((node)=> node.type === "machine").map((machine)=> ({id : machine.id,inQueuesIds:[],outQueueId:""}))
    const queues = nodes.filter((node)=> node.type === "queue").map((queue)=>({id : queue.id , isStart:queue.id==="StartQ",isEnd:queue.id==="End`queue"}))


    edges.forEach((edge) => {
        const { source, target } = edge;
    
        if (source.startsWith("M") && target.startsWith("Q")) {
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

    const simulationDto = {numberOfProducts : numberOfProducts , machineDtos:machines,assemblyLineDtos:queues}
    console.log(edges)
    console.log(nodes)
    console.log(machines)
    console.log(queues)
    console.log(numberOfProducts)

    async () => {
        
        console.log("hello")
        try{
            const response = await axios.post("http://localhost:8080/simulate/",simulationDto);
            if(response.status === 200){
                console.log("done")
            }

        }catch(error){
            console.log(error.response.data)
        }

    }
  
    
}
export default HandleSimulate;