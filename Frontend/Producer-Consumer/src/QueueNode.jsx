import { Handle } from "reactflow";
function QueueNode({data}) {
    return (
        <div className="queue">
        <p>{data.label}<br/>{data.count}</p>
        <Handle type="target" position="left" id="left" style={{ background: "dodger-blue" }} />
          <Handle type="source" position="right" id="right" style={{ background: "dodger-blue" }} />
      </div>

    );
}
export default QueueNode;