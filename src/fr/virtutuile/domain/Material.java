package fr.virtutuile.domain;

import java.awt.Color;
import java.util.ArrayList;

public class Material implements java.io.Serializable {

    private int width;
    private int height;
    private String name;
    private Color color;
    private int nbTileByBox = 10;	
    
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
    
    public void setNbTileByBox(int nb) {	
        this.nbTileByBox = nb;	
    }	

    public int getNbAllProjectUsedTile(ArrayList<Surface> surfaces) {
        int nbTiles = 0;
        for (Surface surface : surfaces) {
            if (surface.getMaterial() == this) {
                nbTiles += surface.getNbTiles();
            }
        }
        return nbTiles;
    }
    public int getNbTileByBox() {	
        return nbTileByBox;	
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
