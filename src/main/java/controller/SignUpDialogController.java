package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.PlayerInfoDAO;
import model.players.PlayerInfo;

import java.text.ParseException;
import java.util.Optional;

public class SignUpDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nickField;

    @FXML
    private TextField passwordField;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleSignUpAction(ActionEvent event) throws ParseException {
        PlayerInfoDAO playerInfoDAO = new PlayerInfoDAO();
        Optional<PlayerInfo> playerInfo = playerInfoDAO.findByNick(nickField.getText());
        if (playerInfo.isPresent()) {
            dialogStage.setTitle("User with your nick exsist");
            nickField.setText("");
        } else {
            if (!nameField.getText().isBlank() && !nickField.getText().isBlank() && !passwordField.getText().isBlank()) {
                playerInfoDAO.create(nameField.getText(), surnameField.getText(), nickField.getText(),
                        emailField.getText(), countryField.getText(), passwordField.getText());
                dialogStage.close();
            }
        }
    }

}
