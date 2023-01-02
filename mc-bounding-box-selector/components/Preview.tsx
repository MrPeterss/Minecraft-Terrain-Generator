import { useRef, useState, Suspense } from 'react'
import { Canvas, useFrame, useLoader } from '@react-three/fiber'
import GrassBlock from './GrassBlock';
import { PresentationControls } from '@react-three/drei';

type PreviewProps = {
    className?: string;
}

export default function Preview (props: PreviewProps) {
  // This reference gives us direct access to the THREE.Mesh object.
  const ref = useRef()  

  const [scale, setScale] = useState(0.4);

  // Return the view.
  // These are regular three.js elements expressed in JSX.
    //onWheel={(e) => onWheel(e)}
    
  return (
    <Canvas >
        <ambientLight intensity={0.5} />      
        <spotLight position={[10, 10, 10]} angle={0.15} penumbra={1} />      
        <pointLight position={[-10, -10, -10]} />
        <Suspense fallback={null}>
            <PresentationControls
                enabled={true} // the controls can be disabled by setting this to false
                global={true} // Spin globally or by dragging the model
                cursor={true} // Whether to toggle cursor style on drag
                snap={false} // Snap-back to center (can also be a spring config)
                speed={2} // Speed factor
                zoom={1} // Zoom factor when half the polar-max is reached
                rotation={[0, Math.PI/4, 0]} // Default rotation
                polar={[Math.PI/6, Math.PI/6]} // Vertical limits
                azimuth={[-Infinity, Infinity]} // Horizontal limits
                config={{ mass: 1, tension: 170, friction: 20 }} // Spring config
            >
                
                <GrassBlock x={0} y={0} z={0} scale={scale}/>
                <GrassBlock x={1} y={0} z={0} scale={scale}/>
                <GrassBlock x={1} y={0} z={1} scale={scale}/>
            </PresentationControls>
        </Suspense>
    </Canvas>
  )
}