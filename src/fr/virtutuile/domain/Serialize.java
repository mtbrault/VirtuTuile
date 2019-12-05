package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Serialize implements java.io.Serializable {
    public ArrayList<Surface> surfaces;
    public ArrayList<Material> materials;
    public List<Point> points;
    public Point camPos;
    public double zoom;

    public Serialize(ArrayList<Surface> surfaces, ArrayList<Material> materials, List<Point> points, Point camPos, double zoom) {
        this.surfaces = surfaces;
        this.materials = materials;
        this.points = points;
        this.camPos = camPos;
        this.zoom = zoom;
    }
}
