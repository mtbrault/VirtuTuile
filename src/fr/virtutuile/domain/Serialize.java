package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Serialize implements java.io.Serializable {
    public ArrayList<ArrayList<Surface>>    history;
    public int                              historyIndex;
    public ArrayList<Surface>               surfaces;
    public ArrayList<Material>              materials;
    public List<Point>                      points;
    public Point                            camPos;
    public double                           zoom;

    public Serialize(ArrayList<Surface> surfaces, ArrayList<Material> materials, List<Point> points, Point camPos, double zoom, ArrayList<ArrayList<Surface>> history, int index) {
        this.surfaces = surfaces;
        this.materials = materials;
        this.points = points;
        this.camPos = camPos;
        this.zoom = zoom;
        this.history = history;
        this.historyIndex = index;
    }
}
