package me.mrpeterss.terrainGen.utils.Data;

import me.mrpeterss.terrainGen.utils.Utils;

import java.util.ArrayList;

public class Images {


    private int[] downLeftTile;

    private int[] upRightTile;

    public Images(Link data) {

        downLeftTile = Utils.deg2tile(data.selectionBBox.south, data.selectionBBox.west, data.selectionZoomLvl);
        upRightTile = Utils.deg2tile(data.selectionBBox.north, data.selectionBBox.east, data.selectionZoomLvl);

        System.out.println("Left x: " + downLeftTile[0]);
        System.out.println("Right x: " + upRightTile[0]);

        System.out.println("Down y: " + downLeftTile[1]);
        System.out.println("Up y: " + upRightTile[1]);
    }

    public ArrayList<int[]> getTiles() {
        ArrayList<int[]> tileList = new ArrayList<>();
        for (int y = downLeftTile[1]; y >= upRightTile[1]; y--) {
            for (int x = downLeftTile[0]; x <= upRightTile[0]; x++) {
                System.out.println("Tile: " + x + ", " + y);
                tileList.add(new int[]{x, y});
            }
        }
        System.out.println("TileList size: " + tileList.size());
        return tileList;
    }


}
