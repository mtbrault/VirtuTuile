package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Pattern implements java.io.Serializable {
    public Pattern() {
    }

    public List<Tile> build(Material material, Surface surface) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        Point point1 = surface.getPoints().get(0);
        Point point2 = surface.getPoints().get(1);
        Point point4 = surface.getPoints().get(3);
        int nbXTiles = Math.abs(point1.x - point2.x) / (material.getWidth() + surface.getJointSize()) + 1;
        int nbYTiles = Math.abs(point1.y - point4.y) / (material.getHeight() + surface.getJointSize()) + 1;
        int minPointX = point1.x;
        int minPointY = point1.y;
        if (surface.getSurfaceType() == SurfaceType.REGULAR && surface.getPatternId() == 0) {
            for (int y = 0; y < nbYTiles; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    points.add(new Point(x * material.getWidth() + minPointX + (surface.getJointSize() * x), y * material.getHeight() + minPointY + (y*surface.getJointSize())));
                    int Xpoint = (x + 1) * material.getWidth() + minPointX + (x*surface.getJointSize());
                    points.add(new Point(Xpoint > point2.x ? point2.x : Xpoint, y * material.getHeight() + minPointY + (y*surface.getJointSize())));
                    int Ypoint = (y + 1) * material.getHeight() + minPointY + (y*surface.getJointSize());
                    points.add(new Point(Xpoint > point2.x ? point2.x : Xpoint, Ypoint < point4.y ? Ypoint : point4.y));
                    points.add(new Point(x * material.getWidth() + minPointX + (x*surface.getJointSize()), Ypoint < point4.y ? Ypoint : point4.y));
                    tiles.add(new Tile(points));
                }
            }
        } else if (surface.getSurfaceType() == SurfaceType.REGULAR && surface.getPatternId() == 1) {
            for (int y = 0; y < nbYTiles; y++) {
                for (int x = 0; x < nbXTiles; x++) {
                    ArrayList points = new ArrayList<Point>();
                    int Xpoint0 = x * material.getWidth() + minPointX + (surface.getJointSize() * x);
                    if (y % 2 == 0) {
                        Xpoint0 -= material.getWidth() * 0.7;
                        if (Xpoint0 < point1.x) {
                            Xpoint0 = point1.x;
                        }
                    }
                    int Ypoint0 = y * material.getHeight() + minPointY + (y*surface.getJointSize());
                    points.add(new Point(Xpoint0, Ypoint0));

                    int Xpoint = (x + 1) * material.getWidth() + minPointX + (x*surface.getJointSize());
                    if (y % 2 == 0) {
                        Xpoint -= material.getWidth() * 0.7;
                    }
                    points.add(new Point(Xpoint > point2.x ? point2.x : Xpoint, Ypoint0));

                    int Ypoint = (y + 1) * material.getHeight() + minPointY + (y*surface.getJointSize());
                    points.add(new Point(Xpoint > point2.x ? point2.x : Xpoint, Ypoint < point4.y ? Ypoint : point4.y));

                    points.add(new Point(Xpoint0, Ypoint < point4.y ? Ypoint : point4.y));
                    tiles.add(new Tile(points));
                }
                if (nbXTiles * material.getWidth() + (surface.getJointSize() * nbXTiles) - material.getWidth() * 0.7 < Math.abs(point1.x - point2.x) && y % 2 == 0) {
                    ArrayList points = new ArrayList<Point>();
                    double lastTileX = nbXTiles * material.getWidth() + (surface.getJointSize() * nbXTiles) - material.getWidth() * 0.7 + minPointX;
                    points.add(new Point((int)lastTileX, y * material.getHeight() + minPointY + (y*surface.getJointSize())));
                    points.add(new Point(point2.x, y * material.getHeight() + minPointY + (y*surface.getJointSize())));
                    int lastTileY = (y + 1) * material.getHeight() + minPointY + (y*surface.getJointSize());
                    points.add(new Point(point2.x, lastTileY < point4.y ? lastTileY : point4.y));
                    points.add(new Point((int)lastTileX, lastTileY < point4.y ? lastTileY : point4.y));
                    tiles.add(new Tile(points));
                }
            }
        }
        return tiles;
    }
}
