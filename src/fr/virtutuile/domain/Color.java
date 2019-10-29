package fr.virtutuile.domain;

public class Color {
    public int red;
    public int green;
    public int blue;
    public float alpha;

    public Color(int red, int green, int blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.alpha = 1;
    }
}
