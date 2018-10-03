package GiedriusKristinaitis;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {

    // all cells of the snake
    private List<SnakeCell> cells = new ArrayList<SnakeCell>();

    private int x_speed; // horizontal speed of the snake
    private int y_speed; // vertical speed of the snake

    // flags indicating whether the snake can turn in certain directions
    private boolean canTurnVertical = true;
    private boolean canTurnHorizontal = false;

    // body color
    private final Color bodyColor;

    /**
     * Class constructor
     * @param headColor color of the snake's head
     * @param bodyColor color of the snake's body
     * @param start_x starting x coordinate of the snake
     * @param start_y starting y coordinate of the snake
     * @param speed_x starting x speed of the snake
     * @param speed_y starting y speed of the snake
     */
    public Snake(Color headColor, Color bodyColor, int start_x, int start_y, int speed_x, int speed_y){
        x_speed = speed_x;
        y_speed = speed_y;

        // initialize the head cell
        SnakeCell head = new SnakeCell(start_x, start_y, headColor);
        cells.add(head);

        this.bodyColor = bodyColor;
    }


    /**
     * Draws the snake on the screen
     * @param game game instance to draw to
     */
    public void draw(Game game){
        for(SnakeCell cell: cells){
            cell.draw(game);
        }
    }


    /**
     * Updates the position of the snake
     */
    public void update(Game game){
        // update head's position
        SnakeCell head = cells.get(0);
        head.setPreviousX(head.getX());
        head.setPreviousY(head.getY());

        int headX = head.getX() + x_speed;
        int headY = head.getY() + y_speed;

        if(headX >= game.SCREEN_WIDTH){
            headX = 0;
        }else if(headX < 0){
            headX = game.SCREEN_WIDTH - 1;
        }

        if(headY >= game.SCREEN_HEIGHT - 1){
            headY = 0;
        }else if(headY < 0){
            headY = game.SCREEN_HEIGHT - 2;
        }

        head.setX(headX);
        head.setY(headY);

        // update each cell's position
        for(int i = 1; i < cells.size(); i++){
            SnakeCell cell = cells.get(i);
            SnakeCell cellAhead = cells.get(i - 1);

            cell.setPreviousX(cell.getX());
            cell.setPreviousY(cell.getY());
            cell.setX(cellAhead.getPreviousX());
            cell.setY(cellAhead.getPreviousY());
        }

        if(x_speed != 0){
            canTurnVertical = true;
        }

        if(y_speed != 0){
            canTurnHorizontal = true;
        }
    }


    /**
     * Checks if the snake head's position overlaps with any snake cell's position - the
     * snake is trying to eat itself
     * @return true if the snake is eating itself, false otherwise
     */
    public boolean isTryingToEatItself(){
        SnakeCell head = cells.get(0);

        for(int i = 1; i < cells.size(); i++){
            SnakeCell cell = cells.get(i);

            if(cell.getX() == head.getX() && cell.getY() == head.getY()){
                return true;
            }
        }

        return false;
    }


    /**
     * Adds a new cell to the snake, called every time the snake eats food
     */
    public void grow(){
        SnakeCell cell = new SnakeCell(bodyColor);
        SnakeCell lastCell = cells.get(cells.size() - 1);

        cell.setX(lastCell.getPreviousX());
        cell.setY(lastCell.getPreviousY());

        cells.add(cell);
    }


    /**
     * Makes the snake more upwards
     */
    public void moveUp(){
        if(!canTurnVertical){
            return;
        }

        x_speed = 0;
        y_speed = -1;

        canTurnVertical = false;
    }


    /**
     * Makes the snake move downwards
     */
    public void moveDown(){
        if(!canTurnVertical){
            return;
        }

        x_speed = 0;
        y_speed = 1;

        canTurnVertical = false;
    }


    /**
     * Makes the snake move left
     */
    public void moveLeft(){
        if(!canTurnHorizontal){
            return;
        }

        y_speed = 0;
        x_speed = -1;

        canTurnHorizontal = false;
    }


    /**
     * Makes the snake move right
     */
    public void moveRight(){
        if(!canTurnHorizontal){
            return;
        }

        y_speed = 0;
        x_speed = 1;

        canTurnHorizontal = false;
    }


    /**
     * Gets all the cells of the snake
     * @return list with all cells
     */
    public List<SnakeCell> getCells(){
        return cells;
    }


    /**
     * Checks if there is a snake cell at the specified position
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @return true if there is a cell, false otherwise
     */
    public boolean hasCellAt(int x, int y){
        for(SnakeCell cell: cells){
            if(cell.getX() == x && cell.getY() == y){
                return true;
            }
        }

        return false;
    }


    /**
     * Makes the snake able to turn any direction
     */
    public void makeAbleToTurn(){
        canTurnHorizontal = true;
        canTurnVertical = true;
    }


    // ******** GETTERS ******** //
    public int getHeadX() { return cells.get(0).getX(); }
    public int getHeadY() { return cells.get(0).getY(); }
    public int getXSpeed() { return x_speed; }
    public int getYSpeed() { return y_speed; }
}
