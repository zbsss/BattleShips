import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import service.SessionService;

import java.io.IOException;

public class BattleshipApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            SessionService.openSession();
            var loader = new FXMLLoader();
            loader.setLocation(BattleshipApp.class.getResource("view/battleships.fxml"));
            BorderPane rootLayout = loader.load();
            primaryStage.setMaxHeight(750);
            primaryStage.setMaxWidth(1000);
            configureStage(primaryStage, rootLayout);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureStage(Stage primaryStage, BorderPane rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships");
        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }
}
