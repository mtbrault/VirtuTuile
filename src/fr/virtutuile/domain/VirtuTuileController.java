package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VirtuTuileController {
    private List<Surface> surfaces;
    private List<Material> materials;
    private List<Point> points;
    private List<SurfacesControllerObserver> observers;
    private float zoom = 0 ;
    private int polygonLastId = 0;

    public VirtuTuileController() {
        surfaces = new ArrayList<Surface>();
        observers = new LinkedList<SurfacesControllerObserver>();
    }

    public void addSurface(Surface surface) {
        surfaces.add(surface);
        notifyObserverForSurfaces();
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }


    public void addRectangleSurface() {

    }

    public float getZoom() {
        return zoom;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public void onClick(Point point) {
    }

    public void onMousePressed(Point point) {

    }
    public void onMouseReleased(Point point) {

    }

    public List<Surface> getSurfaces() {
        return surfaces;
    }

    public void mouseUp() {

    }

    public void mouseDown() {

    }

    public void save() {

    }

    public void deletedSelected() {

    }

    public void deleteZone(List<Point> points) {

    }

    public void move() {

    }

    public void registerObserver(SurfacesControllerObserver newListener) {
        observers.add(newListener);
    }

    public void unregisterObserver(SurfacesControllerObserver listener) {
        observers.remove(listener);
    }

    public void notifyObserverForSurfaces() {
        for (SurfacesControllerObserver observer : observers) {
            observer.notifyCreatedSurface();
        }
    }

    public  Point convertPoint(int x, int y) {
        return new Point(x, y);
    }
}
