package fr.virtutuile.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class VirtuTuileController {
    private List<Surface> surfaces;
    private List<Material> materials;
    private List<Point> points;
    private Surface tmpSurface;
    private Point mousePosition;
    private List<SurfacesControllerObserver> observers;
    private float zoom = 0;
    public Point camPos;
    public Point initCamPos;
    public int speed = 3;
    private int polygonLastId = 0;
    private State state = State.UNKNOWN;

    public VirtuTuileController() {
        surfaces = new ArrayList<Surface>();
        points = new ArrayList<Point>();
        observers = new LinkedList<SurfacesControllerObserver>();
        mousePosition = new Point(0, 0);
        camPos = new Point(0, 0);
    }

    public void addSurface(Surface surface) {
        surfaces.add(surface);
        notifyObserverForSurfaces();
    }

    public void setState(State newState) {
        state = newState;
    }

    public State getState() {
        return state;
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
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1.add(camPos), point2.add(camPos), point3.add(camPos), point4.add(camPos)));
        Surface surface = new Surface(surfacePoints);
        surfaces.add(surface);
        notifyObserverForSurfaces();
    }

    public void deleteSurface() {
        for (Iterator<Surface> iter = surfaces.listIterator(); iter.hasNext();) {
            Surface surface = iter.next();
            if (surface.isSelected())
                iter.remove();
        }
        notifyObserverForSurfaces();
    }

    public float getZoom() {
        return zoom;
    }

    public Point getCamPos() {
        return camPos;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public void addTmpSurface() {
        Point point1 = new Point(points.get(0));
        Point point2 = new Point(mousePosition.x, point1.y);
        Point point3 = new Point(mousePosition);
        Point point4 = new Point(point1.x, mousePosition.y);
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1.add(camPos), point2.add(camPos), point3.add(camPos), point4.add(camPos)));
        tmpSurface = new Surface(surfacePoints);
        surfaces.add(tmpSurface);
        notifyObserverForSurfaces();
    }

    public void onClick(Point point) {
        if (state == State.SELECTION) {
            for (Surface surface : surfaces) {
                if (surface != tmpSurface && surface.isInside(new Point(point).add(camPos))) {
                    surface.setSelected(true);
                } else {
                    surface.setSelected(false);
                }
            }
            notifyObserverForSurfaces();
        } else if (state == State.CREATE_RECTANGULAR_SURFACE) {
            points.add(point);
            if (points.size() == 2) {
                surfaces.remove(tmpSurface);
                addRectangleSurface();
                points.clear();
            } else {
                addTmpSurface();
            }
        }
    }

    public void onMousePressed(Point point) {
        if (state == State.MOVE) {
            points.add(point);
            initCamPos = new Point(camPos);
            System.out.println("Pressed");
        } else if (state == State.MOVE_SURFACE) {
            points.add(point);
            for (Surface surface : surfaces) {
                if (surface.isInside(point)) {
                    surface.setSelected(true);
                    for (Point surfacePoint : surface.getPoints()) {
                        surfacePoint.initX = surfacePoint.x;
                        surfacePoint.initY =  surfacePoint.y;
                    }
                } else {
                    surface.setSelected(false);
                }
            }
            notifyObserverForSurfaces();
        }
    }
    public void onMouseReleased(Point point) {
        if (state == State.MOVE) {
            points.clear();
        } else if (state == State.MOVE_SURFACE) {
            points.clear();
        }
    }

    public void onMouseMoved(Point point) {
        mousePosition.setPos(point.x, point.y);
        if (state == State.CREATE_RECTANGULAR_SURFACE) {
            if (points.size() == 1) {
                List<Point> surfacePoints = tmpSurface.getPoints();

                Point point2 = surfacePoints.get(1);
                point2.x = mousePosition.x + camPos.x;

                Point point3 = surfacePoints.get(2);
                point3.setPos(mousePosition.x + camPos.x, mousePosition.y + camPos.y);

                Point point4 = surfacePoints.get(3);
                point4.y = mousePosition.y + camPos.y;
                notifyObserverForSurfaces();
            }
        } else if (state == State.MOVE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            camPos.x =  initCamPos.x + (pressedPoint.x - point.x);
            camPos.y =  initCamPos.y + (pressedPoint.y - point.y);
            notifyObserverForSurfaces();
        } else if (state == State.MOVE_SURFACE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            for (Surface surface : surfaces) {
                if (surface.isSelected()) {
                    for (Point surfacePoint : surface.getPoints()) {
                        surfacePoint.x = surfacePoint.initX - (pressedPoint.x - point.x);
                        surfacePoint.y = surfacePoint.initY - (pressedPoint.y - point.y);
                    }
                    notifyObserverForSurfaces();
                }
            }
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
