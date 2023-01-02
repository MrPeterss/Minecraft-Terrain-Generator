package me.mrpeterss.terrain.utils.Data;

public class BoundingBox {

    public double east;
    public double south;
    public double west;
    public double north;


    public BoundingBox(double east, double south, double west, double north) {
        this.east = east;
        this.south = south;
        this.west = west;
        this.north = north;
    }
}
