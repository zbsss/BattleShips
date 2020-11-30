package controller;


import com.sun.javafx.scene.ImageViewHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BattleshipGameController {

    private final static Paint BOARD_PAINT = Paint.valueOf("aquamarine");
    private final static Paint SHIP_PAINT = Paint.valueOf("orange");

    @FXML
    BorderPane borderPane;

    @FXML
    VBox playerBoardArea;

    @FXML
    GridPane playerBoard;

    @FXML
    GridPane enemyBoard;

    @FXML
    HBox fourByOne;

    @FXML
    HBox threeByOneA;

    @FXML
    HBox threeByOneB;

    @FXML
    HBox twoByOneA;

    @FXML
    HBox twoByOneB;

    @FXML
    HBox twoByOneC;

    @FXML
    HBox oneByOneA;

    @FXML
    HBox oneByOneB;

    @FXML
    HBox oneByOneC;

    @FXML
    HBox oneByOneD;

    int length;
    Rectangle dragSource;


    //TODO ugly
    Map<List<Integer>, Rectangle> coordsToPlayerBoard = new HashMap<>();
    Map<Rectangle, List<Integer>> playerBoardToCoords = new HashMap<>();
    List<Rectangle> currShip = new LinkedList<>();
    Map<Integer, List<HBox>> possibleShips = new HashMap<>();




    @FXML
    public void initialize() {
        possibleShips.put(4, Collections.singletonList(fourByOne));
        possibleShips.put(3, Arrays.asList(threeByOneA, threeByOneB));
        possibleShips.put(2, Arrays.asList(twoByOneA, twoByOneB, twoByOneC));
        possibleShips.put(1, Arrays.asList(oneByOneA, oneByOneB, oneByOneC, oneByOneD));



        initializeBoards();
        initializeShips();
        initializeCellsListeners();
    }

    private void initializeCellsListeners() {
        borderPane.setOnDragOver(e -> e.acceptTransferModes(TransferMode.ANY));
        borderPane.setOnDragDropped(e -> {
            if (! (e.getTarget() instanceof Rectangle))
                 reject(e);
        });
        playerBoard.getChildren().forEach(c -> {
            if (c instanceof Rectangle) {
                c.setOnMouseClicked(e -> {
                    if (!possibleShips.get(1).isEmpty()) {
                        length = 1;
                        currShip.add((Rectangle) c);
                        dragSource = (Rectangle) c;
                        ((Rectangle) c).setFill(SHIP_PAINT);

                        List<HBox> ships = possibleShips.get(length);
                        HBox ship = ships.get(0);
                        if (ship != null) {
                            ship.setVisible(false);
                        }
                        List<HBox> newShips = ships.stream().filter(s -> s != ship).collect(Collectors.toList());
                        possibleShips.put(length, newShips);
                    }

                });
                c.setOnDragOver(e -> {
                    e.acceptTransferModes(TransferMode.ANY);
                });
                c.setOnDragDropped(e -> {
                    if (!possibleShips.get(length).isEmpty()) {
                        List<HBox> ships = possibleShips.get(length);
                        HBox ship = ships.get(0);
                        if (ship != null) {
                            ship.setVisible(false);
                        }
                        List<HBox> newShips = ships.stream().filter(s -> s != ship).collect(Collectors.toList());
                        possibleShips.put(length, newShips);
                        e.setDropCompleted(true);
                    } else if(possibleShips.get(length).isEmpty()){
                        currShip.forEach(r -> r.setFill(BOARD_PAINT));
                    }
                });
//                c.setOnDrag
                c.setOnDragDetected(e -> {
                    currShip.clear();
                    if (possibleShips.values().stream().anyMatch(l -> !l.isEmpty())) {
                        currShip.add((Rectangle) c);
                        dragSource = (Rectangle) c;
                        Dragboard db = c.startDragAndDrop(TransferMode.ANY);
                        Map<DataFormat, Object> map = new HashMap<>();
                        map.put(DataFormat.PLAIN_TEXT, "1");
                        length = 1;
                        ((Rectangle) c).setFill(SHIP_PAINT);
                        db.setContent(map);
                        e.setDragDetect(true);
                        e.consume();
                    }
                });
                c.setOnDragOver(e -> {
                    if (((Rectangle) c).getY() == dragSource.getY() || ((Rectangle) c).getX() == dragSource.getY());
                    e.acceptTransferModes(TransferMode.ANY);
                });
                c.setOnDragEntered(e -> {
                    int max = possibleShips.keySet().stream().filter(k -> !possibleShips.get(k).isEmpty()).max(Integer::compareTo).orElse(0);

                    if (c != dragSource && (((Rectangle) c).getX() == dragSource.getX() || ((Rectangle) c).getY() == dragSource.getY())
                    && !currShip.contains(c) && (currShip.get(currShip.size() - 1).getX() == ((Rectangle) c).getX() || currShip.get(currShip.size() - 1).getY()==((Rectangle) c).getY())
                    && max > length) {
                        currShip.add((Rectangle) c);
                        length++;
                        Dragboard db = e.getDragboard();
                        Map<DataFormat, Object> map = new HashMap<>();
                        int l = Integer.parseInt((String) db.getContent(DataFormat.PLAIN_TEXT));
                        map.put(DataFormat.PLAIN_TEXT, String.valueOf(l + 1));
                        db.setContent(map);
                        ((Rectangle) c).setFill(SHIP_PAINT);
                    }
                });
            }
        });
    }

    private void reject(DragEvent e) {
        currShip.forEach(r -> r.setFill(BOARD_PAINT));
    }

    private void initializeShips() {
//        initializeShip(fourByOne, "4");
//        initializeShip(threeByOne, "3");
//        initializeShip(twoByOne, "2");
//        initializeShip(oneByOne, "1");
    }

    private void initializeShip(HBox ship, String length) {
//        ship.setOnDragDetected(mouseEvent -> {
//            final ImageView preview = new ImageView(ship.snapshot(null, null));
//            final Dragboard db = ship.startDragAndDrop(TransferMode.ANY);
//            Map<DataFormat, Object> map = new HashMap<>();
//            map.put(DataFormat.PLAIN_TEXT, length + ",h");
//            db.setContent(map);
//            db.setDragView(preview.getImage());
//
//            mouseEvent.setDragDetect(true);
//            mouseEvent.consume();
//        });
    }

    private void initializeBoards() {
        int x = 10;
        int y = 10;


        enemyBoard.setAlignment(Pos.CENTER);

        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        List<Label> playerLabels = new LinkedList<>();
        Arrays.stream(letters).forEach(l ->{
            Label label = new Label(l);
            GridPane.setHalignment(label, HPos.CENTER);
            playerLabels.add(label);
        });

        List<Label> enemyLabels = new LinkedList<>();
        Arrays.stream(letters).forEach(l ->{
            Label label = new Label(l);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            enemyLabels.add(label);
        });

        playerLabels.forEach(l -> playerBoard.add(l, playerLabels.indexOf(l) + 1, 0));
        enemyLabels.forEach(l -> enemyBoard.add(l, enemyLabels.indexOf(l) + 1, 0));

        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        List<Label> playerNumbers = new LinkedList<>();
        Arrays.stream(numbers).forEach(l ->{
            Label label = new Label(l);
            GridPane.setHalignment(label, HPos.CENTER);
            playerNumbers.add(label);
        });

        List<Label> enemyNumbers = new LinkedList<>();
        Arrays.stream(numbers).forEach(l ->{
            Label label = new Label(l);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            enemyNumbers.add(label);
        });

        playerNumbers.forEach(l -> playerBoard.add(l, 0,playerNumbers.indexOf(l) + 1));
        enemyNumbers.forEach(l -> enemyBoard.add(l, 0, enemyNumbers.indexOf(l) + 1));



        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                Rectangle rect1 = new Rectangle(20, 20, BOARD_PAINT);
                Rectangle rect2 = new Rectangle(20, 20, BOARD_PAINT);

                rect1.setX(i);
                rect1.setY(j);

                rect2.onMouseEnteredProperty().set(e -> rect2.setFill(Paint.valueOf("black")));
                rect2.onMouseExitedProperty().set(e -> rect2.setFill(BOARD_PAINT));

                coordsToPlayerBoard.put(Arrays.asList(i, j), rect1);
                playerBoardToCoords.put(rect1, Arrays.asList(i ,j));

                playerBoard.add(rect1, i, j);
                enemyBoard.add(rect2, i, j);
            }
        }
    }

    public void placeShip(ActionEvent actionEvent) {
        Button b = (Button)  actionEvent.getSource();

//        if (b == fourByOneButton)

    }
}
