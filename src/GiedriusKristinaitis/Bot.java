package GiedriusKristinaitis;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    // game objects the bot might encounter
    private static final int GAME_OBJECT_OBSTACLE = -1;

    // bot's snake
    private Snake snake;

    // game grid variables
    private final int[][] grid;
    private final int gridWidth;
    private final int gridHeight;

    // path to the food
    private List<Point> path = new ArrayList<Point>();

    /**
     * Class constructor
     * @param snake snake the bot will control
     */
    public Bot(int gridWidth, int gridHeight, Snake snake){
        this.snake = snake;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        grid = new int[gridWidth][gridHeight];
    }


    /**
     * Initializes the game grid
     */
    public void initializeGrid(Cell[] obstacles){
        for(int x = 0; x < gridWidth; x++){
            for(int y = 0; y < gridHeight; y++){
                grid[x][y] = 0;
            }
        }

        for(Cell cell: obstacles){
            grid[cell.getX()][cell.getY()] = GAME_OBJECT_OBSTACLE;
        }
    }


    /**
     * Updates the game grid
     * @param player player's snake
     */
    public void updateGrid(Snake player){
        List<SnakeCell> cells = player.getCells();

        addCellsAsObstacles(cells);
        addCellsAsObstacles(snake.getCells());
    }


    /**
     * Adds cells to the grid as obstacles
     * @param cells list of cells to be treated as obstacles
     */
    private void addCellsAsObstacles(List<SnakeCell> cells){
        for(SnakeCell cell: cells){
            grid[cell.getX()][cell.getY()] = GAME_OBJECT_OBSTACLE;
        }
    }


    /**
     * Updates the path to the food
     */
    private void updatePath(Cell[] obstacles, Snake player, Cell food){
        if(path.size() > 0){
            for(int x = 0; x < gridWidth; x++){
                for(int y = 0; y < gridHeight; y++){
                    for(Point point: path){
                        if(point.getX() == x && point.getY() == y && grid[x][y] == GAME_OBJECT_OBSTACLE && !snake.hasCellAt(x, y)){
                            findPathToFood(obstacles, player, food);
                            break;
                        }
                    }
                }
            }
        }
    }


    /**
     * Resets the game grid
     */
    private void resetGrid(Cell[] obstacles, Snake player){
        initializeGrid(obstacles);
        updateGrid(player);
    }


    /**
     * Finds the path from the bot's snake to the food
     */
    public void findPathToFood(Cell[] obstacles, Snake player, Cell food){
        path.clear();
        resetGrid(obstacles, player);

        List<Point> points = new ArrayList<Point>();
        List<Point> newPoints = new ArrayList<Point>();

        points.add(new Point(food.getX(), food.getY()));

        moveToPoints(points, newPoints, 0, food);

        // find shortest path
        Point closest = getClosestPoint(snake.getHeadX(), snake.getHeadY());
        processPoint(closest.getX(), closest.getY());

        return;
    }


    /**
     * Gets the point that leads to the shortest path to the food from the specified point
     * @param x x coordinate of the specified point
     * @param y y coordinate of the specified point
     * @return point leading to the shortest path
     */
    private Point getClosestPoint(int x, int y){
        Point closest = new Point(x, y);

        int distanceLeft = 100000;
        int distanceRight = 100000;
        int distanceUp = 100000;
        int distanceDown = 100000;

        // check right point
        if((x + 1 >= 0 && x + 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x + 1][y] != GAME_OBJECT_OBSTACLE){ distanceRight = grid[x + 1][y]; }
        // check left point
        if((x - 1 >= 0 && x - 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x - 1][y] != GAME_OBJECT_OBSTACLE){ distanceLeft = grid[x - 1][y]; }
        // check bottom point
        if((x >= 0 && x < gridWidth) && (y + 1 >= 0 && y + 1 < gridHeight) && grid[x][y + 1] != GAME_OBJECT_OBSTACLE){ distanceDown = grid[x][y + 1]; }
        // check top point
        if((x >= 0 && x < gridWidth) && (y - 1 >= 0 && y - 1 < gridHeight) && grid[x][y - 1] != GAME_OBJECT_OBSTACLE){ distanceUp = grid[x][y - 1]; }

        // check right distance
        if(distanceRight <= distanceLeft && distanceRight <= distanceUp && distanceRight <= distanceDown){
            closest.setX(x + 1); closest.setY(y);
        }
        // check left distance
        else if(distanceLeft <= distanceRight && distanceLeft <= distanceUp && distanceLeft <= distanceDown){
            closest.setX(x - 1); closest.setY(y);
        }
        // check up distance
        else if(distanceUp <= distanceLeft && distanceUp <= distanceRight && distanceUp <= distanceDown){
            closest.setX(x); closest.setY(y - 1);
        }
        // check down distance
        else if(distanceDown <= distanceLeft && distanceDown <= distanceUp && distanceDown <= distanceRight){
            closest.setX(x); closest.setY(y + 1);
        }

        return closest;
    }


    /**
     * Processes a point and adds it to the path if it is good
     */
    private void processPoint(int x, int y){
        int distance = grid[x][y];

        path.add(new Point(x, y));

        if(distance == 0){
            return;
        }

        // check right point
        if((x + 1 >= 0 && x + 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x + 1][y] < distance && grid[x + 1][y] != GAME_OBJECT_OBSTACLE){
            processPoint(x + 1, y);
        }

        // check left point
        else if((x - 1 >= 0 && x - 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x - 1][y] < distance && grid[x - 1][y] != GAME_OBJECT_OBSTACLE){
            processPoint(x - 1, y);
        }

        // check bottom point
        else if((x >= 0 && x < gridWidth) && (y + 1 >= 0 && y + 1 < gridHeight) && grid[x][y + 1] < distance && grid[x][y + 1] != GAME_OBJECT_OBSTACLE){
            processPoint(x, y + 1);
        }

        // check top point
        else if((x >= 0 && x < gridWidth) && (y - 1 >= 0 && y - 1 < gridHeight) && grid[x][y - 1] < distance && grid[x][y - 1] != GAME_OBJECT_OBSTACLE){
            processPoint(x, y - 1);
        }
    }


    /**
     * Moves to points on the game grid and sets the distance(used when finding path to food)
     * @param points list of points to move to
     * @param newPoints list of new points that will be used when making a recursive call
     * @param distance distance to set to the points
     * @param food the food cell
     */
    private void moveToPoints(List<Point> points, List<Point> newPoints, int distance, Cell food){
        if(points.size() == 0){
            return;
        }

        for(int i = 0; i < points.size(); i++){
            int x = points.get(i).getX();
            int y = points.get(i).getY();

            grid[x][y] = distance;

            // check right point
            if((x + 1 >= 0 && x + 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x + 1][y] == 0 && (x + 1 != food.getX() || y != food.getY())){
                Point point = new Point(x + 1, y);

                if(!newPoints.contains(point)) {
                    newPoints.add(point);
                }
            }

            // check left point
            if((x - 1 >= 0 && x - 1 < gridWidth) && (y >= 0 && y < gridHeight) && grid[x - 1][y] == 0 && (x - 1 != food.getX() || y != food.getY())){
                Point point = new Point(x - 1, y);

                if(!newPoints.contains(point)) {
                    newPoints.add(point);
                }
            }

            // check bottom point
            if((x >= 0 && x < gridWidth) && (y + 1 >= 0 && y + 1 < gridHeight) && grid[x][y + 1] == 0 && (x != food.getX() || y + 1 != food.getY())){
                Point point = new Point(x, y + 1);

                if(!newPoints.contains(point)) {
                    newPoints.add(point);
                }
            }

            // check top point
            if((x >= 0 && x < gridWidth) && (y - 1 >= 0 && y - 1 < gridHeight) && grid[x][y - 1] == 0 && (x != food.getX() || y - 1 != food.getY())){
                Point point = new Point(x, y - 1);

                if(!newPoints.contains(point)) {
                    newPoints.add(point);
                }
            }
        }

        points.clear();

        moveToPoints(newPoints, points, distance + 1, food);
    }


    /**
     * Updates the bot state
     */
    public void update(Cell[] obstacles, Snake player, Cell food){
        updateGrid(player);
        updatePath(obstacles, player, food);

        if(path.size() > 0){
            followPath();
        }
    }


    /**
     * Makes the bot follow the path to the food
     */
    private void followPath(){
        Point point = path.get(0);

        if(point.getY() == snake.getHeadY()){
            if(point.getX() > snake.getHeadX()){
                snake.moveRight();
                path.remove(0);
            }else if(point.getX() < snake.getHeadX()){
                snake.moveLeft();
                path.remove(0);
            }
        }else if(point.getX() == snake.getHeadX()){
            if(point.getY() > snake.getHeadY()){
                snake.moveDown();
                path.remove(0);
            }else if(point.getY() < snake.getHeadY()){
                snake.moveUp();
                path.remove(0);
            }
        }
    }


    // ******** SETTERS ******** //
    public void setSnake(Snake snake){
        this.snake = snake;
        this.snake.makeAbleToTurn();
    }
}
