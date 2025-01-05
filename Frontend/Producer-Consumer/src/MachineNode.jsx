import React from "react";
import { Handle } from "reactflow";
function MachineNode({data}) {
  return (
    <div className="machine" style={{background:(data.background)}} >
      <p>{data.label}</p>
      {<Handle type="target" position="left" id="left" style={{ scale:"70%",background: "dodger-blue" }} />
       }{data.label!="EndQ"&& <Handle type="source" position="right" id="right" style={{scale:"70%", background: "dodger-blue" }} />}
    </div>
  );
}

export default MachineNode;
