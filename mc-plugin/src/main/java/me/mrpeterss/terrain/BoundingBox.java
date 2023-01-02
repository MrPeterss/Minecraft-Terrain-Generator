package me.mrpeterss.terrain;

public class BoundingBox {

    public float east;
    public float south;
    public float west;
    public float north;


    public BoundingBox(float east, float south, float west, float north) {
        this.east = east;
        this.south = south;
        this.west = west;
        this.north = north;
    }
}
