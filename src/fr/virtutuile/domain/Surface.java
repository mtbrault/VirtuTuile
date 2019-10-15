package fr.virtutuile.domain;

import java.util.List;

public class Surface extends Polygon {

    private List<Tile> tiles;
    private boolean selected;
    private Pattern pattern;
    private Material material;

    public Surface(List<Point> points) {
        super(points, PolygonType.SURFACE);
    }

    public int getNbTiles() {
        return tiles.size();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


}
