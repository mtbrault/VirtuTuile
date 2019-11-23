package fr.virtutuile.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.Robot;
import java.util.Iterator;

public class VirtuTuileController {
    private ArrayList<Surface> surfaces;
    private ArrayList<Material> materials;
    private Tile selectedTile;
    private List<Point> points;
    private Surface tmpSurface = null;
    private Surface movingSurface = null;
    public Point mousePosition;
    private List<SurfacesControllerObserver> observers;
    private double zoom = 1;
    public Point mousePositionZoom;
    private float previousZoom = 1;
    public Point camPos;
    public Point initCamPos;
    public int speed = 3;
    private Point canvasPosition;
    private int polygonLastId = 0;
    private State state = State.UNKNOWN;
    private boolean gridSwitch = false;
    private boolean isBeingDragged = false;

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

    public boolean getGridSwitch() {
        return (gridSwitch);
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
        List<Point> tmpSurfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2, point3, point4));
        List<Point> surfacePoints = new ArrayList<Point>();
        int minX = point1.x;
        int minY = point1.y;
        int maxX = point1.x;
        int maxY = point1.y;
        for (Point point : tmpSurfacePoints) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }
        surfacePoints.add(new Point(minX, minY));
        surfacePoints.add(new Point(maxX, minY));
        surfacePoints.add(new Point(maxX, maxY));
        surfacePoints.add(new Point(minX, maxY));
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

    public void switchGrid() {
        gridSwitch = !gridSwitch;
        notifyObserverForSurfaces();
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
                    movingSurface = surface;
                    for (Point surfacePoint : surface.getPoints()) {
                        surfacePoint.initX = surfacePoint.x;
                        surfacePoint.initY =  surfacePoint.y;
                    }
                    isBeingDragged = true;
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
    public boolean handleSurfaceStackable(Surface selectedSurface, ArrayList<Surface> otherSurfaces) {
        List<Point> selectedSurfacePoints = selectedSurface.getPoints();
        for (Surface surface : otherSurfaces) {
            if (surface != selectedSurface) {
                if (selectedSurface.isSurfaceStacked(surface)) {
                    int dist1 = Math.abs(selectedSurfacePoints.get(0).x - surface.getPoints().get(1).x);
                    int dist2 = Math.abs(selectedSurfacePoints.get(1).x - surface.getPoints().get(0).x);
                    int dist3 = Math.abs(selectedSurfacePoints.get(0).y - surface.getPoints().get(2).y);
                    int dist4 = Math.abs(selectedSurfacePoints.get(2).y - surface.getPoints().get(0).y);
                    int minXDist = dist1 < dist2 ? dist1 : dist2 * -1;
                    int minYDist = dist3 < dist4 ? dist3 : dist4 * -1;
                    int minDist = Math.abs(minXDist) < Math.abs(minYDist) ? minXDist : minYDist;
                    if (minDist != 0) {
                        if (minDist == minXDist)
                            selectedSurface.move(new Point(minDist,0));
                        else
                            selectedSurface.move(new Point(0,minDist));
                        selectedSurface.onMoved();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void onMouseReleased(Point point) {
        if (state == State.MOVE) {
            points.clear();
        } else if (state == State.MOVE_SURFACE) {
            isBeingDragged = false;
            movingSurface = null;
            points.clear();
            for (Surface surface : surfaces) {
                if (surface.isSelected()) {
                    handleSurfaceStackable(surface, surfaces);
                    notifyObserverForSurfaces();
                }
            }
        }
    }

    public void combineSelectedSurfaces() {
        for (Surface firstSurface : surfaces) {
            for (Surface secondSurface : surfaces) {
                if (firstSurface != secondSurface) {
                    if (firstSurface.isSurfaceStacked(secondSurface)) {
                        firstSurface.getIntersectionsPointWithSurface(secondSurface);
                    }
                }
            }
        }
    }

    public Point gridAttractMouse(Point before) {
        Point after = new Point(before);
        System.out.println(after.x + "/" + after.y);
        if (before.x % 100 < 4)
            after.x -= after.x % 100;
        else if (before.x % 100 >= 96)
            after.x += 100 - after.x % 100;
        if (before.y % 100 < 4)
            after.y -= after.y % 100;
        else if (before.y % 100 >= 96)
            after.y += 100 - after.y % 100;
        return (after);
    }

    public Point gridAttractSurface(Point before) {
        Point after = new Point(before);
        Point vector = new Point(0, 0);
        for (Point surfacePoint : movingSurface.getPoints()) {
            Point compare = coordToGraphic(surfacePoint.x, surfacePoint.y);
            if (compare.x % 100 < 4 && vector.x >= 0)
                vector.x -= compare.x % 100;
            else if (compare.x % 100 >= 96 && vector.x <= 0)
                vector.x += 100 - compare.x % 100;
            if (compare.y % 100 < 4 && vector.y >= 0)
                vector.y -= compare.y % 100;
            else if (compare.y % 100 >= 96 && vector.y <= 0)
                vector.y += 100 - compare.y % 100;
        }
        after.x += vector.x;
        after.y += vector.y;
        return (after);
    }

    public Point gridMagnet(Point before) {
        Point after = isBeingDragged && movingSurface.isInside(graphicToCoord(before.x, before.y)) ?
                gridAttractSurface(before) : gridAttractMouse(before);
        if (!before.x.equals(after.x) || !before.y.equals(after.y)) {
            try {
                    Robot robot = new Robot();
                    robot.mouseMove(after.x + this.canvasPosition.x, after.y + this.canvasPosition.y);

            } catch (AWTException e) {
                System.out.println("Error initialazing the mouse Robot for the magnetic grid.");
            }
        }
        return graphicToCoord(after.x, after.y);
    }

    private void setSelectedTile(Tile tile) {
        selectedTile = tile;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void onMouseMoved(Point point) {
        if (gridSwitch)
            point = gridMagnet(coordToGraphic(point.x, point.y));
        mousePosition.setPos(point.x, point.y);
        for (Surface surface : surfaces) {
            for(Tile tile : surface.getTiles()) {
                if (tile.isInside(point)) {
                    setSelectedTile(tile);
                    tile.setSelected(true);
                } else {
                    tile.setSelected(false);
                }
            }
        }
        if (state == State.CREATE_RECTANGULAR_SURFACE) {
            if (points.size() == 1) {
                List<Point> surfacePoints = tmpSurface.getPoints();

                Point point2 = surfacePoints.get(1);
                point2.x = mousePosition.x;

                Point point3 = surfacePoints.get(2);
                point3.setPos(mousePosition.x, mousePosition.y);

                Point point4 = surfacePoints.get(3);
                point4.y = mousePosition.y;
            }
        } else if (state == State.MOVE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            camPos.x =  (int)(zoom * (pressedPoint.x - mousePosition.x)) + camPos.x;
            camPos.y =  (int)(zoom * (pressedPoint.y - mousePosition.y)) + camPos.y;
        } else if (state == State.MOVE_SURFACE && points.size() > 0) {
            Point pressedPoint = points.get(0);
            for (Point surfacePoint : movingSurface.getPoints()) {
                surfacePoint.x = surfacePoint.initX - (pressedPoint.x - point.x);
                surfacePoint.y = surfacePoint.initY - (pressedPoint.y - point.y);
            }
            movingSurface.onMoved();
        }
        notifyObserverForSurfaces();
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

    public Point graphicToCoord(int x, int y) {
        Point dest = new Point(x, y);
        dest.x = (int)Math.round(dest.x / zoom);
        dest.y = (int)Math.round(dest.y / zoom);
        dest.add(camPos);
        return dest;
    }

    public  Point coordToGraphic(int x, int y) {
        Point dest = new Point(x, y);
        System.out.println("IN: " + dest.x + ";" + dest.y);
        System.out.println("Zoom is " + zoom);
        dest.less(camPos);
        dest.x = (int)Math.round(dest.x * zoom);
        dest.y = (int)Math.round(dest.y * zoom);
        System.out.println("OUT: " + dest.x + ";" + dest.y);
        return dest;
    }
}
