package fr.virtutuile.domain;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.Robot;
import java.util.Iterator;

public class VirtuTuileController {
    private ArrayList<ArrayList<Surface>> history;
    private int historyIndex;
    private ArrayList<Surface> surfaces;
    private ArrayList<Material> materials;
    private Tile selectedTile;
    private List<Point> points;
    private Surface tmpSurface = null;
    private Surface movingSurface = null;
    private Surface cutSurface = null;
    public Point mousePosition;
    private List<SurfacesControllerObserver> observers;
    private double zoom = 1;
    public Point mousePositionZoom;
    public Point camPos;
    private Point canvasPosition;
    private State state = State.UNKNOWN;
    private boolean gridSwitch = false;
    private boolean isBeingDragged = false;

    public VirtuTuileController() {
        surfaces = new ArrayList<Surface>();
        history = new ArrayList<ArrayList<Surface>>();
        history.add(new ArrayList<Surface>());
        points = new ArrayList<Point>();
        observers = new LinkedList<SurfacesControllerObserver>();
        materials = new ArrayList<Material>();
        mousePosition = new Point(0, 0);
        camPos = new Point(0, 0);
        mousePositionZoom = new Point(0, 0);
        materials.add(new Material());
        historyIndex = 0;
    }

    public void addSurface(Surface surface) {
        surfaces.add(surface);
        history.add(0, surfaces);
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

    public void setCanvasPosition(Point p) {
        this.canvasPosition = p;
    }

    public  ArrayList<Material> getMaterials() {
        return this.materials;
    }

    public void addRectangleHole(Surface surface) {
        Point pointA = points.get(0);
        Point pointC = points.get(1);
        Point pointB = new Point(pointC.x, pointA.y);
        Point pointD = new Point(pointA.x, pointC.y);
        surface.digHole(new ArrayList<Point>(Arrays.asList(pointA, pointB, pointC, pointD)));
        addHistory();
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
        surface.setMaterial(materials.get(0));
        surface.setPattern(new Pattern());
        surfaces.add(surface);
        addHistory();
        notifyObserverForSurfaces();
    }

    public void deleteSurface() {
        for (Iterator<Surface> iter = surfaces.listIterator(); iter.hasNext();) {
            Surface surface = iter.next();
            if (surface.isSelected())
                iter.remove();
        }
        addHistory();
        notifyObserverForSurfaces();
    }

    public void deleteSurface(Surface surfaceToDelete) {
        surfaces.remove(surfaceToDelete);
        addHistory();
        notifyObserverForSurfaces();
    }

    public double getZoom() {
        return zoom;
    }

    public void switchGrid() {
        gridSwitch = !gridSwitch;
        notifyObserverForSurfaces();
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
                    for (Polygon hole : surface.getHoles()) {
                        for (Point surfacePoint : hole.getPoints()) {
                            surfacePoint.initX = surfacePoint.x;
                            surfacePoint.initY = surfacePoint.y;
                        }
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
        } else if (state == State.CUT_SURFACE) {
            cutSurface = isInsideAnySurface(mousePosition);
            if (cutSurface == null) {
                points.clear();
                surfaces.remove(tmpSurface);
                System.out.println("Hollowing tool has to be used on a surface.");
                return;
            }
            points.add(point);
            if (points.size() == 2) {
                surfaces.remove(tmpSurface);
                if (isInsideAnySurface(mousePosition) == cutSurface) {
                    addRectangleHole(cutSurface);
                } else {
                    System.out.println("Hollowing tool has to be used on a surface.");
                }
                cutSurface = null;
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

    public void onMouseReleased() {
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
            if (firstSurface.isSelected() && firstSurface.getSurfaceType() == SurfaceType.REGULAR) {
                for (Surface secondSurface : surfaces) {
                    if (firstSurface != secondSurface && secondSurface.isSelected() && secondSurface.getSurfaceType() == SurfaceType.REGULAR) {
                        SurfacePosition position = firstSurface.getPositionSurface(secondSurface);
                         if (position == SurfacePosition.LEFT) {
                             firstSurface.getPoints().add(secondSurface.getPoints().get(2));
                             firstSurface.getPoints().add(secondSurface.getPoints().get(3));
                             firstSurface.getPoints().add(secondSurface.getPoints().get(0));
                             firstSurface.getPoints().add(secondSurface.getPoints().get(1));
                             firstSurface.setIrregular();
                             firstSurface.getTiles().clear();
                             deleteSurface(secondSurface);
                         }
                        if (position == SurfacePosition.RIGHT) {
                            secondSurface.getPoints().add(firstSurface.getPoints().get(2));
                            secondSurface.getPoints().add(firstSurface.getPoints().get(3));
                            secondSurface.getPoints().add(firstSurface.getPoints().get(0));
                            secondSurface.getPoints().add(firstSurface.getPoints().get(1));
                            secondSurface.setIrregular();
                            secondSurface.getTiles().clear();
                            deleteSurface(firstSurface);
                        }
                        if (position == SurfacePosition.TOP) {
                            firstSurface.getPoints().add(1, secondSurface.getPoints().get(2));
                            firstSurface.getPoints().add(1, secondSurface.getPoints().get(1));
                            firstSurface.getPoints().add(1, secondSurface.getPoints().get(0));
                            firstSurface.getPoints().add(1, secondSurface.getPoints().get(3));
                            firstSurface.setIrregular();
                            firstSurface.getTiles().clear();
                            deleteSurface(secondSurface);
                        }
                        if (position == SurfacePosition.BOTTOM) {
                            secondSurface.getPoints().add(1, firstSurface.getPoints().get(2));
                            secondSurface.getPoints().add(1, firstSurface.getPoints().get(1));
                            secondSurface.getPoints().add(1, firstSurface.getPoints().get(0));
                            secondSurface.getPoints().add(1, firstSurface.getPoints().get(3));
                            secondSurface.setIrregular();
                            secondSurface.getTiles().clear();
                            deleteSurface(firstSurface);
                        }
                        return;
                    }
                }
            }
        }
    }

    public Surface isInsideAnySurface(Point p) {
        for (Surface tmp : surfaces) {
            if (tmp.isInside(p))
                return (tmp);
        }
        return (null);
    }

    public Point gridAttractMouse(Point before) {
        Point after = new Point(before);
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

    public void changeSurfaceMaterial(Surface surface) {
        int index = materials.indexOf(surface.getMaterial());
        if (index == materials.size() - 1)
            surface.setMaterial(materials.get(0));
        else
            surface.setMaterial(materials.get(index + 1));
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

    public void rebuildAllSurface() {
        for (Surface surface : surfaces) {
            surface.onMoved();
        }
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
        if (state == State.CREATE_RECTANGULAR_SURFACE || state == State.CUT_SURFACE) {
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
            if (movingSurface != null) {
                for (Point surfacePoint : movingSurface.getPoints()) {
                    surfacePoint.x = surfacePoint.initX - (pressedPoint.x - point.x);
                    surfacePoint.y = surfacePoint.initY - (pressedPoint.y - point.y);
                }
                for (Polygon hole : movingSurface.getHoles()) {
                    if (hole == null)
                        continue;
                    for (Point p : hole.getPoints()) {
                        if (p == null)
                            continue;
                        p.x = p.initX - (pressedPoint.x - point.x);
                        p.y = p.initY - (pressedPoint.y - point.y);
                    }
                }
                movingSurface.onMoved();
            }
        }
        notifyObserverForSurfaces();
    }

    public List<Surface> getSurfaces() {
        return surfaces;
    }

    private void addHistory() {
        for (int i = 0; i < historyIndex; i++)
            history.remove(0);
        historyIndex = 0;
        history.add(0, new ArrayList<Surface>(surfaces));
    }

    public void undo() {
        if (historyIndex < history.size() - 1)
            historyIndex++;
        surfaces = new ArrayList<Surface>(history.get(historyIndex));
        notifyObserverForSurfaces();
    }

    public void redo() {
        if (historyIndex > 0)
            historyIndex--;
        surfaces = new ArrayList<Surface>(history.get(historyIndex));
        notifyObserverForSurfaces();
    }

    public void zoomIn() {
        zoom = zoom * 1.1f;
        camPos.x = (int)((mousePosition.x - camPos.x) * 0.1f + camPos.x);
        camPos.y = (int)((mousePosition.y - camPos.y) * 0.1f + camPos.y);
        notifyObserverForSurfaces();
    }

    public void zoomOut() {
        zoom = zoom * 0.9f;
        camPos.x = (int)((mousePosition.x - camPos.x) * -0.1f + camPos.x);
        camPos.y = (int)((mousePosition.y - camPos.y) * -0.1f + camPos.y);
        notifyObserverForSurfaces();
    }

    public void registerObserver(SurfacesControllerObserver newListener) {
        observers.add(newListener);
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
        dest.less(camPos);
        dest.x = (int)Math.round(dest.x * zoom);
        dest.y = (int)Math.round(dest.y * zoom);
        return dest;
    }
}
