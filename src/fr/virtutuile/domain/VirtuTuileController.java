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
    public Point mousePosition;
    private List<SurfacesControllerObserver> observers;
    private double zoom = 1;
    public Point mousePositionZoom;
    private float previousZoom = 1;
    public Point camPos;
    public Point initCamPos;
    public int speed = 3;
    private int polygonLastId = 0;
    private State state = State.UNKNOWN;

    public VirtuTuileController() {
        surfaces = new ArrayList<Surface>();
        points = new ArrayList<Point>();
        observers = new LinkedList<SurfacesControllerObserver>();
        materials = new ArrayList<Material>();
        mousePosition = new Point(0, 0);
        camPos = new Point(0, 0);
        mousePositionZoom = new Point(0, 0);

        materials.add(new Material());
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
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2, point3, point4));
        Surface surface = new Surface(surfacePoints);
        surface.setMaterial(new Material());
        surface.setPattern(new Pattern());
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

    public double getZoom() {
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
        List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2, point3, point4));
        tmpSurface = new Surface(surfacePoints);
        surfaces.add(tmpSurface);
        notifyObserverForSurfaces();
    }

    public void onClick(Point point) {

    }

    public void onMousePressed(Point point) {
        if (state == State.MOVE) {
            points.add(point);
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
        else if (state == State.SELECTION) {
            boolean findOne = false;
            for (Surface surface : surfaces) {
                if (surface != tmpSurface && surface.isInside(new Point(point))) {
                    surface.setSelected(!surface.isSelected());
                    findOne = true;
                }
            }
            if (findOne == false) {
                for (Surface surface : surfaces) {
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
                point2.x = mousePosition.x;

                Point point3 = surfacePoints.get(2);
                point3.setPos(mousePosition.x, mousePosition.y);

                Point point4 = surfacePoints.get(3);
                point4.y = mousePosition.y;
                notifyObserverForSurfaces();
            }
        } else if (state == State.MOVE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            camPos.x =  (int)(zoom * (pressedPoint.x - mousePosition.x)) + camPos.x;
            camPos.y =  (int)(zoom * (pressedPoint.y - mousePosition.y)) + camPos.y;
            notifyObserverForSurfaces();
        } else if (state == State.MOVE_SURFACE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            for (Surface surface : surfaces) {
                if (surface.isSelected()) {
                    for (Point surfacePoint : surface.getPoints()) {
                        surfacePoint.x = surfacePoint.initX - (pressedPoint.x - point.x);
                        surfacePoint.y = surfacePoint.initY - (pressedPoint.y - point.y);
                    }
                    surface.onMoved();
                    notifyObserverForSurfaces();
                }
            }
        }
    }

    public List<Surface> getSurfaces() {
        return surfaces;
    }

    public void zoomIn() {
        zoom = zoom * 0.9f;
        camPos.x = (int)((mousePosition.x - camPos.x) * -0.1f + camPos.x);
        camPos.y = (int)((mousePosition.y - camPos.y) * -0.1f + camPos.y);
        notifyObserverForSurfaces();
    }

    public void zoomOut() {
        zoom = zoom * 1.1f;
        camPos.x = (int)((mousePosition.x - camPos.x) * 0.1f + camPos.x);
        camPos.y = (int)((mousePosition.y - camPos.y) * 0.1f + camPos.y);
        notifyObserverForSurfaces();
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

    public Point GraphicToCoord(int x, int y) {
        Point dest = new Point(x, y);
        //int distX = camPos.x + (int)((x - camPos.x) / zoom);
        //int distY = camPos.y + (int)((y - camPos.y) / zoom);
        //dest.setPos(distX, distY);
        dest.x = (int)Math.floor(dest.x / zoom);
        dest.y = (int)Math.floor(dest.y / zoom);
        dest.add(camPos);
        return dest;
    }

    public  Point coordToGraphic(int x, int y) {
        Point dest = new Point(x, y);
        dest.less(camPos);
        //int distX = camPos.x + (int)((dest.x) * zoom);
        //int distY = camPos.y + (int)((dest.y) * zoom);
        dest.x = (int)Math.floor(dest.x * zoom);
        dest.y = (int)Math.floor(dest.y * zoom);
        //dest.setPos(distX, distY);
        return dest;
    }
}
