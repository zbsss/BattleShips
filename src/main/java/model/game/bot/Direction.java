package model.game.bot;

import model.game.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum Direction {
    UP, DOWN, RIGHT, LEFT;

    private static Random random = new Random();
    private static Map<Direction, Position> vectors = new HashMap<>();


    static {
        vectors.put(UP, new Position(-1, 0));
        vectors.put(DOWN, new Position(1, 0));
        vectors.put(LEFT, new Position(0, -1));
        vectors.put(RIGHT, new Position(0, 1));
    }

    public boolean isVertical(){
        return this == UP || this == DOWN;
    }

    public Position toVector(){
        return vectors.get(this);
    }

    public Direction opposite(){
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

}
