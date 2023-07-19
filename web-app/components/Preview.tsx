import { useRef, useState, Suspense } from 'react'
import { Canvas, useFrame, useLoader } from '@react-three/fiber'
import PreviewMesh from './PreviewMesh';
import { OrbitControls, PresentationControls, useTexture } from '@react-three/drei';
import { PlaneGeometry, Vector3 } from 'three';

type PreviewProps = {
    className?: string; 
}

export default function Preview (props: PreviewProps) {
  // This reference gives us direct access to the THREE.Mesh object.
  const ref = useRef()  

  const [scale, setScale] = useState(0.2);

  // Return the view.
  // These are regular three.js elements expressed in JSX.
  //onWheel={(e) => onWheel(e)}

  var heights:number[][]=[ 
    [1, 1, 1, 1, 1, 1, 5],
    [1, 2, 1, 1, 1, 1, 5]
  ]

  // function getSubdivisionsX(arr:number[][]):number {
  //   var subdivisions = 0;
  //   for (var x = 0; x < arr.length; x++) {
  //     for (var z = 0; z < arr[x].length; z++) {     
  //       if (x==0 || x==arr.length-1 || arr[x][z] != arr[x-1][z]) {
  //         subdivisions+=2;
  //       } else if (arr[x][z] == arr[x-1][z] && arr[x][z] != arr[x+1][z]) {
  //         subdivisions++;
  //       } 
  //     }
  //   }
  //   console.log("subx", subdivisions);
  //   return subdivisions;
  // }

  // function getSubdivisionsZ(arr:number[][]):number {
  //   var subdivisions:number = 0;
  //   for (var x = 0; x < arr.length; x++) {
  //     for (var z = 0; z < arr[x].length; z++) {     
  //       if (z==0 || z==arr[x].length-1 || arr[x][z] != arr[x][z-1]) {
  //         subdivisions+=2;
  //       } else if (arr[x][z] == arr[x][z-1] && arr[x][z] != arr[x][z+1]) {
  //         subdivisions++;
  //       } 
  //     }
  //   }
  //   console.log("subz", subdivisions);
  //   return subdivisions;
  // }
  

    
  
  return (
    <Canvas >
        <ambientLight intensity={0.8} />     
        <Suspense fallback={null}>
          <OrbitControls minDistance={50} maxDistance={100}/>
          <PreviewMesh x={0} y={0} z={0} scale={scale} cubes={256} subdivisionsX={32} subdivisionsZ={32} />
        </Suspense>
    </Canvas>
  )
}