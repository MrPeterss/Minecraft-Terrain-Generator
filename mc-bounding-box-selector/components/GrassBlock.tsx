import { useTexture } from '@react-three/drei';
import { useRef, useState } from 'react';

type GrassBlockProps = {
    x: number;
    y: number;
    z: number;
    scale: number;
}

export default function GrassBlock(props:GrassBlockProps) {

    const scale = props.scale;

    // Hold state for hovered and clicked events.
    const [isDown, down] = useState(false);

    // Define Textures
    const top = useTexture("/grass/" + "top" + ".jpg");
    const side = useTexture("/grass/" + "side" + ".jpg");
    const bottom = useTexture("/grass/" + "bottom" + ".jpg");
    const mesh = useRef();


    return (
                <mesh
                position={[props.x*scale, props.y*scale, props.z*scale]}
                onPointerDown={(event) => down(true)}
                onPointerUp={(event) => down(false)}
                > 
                    <boxBufferGeometry attach="geometry" args={[scale, scale, scale]} />
                    <meshStandardMaterial attach="material-0" map={side} />
                    <meshStandardMaterial attach="material-1" map={side} />
                    <meshStandardMaterial attach="material-2" map={top} />
                    <meshStandardMaterial attach="material-3" />
                    <meshStandardMaterial attach="material-4" map={side} />
                    <meshStandardMaterial attach="material-5" map={side} />
                </mesh>
    );
}