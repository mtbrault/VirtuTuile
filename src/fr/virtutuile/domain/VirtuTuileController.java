package fr.virtutuile.domain;

import javafx.scene.shape.*;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.*;
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
	private List<Hole> tmpHoleList;
	private Surface tmpSurface = null;
	private Surface movingSurface = null;
	private Surface cutSurface = null;
	public Point mousePosition;
	private List<SurfacesControllerObserver> observers;
	private List<ZoomControllerObserver> observersZoom;
	private double zoom = 1;
	public Point camPos;
	private Point canvasPosition;
	private State state = State.UNKNOWN;
	private Metering metering = Metering.CM;
	private boolean gridSwitch = false;
	private boolean isBeingDragged = false;
	private int gridDim = 100;
	private int gridTreshHold = 4;
	private Hole movingHole = null;
	private Surface movingHoleTheSurface = null;
	private Tile movingTile = null;
	private ArrayList<Line2D> lines = new ArrayList<Line2D>();

	public VirtuTuileController() {
		surfaces = new ArrayList<Surface>();
		history = new ArrayList<ArrayList<Surface>>();
		history.add(new ArrayList<Surface>());
		points = new ArrayList<Point>();
		observers = new LinkedList<SurfacesControllerObserver>();
		observersZoom = new LinkedList<ZoomControllerObserver>();
		materials = new ArrayList<Material>();
		mousePosition = new Point(0, 0);
		camPos = new Point(0, 0);
		materials.add(new Material("Materiau 1"));
		historyIndex = 0;
	}

	public List<Point> getPoints() {
		return points;
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
		return gridSwitch;
	}

	public void addMaterial(Material material) {
		materials.add(material);
	}

	public void setCanvasPosition(Point p) {
		this.canvasPosition = p;
	}

	public ArrayList<Material> getMaterials() {
		return this.materials;
	}

	public void setSurfaceMaterial(Surface surface, Material material) {
		surface.setMaterial(material);
	}

	public int getSurfaceMaterialIndex(Surface surface, Object[] obj) {
		Object mat = surface.getMaterial();
		int index = 0;
		for (Object objs : obj) {

			if (objs == mat) {
				return index;
			}
			index++;
		}
		return 0;
	}

	public void addRectangleHole(Surface surface) {
		Point pointA = points.get(0);
		Point pointC = points.get(1);
		Point pointB = new Point(pointC.x, pointA.y);
		Point pointD = new Point(pointA.x, pointC.y);
		surface.digHole(new ArrayList<Point>(Arrays.asList(pointA, pointB, pointC, pointD)));
		surface.setPattern(surface.getPattern());
		notifyObserverForSurfaces();
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

	public void setGridDim(int value) {
		gridDim = value;
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

	public void addTmpIrregularSurface() {
		Point point1 = new Point(points.get(0));
		Point point2 = new Point(mousePosition.x, mousePosition.y);
		List<Point> surfacePoints = new ArrayList<Point>(Arrays.asList(point1, point2));
		tmpSurface = new Surface(surfacePoints);
		tmpSurface.setIrregular();
		surfaces.add(tmpSurface);
		notifyObserverForSurfaces();
	}

	public void onMousePressed(Point point, boolean isLeft) {
		if (state == State.MOVE) {
			points.add(point);
		} else if (state == State.MOVE_SURFACE) {
			points.add(point);
			for (Surface surface : surfaces) {
				if (surface.isInside(point)) {
					surface.setSelected(true);
					movingSurface = surface;
					isBeingDragged = true;
				} else
					surface.setSelected(false);
			}
			notifyObserverForSurfaces();
		}  else if (state == State.MOVE_ONE_TILE) {
			for (Surface surface : surfaces) {
				for (Tile tile : surface.getTiles()) {
					if (tile.isInside(point)) {
						movingTile = tile;
					}
				}
			}
		}else if (state == State.MOVE_HOLE) {
			points.add(point);
			for (Surface surface : surfaces) {
				for (Hole hole : surface.getHoles()) {
					if (hole.isInside(point)) {
						movingHole = hole;
						movingHoleTheSurface = surface;
						tmpHoleList = movingHoleTheSurface.getHoles();
					}
				}
			}
		} else if (state == State.SELECTION) {
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
				notifyObserverForSurfaces();
			} else {
				addTmpSurface();
			}
		} else if (state == State.CREATE_IRREGULAR_SURFACE) {
			points.add(point);
			if (points.size() == 1) {
				addTmpIrregularSurface();
			} else {
				if (!isLeft) {
					points.clear();
					tmpSurface.setMaterial(materials.get(0));
					tmpSurface.onMoved();
					this.addHistory();
					tmpSurface = null;
					return;
				}
				tmpSurface.addPoint(new Point(point));
			}
		} else if (state == State.CUT_SURFACE) {
			points.add(point);
			if (points.size() == 2) {
				if (cutSurface != null || (cutSurface = isInsideAnySurface(mousePosition)) != null)
					addRectangleHole(cutSurface);
				else if ((cutSurface = isInsideAnySurface(new Point(points.get(0).x, points.get(1).y))) != null
						|| (cutSurface = isInsideAnySurface(new Point(points.get(1).x, points.get(0).y))) != null) {
					addRectangleHole(cutSurface);
				}
				surfaces.remove(tmpSurface);
				cutSurface = null;
				points.clear();
				addHistory();
			} else {
				cutSurface = isInsideAnySurface(mousePosition);
				addTmpSurface();
			}
		} else if (state == State.MOVE_PATTERN) {
			mousePosition.initX = mousePosition.x;
			mousePosition.initY = mousePosition.y;
			movingSurface = isInsideAnySurface(mousePosition);
		}
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
					addHistory();
					notifyObserverForSurfaces();
				}
			}
		} else if (state == State.MOVE_ONE_TILE) {
			movingTile = null;
		} else if (state == State.MOVE_HOLE) {
			isBeingDragged = false;
			movingHole = null;
			movingHoleTheSurface = null;
			points.clear();
			lines.clear();
			addHistory();
		} else if (state == State.MOVE_PATTERN) {
			movingSurface = null;
		}
	}

	public void combineSelectedSurfaces() {
		for (Surface firstSurface : surfaces) {
			if (firstSurface.isSelected()) {
				for (Surface secondSurface : surfaces) {
					if (firstSurface != secondSurface && secondSurface.isSelected()) {
						firstSurface.merge(secondSurface);
						firstSurface.setIrregular();
						deleteSurface(secondSurface);
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
		Point rCoord = graphicToCoord(before.x, before.y);
		Point xyClosest = coordToGraphic(
				rCoord.x % gridDim > gridDim / 2 ? rCoord.x - rCoord.x % gridDim + gridDim
						: rCoord.x - rCoord.x % gridDim,
				rCoord.y % gridDim > gridDim / 2 ? rCoord.x - rCoord.y % gridDim + gridDim
						: rCoord.y - rCoord.y % gridDim);
		Point after = new Point(before);

		if (before.x - xyClosest.x <= gridTreshHold && before.x - xyClosest.x >= -gridTreshHold)
			after.x -= before.x - xyClosest.x;
		if (before.y - xyClosest.y <= gridTreshHold && before.y - xyClosest.y >= -gridTreshHold)
			after.y -= before.y - xyClosest.y;
		return (after);
	}

	public Point gridAttractSurface(Point before) {
		Point after = new Point(before);
		Point vector = new Point(gridTreshHold + 1, gridTreshHold + 1);
		for (Point surfacePoint : movingSurface.getPoints()) {
			Point gCoord = coordToGraphic(surfacePoint.x, surfacePoint.y);
			Point xyClosest = coordToGraphic(
					surfacePoint.x % gridDim > gridDim / 2 ? surfacePoint.x - surfacePoint.x % gridDim + gridDim
							: surfacePoint.x - surfacePoint.x % gridDim,
					surfacePoint.y % gridDim > gridDim / 2 ? surfacePoint.x - surfacePoint.y % gridDim + gridDim
							: surfacePoint.y - surfacePoint.y % gridDim);
			if (gCoord.x - xyClosest.x <= gridTreshHold && gCoord.x - xyClosest.x >= -gridTreshHold)
				vector.x = Math.abs(vector.x) < Math.abs(gCoord.x - xyClosest.x) ? vector.x : gCoord.x - xyClosest.x;
			if (gCoord.y - xyClosest.y <= gridTreshHold && gCoord.y - xyClosest.y >= -gridTreshHold)
				vector.y = Math.abs(vector.y) < Math.abs(gCoord.y - xyClosest.y) ? vector.y : gCoord.y - xyClosest.y;
		}
		if (vector.x > gridTreshHold)
			vector.x = 0;
		if (vector.y > gridTreshHold)
			vector.y = 0;
		after.x -= vector.x;
		after.y -= vector.y;
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
		Point after = isBeingDragged && movingSurface.isInside(graphicToCoord(before.x, before.y))
				? gridAttractSurface(before)
				: gridAttractMouse(before);
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

	public int getGridDim() {
		return gridDim;
	}

	public ArrayList<Line2D> getLines() {
		return lines;
	}

	public void onMouseMoved(Point point) {
		if (gridSwitch)
			point = gridMagnet(point);
		else
			point = graphicToCoord(point.x, point.y);
		Point mousePosBefore = new Point(mousePosition);
		mousePosition.setPos(point.x, point.y);
		for (Surface surface : surfaces) {
			for (Tile tile : surface.getTiles()) {
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
		} else if (state == State.MOVE_HOLE) {
			Point movementVec = new Point(mousePosition.x - mousePosBefore.x, mousePosition.y - mousePosBefore.y);
			if (movingHole != null) {
				movingHole.move(movementVec.x, movementVec.y);
				movingHoleTheSurface.setHoles(tmpHoleList);
				movingHoleTheSurface.digHole(movingHole.getPoints());
				movingHoleTheSurface.setPattern(movingHoleTheSurface.getPattern());
				lines.clear();
				for (Point surfacePoint : movingHoleTheSurface.getPoints()) {
					Point firstPoint = coordToGraphic(surfacePoint.x, surfacePoint.y);
					Point secondPoint = coordToGraphic(movingHole.getPoints().get(0).x, movingHole.getPoints().get(0).y);
					lines.add(new Line2D.Double(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y));
				}
			}
		} else if (state == State.MOVE_ONE_TILE) {
			Point movementVec = new Point(mousePosition.x - mousePosBefore.x, mousePosition.y - mousePosBefore.y);
			if (movingTile != null) {
				movingTile.move(movementVec.x, movementVec.y);
			}
		} else if (state == State.CREATE_IRREGULAR_SURFACE) {
			if (points.size() >= 1) {
				List<Point> surfacePoints = tmpSurface.getPoints();
				Point lastPoint = surfacePoints.get(surfacePoints.size() - 1);
				lastPoint.x = mousePosition.x;
				lastPoint.y = mousePosition.y;
			}
		} else if (state == State.MOVE && points.size() > 0) {
			Point pressedPoint = points.get(0);
			camPos.x = (int) (zoom * (pressedPoint.x - mousePosition.x)) + camPos.x;
			camPos.y = (int) (zoom * (pressedPoint.y - mousePosition.y)) + camPos.y;
		} else if (state == State.MOVE_SURFACE && points.size() > 0) {
			Point movementVec = new Point(mousePosition.x - mousePosBefore.x, mousePosition.y - mousePosBefore.y);
			if (movingSurface != null)
				movingSurface.move(movementVec.x, movementVec.y);
		} else if (state == State.MOVE_PATTERN && movingSurface != null) {
			if (movingSurface.isInside(mousePosition)) {
				Point vector = new Point(mousePosition.x - mousePosBefore.x, mousePosition.y - mousePosBefore.y);
				movingSurface.movePatern(vector.x, vector.y);
			}
			movingSurface.setPattern(movingSurface.getPattern());
		}
		notifyObserverForSurfaces();
	}

	public ArrayList<Surface> getSurfaces() {
		return surfaces;
	}
	public int convertMeteringToDisplay(int cm) {
		switch (this.metering) {
			case CM:
				return cm;
			case POUCE:
				return (int)Math.round(cm / 2.54);
		}
		return cm;
	}
	
	public void setMetering(Metering value) {
		this.metering = value;
	}
	
	public Metering getMetering() {
		return this.metering;
	}
	
	public int convertMeteringToCm(double otherMetrics) {
		switch (this.metering) {
			case CM:
				return (int)Math.round(otherMetrics);
			case POUCE:
				return (int)Math.round(otherMetrics * 2.54);
		}
		return (int)Math.round(otherMetrics);
	}
	public void saveObject(File filename) {
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			Serialize object = new Serialize(surfaces, materials, points, camPos, zoom, history, historyIndex);
			out.writeObject(object);
			out.close();
			file.close();
			System.out.println("Object has been saved !");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void loadObject(File filename) {
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			Serialize object = (Serialize) in.readObject();
			surfaces = object.surfaces;
			materials = object.materials;
			zoom = object.zoom;
			camPos = object.camPos;
			points = object.points;
			history = object.history;
			historyIndex = object.historyIndex;
			in.close();
			file.close();
			System.out.println("Object has been loaded");
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println(ex);
		}
	}

	public void addHistory() {
		for (int i = 0; i < historyIndex; i++)
			history.remove(0);
		historyIndex = 0;
		ArrayList<Surface> tmp = new ArrayList<Surface>();
		for (Surface surface : surfaces)
			tmp.add(new Surface(surface));
		history.add(0, tmp);
	}

	public void undo() {
		if (historyIndex < history.size() - 1)
			historyIndex++;
		else
			return ;
		surfaces = new ArrayList<Surface>(history.get(historyIndex));
		notifyObserverForSurfaces();
	}

	public void redo() {
		if (historyIndex > 0)
			historyIndex--;
		else
			return ;
		surfaces = new ArrayList<Surface>(history.get(historyIndex));
		notifyObserverForSurfaces();
	}

	public void zoomIn() {
		zoom = zoom * 1.1f;
		camPos.x = (int) ((mousePosition.x - camPos.x) * 0.1f + camPos.x);
		camPos.y = (int) ((mousePosition.y - camPos.y) * 0.1f + camPos.y);
		notifyObserverForSurfaces();
		notifyObserverForZoom();
	}

	public void zoomOut() {
		zoom = zoom * 0.9f;
		camPos.x = (int) ((mousePosition.x - camPos.x) * -0.1f + camPos.x);
		camPos.y = (int) ((mousePosition.y - camPos.y) * -0.1f + camPos.y);
		notifyObserverForSurfaces();
		notifyObserverForZoom();
	}

	public void registerObserver(SurfacesControllerObserver newListener) {
		observers.add(newListener);
	}

	public void registerObserverZoom(ZoomControllerObserver newListenerZoom) {
		observersZoom.add(newListenerZoom);
	}

	public void notifyObserverForZoom() {
		for (ZoomControllerObserver observerZoom : observersZoom) {
			observerZoom.notifyUpdatedZoom();
		}
	}

	public void notifyObserverForSurfaces() {
		for (SurfacesControllerObserver observer : observers) {
			observer.notifyObserver();
		}
	}

	public Point graphicToCoord(int x, int y) {
		Point dest = new Point(x, y);
		dest.x = (int) Math.round(dest.x / zoom);
		dest.y = (int) Math.round(dest.y / zoom);
		dest.add(camPos);
		return dest;
	}

	public Point coordToGraphic(int x, int y) {
		Point dest = new Point(x, y);
		dest.less(camPos);
		dest.x = (int) Math.round(dest.x * zoom);
		dest.y = (int) Math.round(dest.y * zoom);
		return dest;
	}

	public Point getExtremePoint(Polygon surface, int x, int y) {
		int value = (x == -1 || y == -1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		Point pointValue = null;

		for (Point point : surface.getPoints()) {
			if (x == 1 && point.x > value) {
				value = point.x;
				pointValue = point;
			} else if (y == 1 && point.y >= value) {
				value = point.y;
				pointValue = point;
			} else if (x == -1 && point.x < value) {
				value = point.x;
				pointValue = point;
			} else if (y == -1 && point.y < value) {
				value = point.y;
				pointValue = point;
			}
		}
		return pointValue;
	}

	private Surface getExtremeSurface(Surface surface) {
		Point left = getExtremePoint(surface, -1, 0);
		Point right = getExtremePoint(surface, 1, 0);
		Point down = getExtremePoint(surface, 0, 1);
		Point up = getExtremePoint(surface, 0, -1);
		Point point1 = new Point(left.x, up.y);
		Point point2 = new Point(right.x, up.y);
		Point point3 = new Point(right.x, down.y);
		Point point4 = new Point(left.x, down.y);
		return new Surface(new ArrayList<Point>() {
			{
				add(point1);
				add(point2);
				add(point3);
				add(point4);
			}
		});
	}

	public void changeSurfaceWithSurfaceCoord(Surface surface, int width, int height) {
		Point landmark = null;

		for (Surface it : surfaces) {
			if (it.isSelected() && it != surface)
				landmark = it.getPoints().get(0);
		}
		if (landmark == null)
			return;
		int x = landmark.x - surface.getPoints().get(0).x + width;
		int y = landmark.y - surface.getPoints().get(0).y + height;
		surface.move(x, y);
		notifyObserverForSurfaces();
		addHistory();
	}

	public void pastSurface(Surface surface, int opt) {
		Surface dest = null;
		for (Surface it : surfaces) {
			if (it.isSelected() && it != surface)
				dest = it;
		}
		if (dest == null)
			return;
		if (opt == 0) {
			if (getExtremePoint(dest, 0, -1).y > getExtremePoint(surface, 0, 1).y)
				return;
			else if (getExtremePoint(dest, 0, 1).y < getExtremePoint(surface, 0, -1).y)
				return;
			int destRight = getExtremePoint(dest, 1, 0).x;
			int destLeft = getExtremePoint(dest, -1, 0).x;
			int fromRight = getExtremePoint(surface, 1, 0).x;
			int fromLeft = getExtremePoint(surface, -1, 0).x;
			if (destRight < fromLeft) {
				surface.move(destRight - fromLeft, 0);
			} else if (destLeft > fromRight) {
				surface.move(destLeft - fromRight, 0);
			}
		} else if (opt == 1) {
			if (getExtremePoint(dest, -1, 0).x > getExtremePoint(surface, 1, 0).x)
				return;
			else if (getExtremePoint(dest, 1, 0).x < getExtremePoint(surface, -1, 0).x)
				return;
			int destBot = getExtremePoint(dest, 0, 1).y;
			int destTop = getExtremePoint(dest, 0, -1).y;
			int fromTop = getExtremePoint(surface, 0, -1).y;
			int fromBot = getExtremePoint(surface, 0, 1).y;
			if (destBot < fromTop) {
				surface.move(0, destBot - fromTop);
			} else if (destTop > fromBot) {
				surface.move(0, destTop - fromBot);
			}
		}
		notifyObserverForSurfaces();
		addHistory();
	}

	public void alignSurface(Surface surface, int x, int y) {
		Surface dest = null;
		for (Surface it : surfaces) {
			if (it.isSelected() && it != surface)
				dest = it;
		}
		if (dest == null)
			return;
		Point destPoint = getExtremePoint(dest, x, y);
		Point fromPoint = getExtremePoint(surface, x, y);
		if (x != 0)
			surface.move(destPoint.x - fromPoint.x, 0);
		else
			surface.move(0, destPoint.y - fromPoint.y);
		notifyObserverForSurfaces();
		addHistory();
	}

	public void centerSurface(Surface surface, int opt) {
		Surface dest = null;
		for (Surface it : surfaces) {
			if (it.isSelected() && it != surface)
				dest = it;
		}
		if (dest == null)
			return;
		if (opt == 0) {
			int destOrigine = getExtremePoint(dest, -1, 0).x;
			int destWidth = getExtremePoint(dest, 1, 0).x - destOrigine;
			int origine = getExtremePoint(surface, -1, 0).x;
			int fromWidth = getExtremePoint(surface, 1, 0).x - origine;
			int diff = (destWidth - fromWidth) / 2;
			if (diff == 0)
				return;
			surface.move(destOrigine + diff - origine, 0);
			notifyObserverForSurfaces();
			addHistory();
		} else if (opt == 1) {
			int destOrigine = getExtremePoint(dest, 0, -1).y;
			int destHeight = getExtremePoint(dest, 0, 1).y - destOrigine;
			int origine = getExtremePoint(surface, 0, -1).y;
			int fromHeight = getExtremePoint(surface, 0, 1).y - origine;
			int diff = (destHeight - fromHeight) / 2;
			if (diff == 0)
				return;
			surface.move(0, destOrigine + diff - origine);
			notifyObserverForSurfaces();
			addHistory();
		}
	}

	public void detectTile(int size) {
		for (Surface surface : surfaces) {
			for (Tile tile : surface.getTiles()) {
				int top = getExtremePoint(tile, 0, -1).y;
				int bottom = getExtremePoint(tile, 0, 1).y;
				int right = getExtremePoint(tile, 1, 0).x;
				int left = getExtremePoint(tile, -1, 0).x;
				if (right - left < size)
					tile.setDetected(true);
				else if (bottom - top < size)
					tile.setDetected(true);
				else
					tile.setDetected(false);
			}
		}
		notifyObserverForSurfaces();
	}
}
