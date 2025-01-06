
import axios from "axios";
async function Replay(setNodes){  

    setNodes((prevNodes) =>
        prevNodes.map((node) => {
          if (node.type === "machine") {
            return { ...node,data:{...node.data,background:"#808080"} }; 
          } else {
            return { ...node, data: { ...node.data, count: 0 } };
          }
        })
      ); 
        
        try{
            const response = await axios.post(`http://localhost:8080/replay`,null);
            if(response.status === 200){
                console.log("replayed")
            }

        }catch(error){
            console.log(error.response.data)
        }  
    
}
export default Replay;