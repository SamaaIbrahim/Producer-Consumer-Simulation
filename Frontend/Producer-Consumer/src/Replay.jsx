
import axios from "axios";
async function Replay(){       
        try{
            const response = await axios.post(`http://localhost:8080/replay`,null);
            if(response.status === 200){
                console.log("done")
            }

        }catch(error){
            console.log(error.response.data)
        }  
    
}
export default Replay;