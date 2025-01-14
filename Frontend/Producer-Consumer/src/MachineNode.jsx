import React from "react";
import { Handle } from "reactflow";
function MachineNode({data}) {
  return (
    <div className="machine" style={{background:(data.background)}} >
      <p>{data.label}</p>
      <Handle type="target" position="left" id="left" className="handle" />
       <Handle type="source" position="right" id="right" className="handle" />
    </div>
  );
}

export default MachineNode;
