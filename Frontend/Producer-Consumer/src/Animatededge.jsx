 
import { AnimatedSvgEdge } from "@/components/animated-svg-edge";
const defaultNodes = [
    {
      id: "M1",
      position: { x: 200, y: 200 },
      data: { label: "A" },
    },
    {
      id: "StartQ1",
      position: { x: 400, y: 400 },
      data: { label: "B" },
    },
  ];
const defaultEdges = [
    {
      id: "1->2",
      source: "1",
      target: "2",
      type: "animatedSvgEdge",
      data: {
        duration: 2,
        shape: "package",
        path: "smoothstep",
      },
    } 
  ];
  const edgeTypes = {
    animatedSvgEdge: AnimatedSvgEdge,
  };