package view;

import javafx.scene.layout.GridPane;
import model.game.Cell;
import model.statuses.CellStatus;

public class SecretBattleshipCellObserver extends BattleshipCellObserver {
    public SecretBattleshipCellObserver(GridPane pane) {
        super(pane);
    }

    @Override
    public void update(Cell newCell) {
        if (newCell.getStatus() != CellStatus.SHIP) {
            super.update(newCell);
        }
    }
}
