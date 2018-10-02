package GiedriusKristinaitis;

public class Point {

    // coordinates of the point
    private int x;
    private int y;


    /**
     * Class constructor
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }


    /**
     * Checks if this point is equal to another object
     * @param other object to check for equality
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other == null) { return false; }
        if(!(other instanceof Point)) { return false; }

        Point point = (Point) other;

        return point.getX() == x && point.getY() == y;
    }


    /**
     * Gets the hash code of this point instance
     * @return hash code
     */
    @Override
    public int hashCode(){
        return new Integer(x).hashCode() + new Integer(y).hashCode();
    }


    // ******** GETTERS ******** //
    public int getX() { return x; }
    public int getY() { return y; }


    // ******** SETTERS ******** //
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
