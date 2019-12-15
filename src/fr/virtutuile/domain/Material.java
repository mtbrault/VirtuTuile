package fr.virtutuile.domain;

import java.awt.Color;

public class Material implements java.io.Serializable {

    private int width;
    private int height;
    private String name;
    private Color color;

    public Material(String name) {
        this.name = name;
    	this.width = 10;
        this.height = 20;
        this.color = color.WHITE;
    }
   
    public Material() {
    	this.name = "Matériaux";
        this.width = 10;
        this.height = 20;
        this.color = color.WHITE;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
	@Override
	public String toString() {
		return this.name;
	}
}
