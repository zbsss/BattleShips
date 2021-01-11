package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.PlayerInfoDAO;
import model.data.PlayerInfo;
import service.EmailService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public class SignUpDialogController {
    @FXML
    Button signUpButton;

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
    public void initialize() {
        signUpButton.disableProperty().bind(Bindings.or(
                Bindings.or(nickField.textProperty().isEmpty(), passwordField.textProperty().isEmpty()),
                Bindings.or(nameField.textProperty().isEmpty(), emailField.textProperty().isEmpty()))
        );
    }


    @FXML
    private void handleSignUpAction(ActionEvent event) throws ParseException, IOException {
        if (dialogStage == null) {
            Button button = (Button) event.getSource();
            dialogStage = (Stage) button.getScene().getWindow();
        }
        PlayerInfoDAO playerInfoDAO = new PlayerInfoDAO();
        Optional<PlayerInfo> playerInfo = playerInfoDAO.findByNick(nickField.getText());
        if (playerInfo.isPresent()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Username already exists");
            error.showAndWait();
            nickField.setText("");
        } else {
            if (!nameField.getText().isBlank() && !nickField.getText().isBlank() && !passwordField.getText().isBlank()) {
                playerInfoDAO.create(nameField.getText(), surnameField.getText(), nickField.getText(),
                        emailField.getText(), countryField.getText(), passwordField.getText());

                // send email notification
                (new EmailService()).registerNotification((new PlayerInfoDAO()).findByNick(nickField.getText()).get());

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setContentText("Account created");
                confirm.showAndWait();
                switchView(event);
            }
        }
    }

    @FXML
    private void handleLogInAction(ActionEvent actionEvent) throws IOException {
        switchView(actionEvent);
    }

    private void switchView(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LogInDialog.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
