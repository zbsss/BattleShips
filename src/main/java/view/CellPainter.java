package view;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.statuses.CellStatus;

public class CellPainter {

    public final static Paint BOARD_PAINT = Paint.valueOf("aquamarine");
    public final static Paint SHIP_PAINT = Paint.valueOf("orange");
    public final static Paint ATTACK_PAINT = Paint.valueOf("black");
    public final static Paint HIT_PAINT = Paint.valueOf("magenta");
    public final static Paint DESTROYED_PAINT = Paint.valueOf("red");
    public final static Paint MISS_PAINT = Paint.valueOf("green");


    public static void updateColor(CellStatus status, Rectangle rectangle) {
        switch(status) {
            case HIT -> {
                rectangle.setFill(HIT_PAINT);
                rectangle.setOnMouseClicked(null);
            }
            case SHIP -> rectangle.setFill(SHIP_PAINT);
            case WATER -> rectangle.setFill(BOARD_PAINT);
            case DESTROYED -> rectangle.setFill(DESTROYED_PAINT);
            case MISS -> rectangle.setFill(MISS_PAINT);
        }
    }

    public static void updateColor(Paint color, Rectangle rectangle) {
        rectangle.setFill(color);
    }

}
