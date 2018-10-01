package GiedriusKristinaitis;

import java.awt.*;

public class SnakeCell extends Cell {

    // earlier position of the snake cell
    private int previous_x;
    private int previous_y;

    /**
     * Default class constructor
     */
    public SnakeCell(){}


    /**
     * Class constructor with parameters
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @param color color of the cell
     */
    public SnakeCell(int x, int y, Color color){
        super(x, y, color);
    }


    /**
     * Class constructor with color parameter
     * @param color color of the cell
     */
    public SnakeCell(Color color){
        super(color);
    }


    // ******** GETTERS ******** //
    public int getPreviousX() { return previous_x; }
    public int getPreviousY() { return previous_y; }


    // ******** SETTERS ******** //
    public void setPreviousX(int previous_x) { this.previous_x = previous_x; }
    public void setPreviousY(int previous_y) { this.previous_y = previous_y; }
}
