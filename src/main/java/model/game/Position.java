package model.game;


public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position add(Position other){
        return new Position(this.getX() + other.getX(), this.getY() + other.getY());
    }
}
