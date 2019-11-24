package fr.virtutuile.domain;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
    public Pattern() {
    }

    public List<Tile> build(Material material, Surface surface) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        if (surface.getSurfaceType() == SurfaceType.REGULAR) {
            Point point1 = surface.getPoints().get(0);
            Point point2 = surface.getPoints().get(1);
            Point point4 = surface.getPoints().get(3);
            int nbXTiles = Math.abs(point1.x - point2.x) / (material.getWidth() + surface.getJointSize()) + 1;
            int nbYTiles = Math.abs(point1.y - point4.y) / (material.getHeight() + surface.getJointSize()) + 1;
            int minPointX = point1.x;
            int minPointY = point1.y;
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
        }
        return tiles;
    }
}
