import { Handle } from "reactflow";
function QueueNode({data}) {
    return (
        <div className="queue">
        <p>{data.label}<br/>{data.count||0}</p>
       {data.label!="StartQ"&& <Handle type="target" position="left" id="left" style={{scale:"70%", background: "dodger-blue" }} />}
        {data.label!="EndQ"&&  <Handle type="source" position="right" id="right" style={{scale:"70%", background: "dodger-blue" }} />
 } </div>

    );
}
export default QueueNode;