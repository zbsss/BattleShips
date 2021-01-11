package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.dao.PlayerInfoDAO;
import model.data.GameResult;
import model.data.PlayerInfo;
import model.statuses.Difficulty;
import model.statuses.Result;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public class EndGameController {
    @FXML
    public Label endMessage;

    private PlayerInfo player;

    @FXML
    public void initialize() {
    }
    @FXML
    private void handleLogOut(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LogInDialog.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void handleRanking(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/default.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        DefaultController controller = loader.getController();
        controller.setPlayer(player);

        stage.setScene(scene);
    }

    @FXML
    private void handlePlay(ActionEvent actionEvent) throws IOException {
        try {

            Alert difficulty = new Alert(Alert.AlertType.CONFIRMATION);
            difficulty.setTitle("Choose difficulty");
            difficulty.setHeaderText("Choose the difficulty of the opponent");
            difficulty.setContentText("Choose your option");

            ButtonType easyGame = new ButtonType("Easy");
            ButtonType hardGame = new ButtonType("Hard");
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            difficulty.getButtonTypes().setAll(easyGame, hardGame, cancel);
            Optional<ButtonType> result = difficulty.showAndWait();
            if (result.get() != cancel) {
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/battleships.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);

                BattleshipGameController controller = loader.getController();
                if (result.get() == easyGame)
                    controller.setPlayer(player, Difficulty.EASY);
                else if (result.get() == hardGame)
                    controller.setPlayer(player, Difficulty.HARD);

                stage.show();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }



    public void setResult(Result result, PlayerInfo playerInfo) {
        player = playerInfo;
        if (result == Result.WON)
            endMessage.setText("You have won!");
        else if (result == Result.LOST)
            endMessage.setText("You have lost!");
        else
            endMessage.setText("The game is over!");
    }


}
