import { useEffect, useState } from "react";
import { TextureLoader } from "three";
import GetHeights from "../scripts/GetHeights";


type TestImgProps = {

}

export default function TestImg(props:TestImgProps) {
    const [imageUrl, setImageUrl] = useState('');
    const [tempImageUrl, setTempImageUrl] = useState('');

    useEffect(() => {
        const fetchImage = async () => {
            setTempImageUrl(await GetHeights(13, 2414, 3015));
        };
        fetchImage();       
    }, []);

    useEffect(() => {
        console.log("WORKING");
        const image = new Image();
        image.src = tempImageUrl;

            // Wait for the image to load
            image.onload = () => {
                console.log("loaded");
                image.src = tempImageUrl;
                // Create a canvas element
                const canvas = document.createElement("canvas");
                const ctx = canvas.getContext("2d");

                // Set the canvas dimensions to the same as the image
                canvas.width = image.width;
                canvas.height = image.height;

                console.log(canvas.width);

                // Get ImageData from the image
                const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);

                console.log(imageData.data.length);
                
                // Loop through the pixels and modify the pixel values
                for (let i = 0; i < imageData.data.length; i += 4) {
                    let grey = (imageData.data[i] * 256 + imageData.data[i+1] + imageData.data[i+2] / 256) - 32768;
                    console.log(grey);
                    imageData.data[i] = grey;   // Set the red value
                    imageData.data[i+1] = grey;   // Set the green value
                    imageData.data[i+2] = grey;   // Set the blue value
                    imageData.data[i+3] = 255;   // Set the alpha value
                }

                // Put the modified ImageData back onto the canvas
                ctx.putImageData(imageData, 0, 0);
                // Get the new image URL
                const newImageURL = canvas.toDataURL();
                setImageUrl(newImageURL);
            };

    }, [tempImageUrl]);

    return (
        <img src={imageUrl} />
    );
}