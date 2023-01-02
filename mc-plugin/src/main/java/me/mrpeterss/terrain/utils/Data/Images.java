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

        //get all tiles in bbox selection
        for (int i = downLeftTile[0]; i >= upRightTile[0]; i--) {
            ArrayList<int[]> lons = new ArrayList<>();
            for (int u = downLeftTile[1]; u <= upRightTile[1]; u++) {
                lons.add(new int[]{i, u});
            }
            tiles.add(lons);
        }

        //could be backwards???
        images = new BufferedImage[tiles.size()][tiles.get(0).size()];
        getImages(tiles);

    }

    public void getImages(ArrayList<ArrayList<int[]>> tiles) throws IOException {
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = 0; j < tiles.get(i).size(); j++) {
                images[i][j] = getImg(tiles.get(i).get(j));
            }
        }
    }

    public BufferedImage getImg(int[] tile) throws IOException {
        return Utils.getTileImg(tile[0],tile[1], zoom);
    }
}
