package me.mrpeterss.terrain;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Images {

    BufferedImage[][] images;

    public Images(Link data) {
        int[] upLeftTile = deg2tile(data.selectionBBox.north, data.selectionBBox.east, data.selectionZoomLvl);
        int[] downRightTile = deg2tile(data.selectionBBox.south, data.selectionBBox.west, data.selectionZoomLvl);

        ArrayList<int[]> tiles = new ArrayList<>();

        //get all tiles in bbox selection
        for (int i = upLeftTile[0]; i <= downRightTile[1]; i++) {
            for (int u = upLeftTile[1]; u >= downRightTile[0]; u--) {
                tiles.add(new int[]{i,u});
            }
        }
    }

    public int[] deg2tile(double lat_deg, double lon_deg, int zoom) {
        double lat_rad = Math.toRadians(lat_deg);
        double n = Math.pow(2, zoom);
        int xtile = (int) ((lon_deg + 180.0) / 360.0 * n);
        int ytile = (int) ((1.0 - Math.log(Math.tan(lat_rad) + 1.0 / Math.cos(lat_rad)) / Math.PI) / 2.0 * n);
        return new int[]{xtile, ytile};
    }
}
