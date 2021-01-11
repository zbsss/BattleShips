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

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public class LoginDialogController {
    BattleshipGameController battleshipGameController;

    @FXML
    Button logInButton;

    @FXML
    private TextField nickField;

    @FXML
    private TextField passwordField;

    private Stage dialogStage;

    @FXML
    public void initialize() {
        logInButton.disableProperty().bind(Bindings.or(nickField.textProperty().isEmpty(), passwordField.textProperty().isEmpty()));

    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBattleshipGameController(BattleshipGameController battleshipGameController) {
        this.battleshipGameController = battleshipGameController;
    }

    @FXML
    private void handleLogInAction(ActionEvent event) throws ParseException, IOException {
        if (dialogStage == null) {
            Button button = (Button) event.getSource();
            dialogStage = (Stage) button.getScene().getWindow();
        }
        PlayerInfoDAO playerInfoDAO = new PlayerInfoDAO();
        Optional<PlayerInfo> playerInfo = playerInfoDAO.findByNick(nickField.getText());
        if (playerInfo.isPresent() && playerInfo.get().getPassword().equals(passwordField.getText())){
            switchView(event, playerInfo.get());
        }
        else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Log in failed, perhaps password is incorrect?");
            error.showAndWait();
        }

        passwordField.setText("");

    }


    public void initRootLayout() {
    }

    public void handleSignUpAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SignUpDialog.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void switchView(ActionEvent actionEvent, PlayerInfo player) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/default.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        DefaultController controller = loader.getController();
        controller.setPlayer(player);

        stage.show();
    }
}
