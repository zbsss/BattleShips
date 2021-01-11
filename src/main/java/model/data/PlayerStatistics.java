package model.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PlayerStatistics {
    private ObjectProperty<PlayerInfo> player;
    private IntegerProperty wins;
    private IntegerProperty loses;

    public PlayerStatistics(PlayerInfo player, int wins, int loses) {
        this.player = new SimpleObjectProperty<>(player);
        this.wins = new SimpleIntegerProperty(wins);
        this.loses = new SimpleIntegerProperty(loses);
    }

    public PlayerInfo getPlayer() {
        return player.get();
    }

    public long getWins() {
        return wins.get();
    }

    public long getLoses() {
        return loses.get();
    }

    public ObjectProperty<PlayerInfo> playerProperty() {
        return player;
    }

    public IntegerProperty winsProperty() {
        return wins;
    }

    public IntegerProperty losesProperty() {
        return loses;
    }
}
