package fr.virtutuile.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VirtuTuileController {
    private List<Surface> surfaces;
    private List<Material> materials;
    private List<Point> points;
    private Surface tmpSurface;
    private Point mousePosition;
    private List<SurfacesControllerObserver> observers;
    private float zoom = 0 ;
    private int polygonLastId = 0;

    public VirtuTuileController() {
        surfaces = new ArrayList<Surface>();
        points = new ArrayList<Point>();
        observers = new LinkedList<SurfacesControllerObserver>();
        mousePosition = new Point(0, 0);
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
        Point point1 = points.get(0);
        Point point3 = points.get(1);
        Point point2 = new Point(point3.x, point1.y);
        Point point4 = new Point(point1.x, point3.y);
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2, point3, point4));;
        Surface surface = new Surface(surfacePoints);
        surfaces.add(surface);
        notifyObserverForSurfaces();
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

    public void addTmpSurface() {
        Point point1 = points.get(0);
        Point point2 = new Point(mousePosition.x, point1.y);
        Point point3 = mousePosition;
        Point point4 = new Point(point1.x, mousePosition.y);
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2, point3, point4));;
        tmpSurface = new Surface(surfacePoints);
        surfaces.add(tmpSurface);
        notifyObserverForSurfaces();
    }

    public void onClick(Point point) {
        boolean isSelection = false;
        for (Surface surface : surfaces) {
            if (surface.isInside(point)) {
                isSelection = true;
                surface.setSelected(true);
            } else {
                surface.setSelected(false);
            }
        }
        if (isSelection) {
            notifyObserverForSurfaces();
            return;
        }
        points.add(point);
        if (points.size() == 2) {
            surfaces.remove(tmpSurface);
            addRectangleSurface();
            points.clear();
        } else {
            addTmpSurface();
        }
    }

    public void onMousePressed(Point point) {

    }
    public void onMouseReleased(Point point) {

    }

    public void onMouseMoved(Point point) {
        mousePosition.setPos(point.x, point.y);
        if (points.size() == 1) {
            List<Point> points = tmpSurface.getPoints();
            Point point2 = points.get(1);
            point2.x = mousePosition.x;
            Point point4 = points.get(3);
            point4.y = mousePosition.y;
            notifyObserverForSurfaces();
        }
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
