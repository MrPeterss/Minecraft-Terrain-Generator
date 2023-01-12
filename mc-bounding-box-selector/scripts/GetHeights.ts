import fetch from 'isomorphic-unfetch';

export default async function getHeightMap(zoom: number, tileX: number, tileY: number) : Promise<string> {

    async function getHeights(zoom: number, tileX: number, tileY: number) {
        const response = fetch(`https://tile.nextzen.org/tilezen/terrain/v1/512/terrarium/${zoom}/${tileX}/${tileY}.png?api_key=***REMOVED***`);
        const image = (await response).blob();
        const imageUrl = URL.createObjectURL(await image);
        return await imageUrl;
    }

    if (process.browser) {
        
        var terranium_img_url = await getHeights(zoom, tileX, tileY);

        var tempImg = document.createElement('img');
        tempImg.src = terranium_img_url;

        var canvas = document.createElement('canvas');
        canvas.width = 512;
        canvas.height = 512;

        var canvas_ctx = canvas.getContext('2d')!;

        return new Promise(resolve => {
            tempImg.onload = function(callback) {  
                //draw temp image on canvas
                canvas_ctx.drawImage(tempImg, 0, 0);

                //get image data from canvas
                var imageData = canvas_ctx.getImageData(0, 0, tempImg.width, tempImg.height);
                var data = imageData.data;
                
                for(var i = 0; i < data.length; i += 4) {
                    var grey = (data[i] * 256 + data[i + 1] + data[i + 2] / 256) - 32768;
                    data[i] = grey;
                    data[i + 1] = grey;
                    data[i + 2] = grey;
                }

                canvas_ctx.putImageData(imageData, 0, 0);
                resolve(canvas.toDataURL());
            }
        });
    } else {
        return "";
    }
}