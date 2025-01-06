import { Handle } from "reactflow";
function QueueNode({data}) {
    return (
        <div className="queue">
        <p>{data.label}<br/>{data.count||0}</p>
       {data.label!="StartQ"&& <Handle type="target" position="left" id="left" className="handle" />}
        {data.label!="EndQ"&&  <Handle type="source" position="right" id="right" className="handle" />
 } </div>

    );
}
export default QueueNode;