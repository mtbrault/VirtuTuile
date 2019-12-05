package fr.virtutuile.domain;

public class Material implements java.io.Serializable {

    private int width;
    private int height;
    private String color;


    public Material() {
        this.width = 10;
        this.height = 20;
        this.color = "#FFFFFF";
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
