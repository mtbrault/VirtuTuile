package fr.virtutuile.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.Robot;

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
    private Point canvasPosition;
    private int polygonLastId = 0;
    private State state = State.UNKNOWN;
    private boolean gridSwitch = false;

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

    public void setCanvasPosition(Point p) {
        this.canvasPosition = p;
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
        }
    }
    public void onMouseReleased(Point point) {
        if (state == State.MOVE) {
            points.clear();
        }
    }

    public void onMouseMoved(Point point) {
        System.out.println("Move " + point.x + "/" + point.y);
        try {
            boolean moved = false;
            Robot robot = new Robot();
            if (point.x % 100 < 5) {
                point.x -= point.x % 100;
                moved = true;
            } else if (point.x % 100 >= 95) {
                point.x += 100 - point.x % 100;
                moved = true;
            }
            if (point.y % 100 < 5) {
                point.y -= point.y % 100;
                moved = true;
            } else if (point.y % 100 >= 95) {
                point.y += 100 - point.y % 100;
                moved = true;
            }
            if (moved)
                robot.mouseMove(point.x + this.canvasPosition.x, point.y + this.canvasPosition.y);
            //System.out.println("Moving " + point.x + "/" + point.y + "+" + this.canvasPosition.x + "/" + this.canvasPosition.y);
        } catch (AWTException e) {
            System.out.println("Error");
        }
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
