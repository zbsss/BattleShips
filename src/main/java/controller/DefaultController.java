package controller;


import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.dao.PlayerStatisticDAO;
import model.data.PlayerInfo;
import model.data.PlayerStatistics;
import model.statuses.Difficulty;
import view.BattleshipCellObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class DefaultController {

    @FXML
    TableView<PlayerStatistics> playersList;

    PlayerInfo player;

    public void initialize() {



    }

    public void handleLogOut(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/LoginDialog.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handlePlay(ActionEvent actionEvent) {
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

    public void setPlayer(PlayerInfo player) {
        this.player = player;

        TableColumn<PlayerStatistics, PlayerInfo> nickColumn = new TableColumn<>();
        TableColumn<PlayerStatistics, Integer> winsColumn = new TableColumn<>();
        TableColumn<PlayerStatistics, Integer> losesColumn = new TableColumn<>();
        nickColumn.setCellValueFactory(cellData -> cellData.getValue().playerProperty());
        winsColumn.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());
        losesColumn.setCellValueFactory(cellData -> cellData.getValue().losesProperty().asObject());
        nickColumn.setText("Nick");
        winsColumn.setText("Wins");
        losesColumn.setText("Loses");

        nickColumn.setCellFactory(column -> {
            return new TableCell<PlayerStatistics, PlayerInfo>() {
                @Override
                protected void updateItem(PlayerInfo playerInfo, boolean empty){
                    super.updateItem(playerInfo, empty);
                    if (playerInfo != null && !empty) {
                        setText(playerInfo.getNickName());
                        if (playerInfo.getNickName().equals(player.getNickName()))
                            setStyle("-fx-font-weight: bold");
                    }
                }
            };
        });

        nickColumn.setSortable(false);
        winsColumn.setSortable(false);
        losesColumn.setSortable(false);

        playersList.getColumns().addAll(Arrays.asList(nickColumn, winsColumn, losesColumn));


        ObservableList<PlayerStatistics> topPlayers = FXCollections.observableArrayList(new PlayerStatisticDAO().getStatisticsAllPlayers().orElse(new ArrayList<>()));
        this.playersList.setItems(topPlayers);
    }
}
