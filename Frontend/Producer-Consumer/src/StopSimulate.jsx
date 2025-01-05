
import axios from "axios";
async function Simulate(){       
        try{
            const response = await axios.post(`http://localhost:8080/stop`,null);
            if(response.status === 200){
                console.log("done")
            }

        }catch(error){
            console.log(error.response.data)
        }  
    
}
export default Simulate; 