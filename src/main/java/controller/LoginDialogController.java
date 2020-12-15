package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.PlayerInfoDAO;
import model.players.PlayerInfo;

import java.text.ParseException;
import java.util.Optional;

public class LoginDialogController {

    @FXML
    private TextField nickField;

    @FXML
    private TextField passwordField;

    private Stage dialogStage;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @FXML
    private void handleLogInAction(ActionEvent event) throws ParseException {
        PlayerInfoDAO playerInfoDAO = new PlayerInfoDAO();
        Optional<PlayerInfo> playerInfo = playerInfoDAO.findByNick(nickField.getText());
        if (playerInfo.isPresent() && playerInfo.get().getPassword().equals(passwordField.getText())){
            dialogStage.close();
        }
        dialogStage.setTitle("Password is incorrect");
        passwordField.setText("");
    }


}
