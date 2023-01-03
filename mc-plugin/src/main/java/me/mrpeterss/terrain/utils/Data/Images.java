package me.mrpeterss.terrain.utils.Data;

import me.mrpeterss.terrain.utils.Data.Link;
import me.mrpeterss.terrain.utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Images {

    public BufferedImage[][] images;

    private int zoom;

    public Images(Link data) throws IOException {

        zoom = data.selectionZoomLvl;

        int[] downLeftTile = Utils.deg2tile(data.selectionBBox.south, data.selectionBBox.west, data.selectionZoomLvl);
        int[] upRightTile = Utils.deg2tile(data.selectionBBox.north, data.selectionBBox.east, data.selectionZoomLvl);

        ArrayList<ArrayList<int[]>> tiles = new ArrayList<>();

        System.out.println("Left x: " + downLeftTile[0]);
        System.out.println("Right x: " + upRightTile[0]);

        System.out.println("Down y: " + downLeftTile[1]);
        System.out.println("Up y: " + upRightTile[1]);

        //get all tiles in bbox selection
        for (int y = downLeftTile[1]; y >= upRightTile[1]; y--) {
            ArrayList<int[]> lons = new ArrayList<>();
            for (int x = downLeftTile[0]; x <= upRightTile[0]; x++) {
                System.out.println("Tile: " + x + ", " + y);
                lons.add(new int[]{x, y});
            }
            tiles.add(lons);
        }
        System.out.println("Total Tiles: " + tiles.size()*tiles.get(0).size());

        //could be backwards???
        images = new BufferedImage[tiles.get(0).size()][tiles.size()];
        getImages(tiles);

    }

    public void getImages(ArrayList<ArrayList<int[]>> tiles) throws IOException {
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = 0; j < tiles.get(i).size(); j++) {
                images[j][i] = getImg(tiles.get(i).get(j));
            }
        }
    }

    public BufferedImage getImg(int[] tile) throws IOException {
        return Utils.getTileImg(tile[0],tile[1], zoom);
    }
}
