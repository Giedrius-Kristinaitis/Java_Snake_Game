package GiedriusKristinaitis;

import studijosKTU.ScreenKTU;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Game extends ScreenKTU implements FocusListener {

    // title of the application's window
    private static final String APP_TITLE = "Le Snake";

    // some constant numeric values
    private static final int CELL_DIMENSIONS = 25;
    public static final int SCREEN_WIDTH = 25;
    public static final int SCREEN_HEIGHT = 26;
    private static final long GAME_UPDATE_INTERVAL = 25;

    // button information
    private static final String BUTTON_TEXT = "PLAY";
    private int button_width;
    private int button_x;
    private int button_y;

    // flag indicating whether the game is running or not
    private boolean gameRunning = false;

    // flag indicating whether the game is paused or not
    private boolean gamePaused = false;

    // game objects
    private Snake snake;    // snake controlled by the player
    private Snake botSnake; // snake controlled by the computer
    private Cell food;
    private Cell[] obstacles;
    private Bot bot;

    // other required objects
    private final Random random = new Random();

    // score
    private int score;

    // food numeric values
    private int foodTillSpecial = 5;
    private int foodScoreIncrease = 1;


    /**
     * Default class constructor
     */
    public Game(){
        super(CELL_DIMENSIONS, CELL_DIMENSIONS, SCREEN_HEIGHT, SCREEN_WIDTH);

        initialize();
    }


    /**
     * Sets initial values and draws main screen
     */
    private void initialize(){
        setTitle(APP_TITLE);

        setBackColor(Color.ORANGE);
        fillRect(0, 0, SCREEN_HEIGHT, SCREEN_WIDTH);

        setBackColor(Color.BLACK);
        setFontColor(Color.WHITE);

        button_x = SCREEN_WIDTH / 2 - BUTTON_TEXT.length() / 2;
        button_y = SCREEN_HEIGHT / 2;
        button_width = BUTTON_TEXT.length();

        print(button_y, button_x, "PLAY");

        refresh();

        addFocusListener(this);
    }

    /**
     * Called when the application's thread is started
     */
    @Override
    public void run(){
        snake = new Snake(Color.GREEN, Color.ORANGE, 0, 0, 1, 0);
        botSnake = new Snake(Color.CYAN, Color.GRAY, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 2, -1, 0);
        bot = new Bot(SCREEN_WIDTH, SCREEN_HEIGHT - 1, botSnake);

        initializeObstacles();
        bot.initializeGrid(obstacles);

        generateFood(Color.BLUE);

        while(gameRunning){
            if(!gamePaused) {
                drawFrame();
                update();
            }

            try{
                Thread.sleep(GAME_UPDATE_INTERVAL);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }


    /**
     * Initializes obstacles
     */
    private void initializeObstacles(){
        obstacles = new Cell[29];
        obstacles[0] = new Cell(1, 1, Color.RED);
        obstacles[1] = new Cell(2, 1, Color.RED);
        obstacles[2] = new Cell(3, 1, Color.RED);
        obstacles[3] = new Cell(1, 2, Color.RED);
        obstacles[4] = new Cell(1, 3, Color.RED);

        obstacles[5] = new Cell(1, SCREEN_HEIGHT - 3, Color.RED);
        obstacles[6] = new Cell(1, SCREEN_HEIGHT - 4, Color.RED);
        obstacles[7] = new Cell(1, SCREEN_HEIGHT - 5, Color.RED);
        obstacles[8] = new Cell(2, SCREEN_HEIGHT - 3, Color.RED);
        obstacles[9] = new Cell(3, SCREEN_HEIGHT - 3, Color.RED);

        obstacles[10] = new Cell(SCREEN_WIDTH - 2, 1, Color.RED);
        obstacles[11] = new Cell(SCREEN_WIDTH - 3, 1, Color.RED);
        obstacles[12] = new Cell(SCREEN_WIDTH - 4, 1, Color.RED);
        obstacles[13] = new Cell(SCREEN_WIDTH - 2, 2, Color.RED);
        obstacles[14] = new Cell(SCREEN_WIDTH - 2, 3, Color.RED);

        obstacles[15] = new Cell(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 3, Color.RED);
        obstacles[16] = new Cell(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 4, Color.RED);
        obstacles[17] = new Cell(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 5, Color.RED);
        obstacles[18] = new Cell(SCREEN_WIDTH - 3, SCREEN_HEIGHT - 3, Color.RED);
        obstacles[19] = new Cell(SCREEN_WIDTH - 4, SCREEN_HEIGHT - 3, Color.RED);

        obstacles[20] = new Cell(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 1, Color.RED);
        obstacles[21] = new Cell(SCREEN_WIDTH / 2 - 1, SCREEN_HEIGHT / 2 - 1, Color.RED);
        obstacles[22] = new Cell(SCREEN_WIDTH / 2 - 2, SCREEN_HEIGHT / 2 - 1, Color.RED);
        obstacles[23] = new Cell(SCREEN_WIDTH / 2 + 1, SCREEN_HEIGHT / 2 - 1, Color.RED);
        obstacles[24] = new Cell(SCREEN_WIDTH / 2 + 2, SCREEN_HEIGHT / 2 - 1, Color.RED);
        obstacles[25] = new Cell(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 2, Color.RED);
        obstacles[26] = new Cell(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 - 3, Color.RED);
        obstacles[27] = new Cell(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, Color.RED);
        obstacles[28] = new Cell(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 1, Color.RED);
    }


    /**
     * Draws a single frame of the game
     */
    private void drawFrame(){
        // clears the screen
        setBackColor(Color.BLACK);
        fillRect(0, 0, SCREEN_HEIGHT, SCREEN_WIDTH);

        // draw score
        setBackColor(Color.BLUE);
        fillRect(SCREEN_HEIGHT - 1, 0, 1, SCREEN_WIDTH);

        setFontColor(Color.WHITE);
        print(SCREEN_HEIGHT - 1, 0, "Score: " + score);

        // draw the snakes and food
        botSnake.draw(this);
        snake.draw(this);
        food.draw(this);

        // draw obstacles
        for(Cell obstacle: obstacles){
            obstacle.draw(this);
        }

        refresh();
    }


    /**
     * Updates the game state
     */
    private void update(){
        // check if the snake has eaten the food
        if(snake.getHeadX() == food.getX() && snake.getHeadY() == food.getY()){
            snake.grow();
            score += foodScoreIncrease;

            if(foodTillSpecial == 0){
                foodTillSpecial = 5;
                foodScoreIncrease = 2;
                generateFood(Color.WHITE);
            }else {
                foodScoreIncrease = 1;
                foodTillSpecial--;
                generateFood(Color.BLUE);
            }

            bot.updateGrid(snake);
            bot.findPathToFood(obstacles, snake, food);
        }else if(botSnake.getHeadX() == food.getX() && botSnake.getHeadY() == food.getY()){
            botSnake.grow();
            generateFood(Color.MAGENTA);
            bot.updateGrid(snake);
            bot.findPathToFood(obstacles, snake, food);
        }

        // update the snake
        snake.update(this);

        // check for collisions
        checkForCollisions();

        // update the bot
        botSnake.update(this);
        bot.update(obstacles, snake, food);
    }


    /**
     * Checks for collisions
     */
    private void checkForCollisions(){
        for(Cell cell: obstacles){
            if(cell.getX() == snake.getHeadX() && cell.getY() == snake.getHeadY()){
                stopGame();
                break;
            }
        }

        if(snake.isTryingToEatItself() || botSnake.hasCellAt(snake.getHeadX(), snake.getHeadY())){
            stopGame();
        }
    }


    /**
     * Generates food for the snake
     */
    private void generateFood(Color color){
        food = new Cell(color);

        while(true){
            int food_x = random.nextInt(SCREEN_WIDTH);
            int food_y = random.nextInt(SCREEN_HEIGHT - 1);

            if(!snake.hasCellAt(food_x, food_y) && !obstacleExists(food_x, food_y)
                && !botSnake.hasCellAt(food_x, food_y)){
                food.setX(food_x);
                food.setY(food_y);

                break;
            }
        }
    }


    /**
     * Checks if an obstacle exists at the specified location
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     * @return true if an obstacle exists at (x, y), false otherwise
     */
    private boolean obstacleExists(int x, int y){
        for(Cell cell: obstacles){
            if(cell.getX() == x && cell.getY() == y){
                return true;
            }
        }

        return false;
    }


    /**
     * Stops the game
     */
    private void stopGame(){
        gameRunning = false;
        gamePaused = true;

        final String message = "Game Over";

        setBackColor(Color.GRAY);
        setFontColor(Color.WHITE);

        print(5, SCREEN_WIDTH / 2 - message.length() / 2,
                message);

        refresh();
    }


    /**
     * Starts the game
     */
    private void startGame(){
        gameRunning = true;
        new Thread(this).start();
    }


    /**
     * Pauses the game
     */
    private void pauseGame(){
        gamePaused = true;
    }


    /**
     * Resumes the game
     */
    private void resumeGame(){
        gamePaused = false;
    }


    /**
     * Gets called when the window gains focus
     * @param event focus event information
     */
    @Override
    public void focusGained(FocusEvent event){
        resumeGame();
    }


    /**
     * Gets called when the window loses focus
     * @param event focus event information
     */
    @Override
    public void focusLost(FocusEvent event){
        pauseGame();
    }


    /**
     * Gets called when a key on the keyboard is pressed (key down event)
     * @param event key event information
     */
    @Override
    public void keyPressed(KeyEvent event){
        if(!gameRunning){
            return;
        }

        switch(event.getKeyCode()){
            case KeyEvent.VK_DOWN:
                snake.moveDown();
                break;
            case KeyEvent.VK_UP:
                snake.moveUp();
                break;
            case KeyEvent.VK_RIGHT:
                snake.moveRight();
                break;
            case KeyEvent.VK_LEFT:
                snake.moveLeft();
                break;
        }
    }


    /**
     * Gets called when a button on the mouse is pressed (mouse down event)
     * @param event mouse event information
     */
    @Override
    public void mousePressed(MouseEvent event){
        if(gameRunning){
           return;
        }

        int cell_x = event.getX() / CELL_DIMENSIONS;
        int cell_y = event.getY() / CELL_DIMENSIONS;

        if(cell_y == button_y && cell_x >= button_x && cell_x <= button_x + button_width){
            startGame();
        }
    }
}
