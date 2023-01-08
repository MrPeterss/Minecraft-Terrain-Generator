import fetch from 'isomorphic-unfetch';


export default async function GetHeights(zoom: number, tileX: number, tileY: number) {
    const response = fetch(`https://tile.nextzen.org/tilezen/terrain/v1/512/terrarium/${zoom}/${tileX}/${tileY}.png?api_key=***REMOVED***`);
    const image = (await response).blob();
    const imageUrl = URL.createObjectURL(await image);
    console.log(await imageUrl);
    return await imageUrl;
}