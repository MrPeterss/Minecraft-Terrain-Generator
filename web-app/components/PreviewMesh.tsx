import { Plane, useTexture } from '@react-three/drei';
import { useEffect, useRef, useState } from 'react';
import { BufferAttribute, BufferGeometry, InstancedMesh, LinearMipMapLinearFilter, MeshPhongMaterial, NearestFilter, RepeatWrapping, TextureLoader } from 'three';
import { Float32BufferAttribute, PlaneGeometry } from 'three/src/Three';
import getHeightMap from '../scripts/GetHeights';

type GrassBlockProps = {
    x: number;
    y: number;
    z: number;
    scale: number;
    cubes: number;
    subdivisionsX: number;
    subdivisionsZ: number;
}

export default function PreviewMesh(props:GrassBlockProps) {
    const ref = useRef<InstancedMesh>(null!);
    const matRef = useRef<MeshPhongMaterial>(null!);
    const scale = props.scale;
    const cubes = props.cubes;
    const subdivisionsX = props.subdivisionsX;
    const subdivisionsZ = props.subdivisionsZ;

    const grass = new TextureLoader().load("/grass/" + "top" + ".png");    
    grass.magFilter = NearestFilter;
    grass.minFilter = LinearMipMapLinearFilter;
    grass.repeat.set(cubes,cubes);
    grass.wrapS = grass.wrapT = RepeatWrapping;
    
    useEffect(() => {
        console.log("useeffect run");
        const fetchData = async () => {
            var heightsMap:string = await getHeightMap(13, 2415, 3015);
            var mapTexture = new TextureLoader().load(heightsMap);
            matRef.current.displacementMap = mapTexture;
            matRef.current.displacementScale = 50;
            matRef.current.displacementBias = -15;
            matRef.current.map = grass;
            matRef.current.needsUpdate = true;
        }
        // call the function
        fetchData()
    }, []);

    function scaleHeight(originalNumber: number, minOriginal: number, maxOriginal: number, minScaled: number, maxScaled: number): number {
        return (originalNumber - minOriginal) * (maxScaled - minScaled) / (maxOriginal - minOriginal) + minScaled;
    }

    return ( 
        <mesh ref={ref} position={[props.x*subdivisionsX*scale,props.y,props.z*subdivisionsX*scale]} rotation={[-Math.PI/2,0,0]} > 
            <planeGeometry args={[scale*cubes, scale*cubes,subdivisionsX,subdivisionsZ]} />
            <meshPhongMaterial ref={matRef} />
        </mesh>
    );
}