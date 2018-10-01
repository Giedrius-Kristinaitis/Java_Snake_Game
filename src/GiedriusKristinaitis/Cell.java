package GiedriusKristinaitis;

import java.awt.*;

public class Cell {

    // coordinates of the cell
    private int x;
    private int y;

    // color of the cell
    private Color color;

    /**
     * Default class constructor
     */
    public Cell(){}


    /**
     * Class constructor with parameters
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @param color color of the cell
     */
    public Cell(int x, int y, Color color){
        this.color = color;
        this.x = x;
        this.y = y;
    }


    /**
     * Class constructor with color parameter
     * @param color color of the cell
     */
    public Cell(Color color){
        this.color = color;
    }


    /**
     * Draws the cell on the screen
     * @param game game instance to draw to
     */
    public void draw(Game game){
        game.setBackColor(color);
        game.fillRect(y, x, 1, 1);
    }


    // ******** GETTERS ******** //
    public Color getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }


    // ******** SETTERS ******** //
    public void setColor(Color color) { this.color = color; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
