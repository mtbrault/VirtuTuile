package fr.virtutuile.domain;

public class Material {

    private int width;
    private int height;
    private Color color;

    public Material(int width, int height, Color color)  {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Material() {
        this.width = 50;
        this.height = 50;
        this.color = new Color();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
