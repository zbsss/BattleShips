package view;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import model.game.Cell;
import model.game.CellObserver;


public class BattleshipCellObserver implements CellObserver {
    GridPane playerPane;

    public BattleshipCellObserver(GridPane pane) {
        this.playerPane = pane;
    }

    @Override
    public void update(Cell newCell) {
        playerPane.getChildren()
                .stream()
                .filter(c ->
                        c instanceof Rectangle
                                && ((Rectangle) c).getX() - 1 == newCell.getPosition().getX()
                                && ((Rectangle) c).getY() - 1 == newCell.getPosition().getY())
                .findAny().ifPresent(rect -> CellPainter.updateColor(newCell.getStatus(), (Rectangle) rect));
    }


}
