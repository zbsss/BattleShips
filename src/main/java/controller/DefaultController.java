package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.data.PlayerInfo;

public class DefaultController {

    @FXML
    ListView playersList;

    PlayerInfo player;

    public void initialize() {

    }

    public void handleLogOut(ActionEvent actionEvent) {
    }

    public void handlePlay(ActionEvent actionEvent) {
    }

    public void setPlayer(PlayerInfo player) {
        this.player = player;
    }
}
