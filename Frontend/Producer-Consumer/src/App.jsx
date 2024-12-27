import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css' 
import SimulationFlow from './Flow'
import Machine from './MachineNode'
import machineicon from './assets/machine.svg'
function App() {
  const [shape, setshape] = useState("");
 
  return (
    <div>
    <SimulationFlow>
       
    </SimulationFlow>
    </div>
  )
}

export default App
