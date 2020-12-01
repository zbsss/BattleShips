package controller;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.game.Game;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BattleshipGameController {

    private final static Paint BOARD_PAINT = Paint.valueOf("aquamarine");
    private final static Paint SHIP_PAINT = Paint.valueOf("orange");
    private final static Paint ATTACK_PAINT = Paint.valueOf("black");

    private final static int BOARD_WIDTH = 10;
    private final static int BOARD_HEIGHT = 10;

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


    /**
     * Attributes necessary for control of drag-operation conducted to place ships on the player board
     */
    private final List<Rectangle> currShip = new LinkedList<>();
    private final Map<Integer, List<HBox>> possibleShips = new HashMap<>();




    @FXML
    public void initialize() {
        initializePossibleShips();
        initializeBoards();
        initializeBoardPaneListeners();
        initializeCellsListeners();
    }

    /**
     * initializing listeners to BoardPane. See #{@link #handleBorderPaneDragOver(DragEvent)}
     * and #{@link #handleBorderPaneDragDropped(DragEvent)}
     */
    private void initializeBoardPaneListeners() {
        borderPane.setOnDragOver(e -> handleBorderPaneDragOver(e));
        borderPane.setOnDragDropped(e -> handleBorderPaneDragDropped(e));
    }

    /**
     * Adding possible ships to the board.
     * In the future possibly method will be replaced with getter from #{@link Game} object
     */
    private void initializePossibleShips() {
        possibleShips.put(4, Collections.singletonList(fourByOne));
        possibleShips.put(3, Arrays.asList(threeByOneA, threeByOneB));
        possibleShips.put(2, Arrays.asList(twoByOneA, twoByOneB, twoByOneC));
        possibleShips.put(1, Arrays.asList(oneByOneA, oneByOneB, oneByOneC, oneByOneD));
    }

    /**
     * initializing listeners to all Fields of the playerBoard.
     * In the future there will also be places listeners for the enemy board.
     * See #{@link #handleFieldDragDetected(Rectangle, MouseEvent)}, #{@link #handleFieldDragDropped(DragEvent)},
     * #{@link #handleFieldDragOver(DragEvent)}, #{@link #handleFieldDragEntered(Rectangle, DragEvent)},
     * #{@link #handleFieldMouseClicked(Rectangle, MouseEvent)}
     */
    private void initializeCellsListeners() {
        playerBoard.getChildren().forEach(c -> {
            if (c instanceof Rectangle) {
                c.setOnMouseClicked(e -> handleFieldMouseClicked((Rectangle) c, e));
                c.setOnDragDropped(e -> handleFieldDragDropped(e));
                c.setOnDragDetected(e -> handleFieldDragDetected((Rectangle) c, e));
                c.setOnDragOver(e -> handleFieldDragOver(e));
                c.setOnDragEntered(e -> handleFieldDragEntered((Rectangle) c, e));
            }
        });
    }

    private void handleBorderPaneDragOver(DragEvent e) {
        e.acceptTransferModes(TransferMode.ANY);
    }

    /**
     * See #{@link #reject()}
     * @param e DragEvent dropped on the field
     */
    private void handleBorderPaneDragDropped(DragEvent e) {
        if (! (e.getTarget() instanceof Rectangle))
            reject();
    }

    /**
     * Method Adds one by one ship on mouse click so user doesn't have to drag mouse over single field
     * @param rectangle board field where the user wants to place the ship
     * @param e MouseEvent object
     * See #{@link #removeFromPossibleShips()} and #{@link #canPlaceShip(int)}
     */
    private void handleFieldMouseClicked(Rectangle rectangle, MouseEvent e) {
        if (canPlaceShip(1)) {
            length = 1;
            currShip.add(rectangle);
            dragSource = rectangle;
            rectangle.setFill(SHIP_PAINT);

            removeFromPossibleShips();
        }
    }

    /**
     * Method removes ship of the length equal to current count of "colored" fields from the possibleShips map
     * and visually represents it by making corresponding object invisible
     */
    private void removeFromPossibleShips() {
        List<HBox> ships = possibleShips.get(length);
        HBox ship = ships.get(0);
        if (ship != null) {
            ship.setVisible(false);
        }
        List<HBox> newShips = ships.stream().filter(s -> s != ship).collect(Collectors.toList());
        possibleShips.put(length, newShips);
    }

    /**
     * handler checks if there is possibility to place a ship and removes it from the possibleShips
     * or rejects current "dragging"
     * @param e DragEvent
     * See #{@link #canPlaceShip(int)} and #{@link #reject()}
     */
    private void handleFieldDragDropped(DragEvent e) {
        if (canPlaceShip(length)) {
            removeFromPossibleShips();
            e.setDropCompleted(true);
        } else if(possibleShips.get(length).isEmpty()){
            reject();
        }
    }

    /**
     * Method begins procedure of creating new ship Placement on the board
     * @param rectangle field where the user has started dragging
     * @param e MouseEvent
     */
        private void handleFieldDragDetected(Rectangle rectangle, MouseEvent e) {
        currShip.clear();
        if (possibleShips.values().stream().anyMatch(l -> !l.isEmpty())) {
            currShip.add(rectangle);
            dragSource = rectangle;
            Dragboard db = rectangle.startDragAndDrop(TransferMode.ANY);
            Map<DataFormat, Object> map = new HashMap<>();
            map.put(DataFormat.PLAIN_TEXT, String.valueOf(rectangle.getX()) + String.valueOf(rectangle.getY()));
            length = 1;
            rectangle.setFill(SHIP_PAINT);
            db.setContent(map);
            e.setDragDetect(true);
            e.consume();
        }
    }

    /**
     * Method accepts transfer mode to make creating ship possible.
     * It is vital for the method #{@link #handleFieldDragDropped(DragEvent)} to work properly.
     * @param e
     */
    private void handleFieldDragOver(DragEvent e) {
        e.acceptTransferModes(TransferMode.ANY);
    }

    /**
     * Method includes the rectangle param in the new ship
     * @param rectangle field where the user wants to include in new ship
     * @param e DragEvent
     * See #{@link #canShipBeIncluded(Rectangle, int)}
     */
    private void handleFieldDragEntered(Rectangle rectangle, DragEvent e) {
        int max = getMaxShipLength();
        if (canShipBeIncluded(rectangle, max)) {
            currShip.add(rectangle);
            length++;
            rectangle.setFill(SHIP_PAINT);
        }
    }

    /**
     * Methods checks whether whether the rectangle param can be part of new ship
     * @param rectangle field where user wants to include in new ship
     * @param max maximum currently available ship length
     * Method likely to be refactored as further mechanics are implemented
     */
    private boolean canShipBeIncluded(Rectangle rectangle, int max) {
        return rectangle != dragSource && (rectangle.getX() == dragSource.getX() || (rectangle.getY() == dragSource.getY())
                && !currShip.contains(rectangle) && (currShip.get(currShip.size() - 1).getX() == rectangle.getX() || currShip.get(currShip.size() - 1).getY()==(rectangle.getY())))
                && max > length;
    }


    /**
     * @return maximum currently available ship length
     */
    private Integer getMaxShipLength() {
        return possibleShips.keySet().stream().filter(k -> !possibleShips.get(k).isEmpty()).max(Integer::compareTo).orElse(0);
    }

    /**
     * Method rejects attempt to create ship. Likely to be refactored as further mechanics are implemented
     */
    private void reject() {
        currShip.forEach(r -> r.setFill(BOARD_PAINT));
    }

    /**
     * @return List of letter labels placed on top of the boards
     * See #{@link #getStringConsumer(List)}
     */
    private List<Label> initializeBoardLetterLabels() {
        List<Label> resultList = new LinkedList<>();
        String[] letters = new String[BOARD_WIDTH];
        char c = 'A';
        for (int i = 0; i < BOARD_WIDTH; i++) {
            letters[i] = String.valueOf(c++);
        }
        Arrays.stream(letters).forEach(getStringConsumer(resultList));

        return resultList;
    }

    /**
     * @return List of number labels placed on left side of the boards
     * See #{@link #getStringConsumer(List)}
     */
    private List<Label> initializeBoardNumberLabels() {
        List<Label> resultList = new LinkedList<>();
        String[] letters = new String[BOARD_WIDTH];
        int c = 1;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            letters[i] = String.valueOf(c++);
        }
        Arrays.stream(letters).forEach(getStringConsumer(resultList));
        return resultList;
    }

    /**
     * Method adds labels into result List depending on label text
     * @param resultList list of labels to add labels into
     */
    private Consumer<String> getStringConsumer(List<Label> resultList) {
        return l -> {
            Label label = new Label(l);
            GridPane.setHalignment(label, HPos.CENTER);
            resultList.add(label);
        };
    }

    /**
     * Method adds labels to the boards and fills them with rectangles/fields/cells
     * See #{@link #initializeBoardLetterLabels()}, #{@link #initializeBoardNumberLabels()}, #{@link #fillBoards()}
     */
    private void initializeBoards() {

        enemyBoard.setAlignment(Pos.CENTER);

        List<Label> playerLabels = initializeBoardLetterLabels();
        List<Label> enemyLabels = initializeBoardLetterLabels();

        playerLabels.forEach(l -> playerBoard.add(l, playerLabels.indexOf(l) + 1, 0));
        enemyLabels.forEach(l -> enemyBoard.add(l, enemyLabels.indexOf(l) + 1, 0));

        List<Label> playerNumbers = initializeBoardNumberLabels();
        List<Label> enemyNumbers = initializeBoardNumberLabels();

        playerNumbers.forEach(l -> playerBoard.add(l, 0,playerNumbers.indexOf(l) + 1));
        enemyNumbers.forEach(l -> enemyBoard.add(l, 0, enemyNumbers.indexOf(l) + 1));


        fillBoards();
    }

    private void fillBoards() {
        for (int i = 1; i <= BOARD_WIDTH; i++) {
            for (int j = 1; j <= BOARD_HEIGHT; j++) {
                Rectangle rect1 = new Rectangle(20, 20, BOARD_PAINT);
                Rectangle rect2 = new Rectangle(20, 20, BOARD_PAINT);

                rect1.setX(i);
                rect1.setY(j);

                rect2.onMouseEnteredProperty().set(e -> rect2.setFill(ATTACK_PAINT));
                rect2.onMouseExitedProperty().set(e -> rect2.setFill(BOARD_PAINT));

                playerBoard.add(rect1, i, j);
                enemyBoard.add(rect2, i, j);
            }
        }
    }

    /**
     * Method returns boolean value if there is possibility to place a ship of specific length on specific place
     * Potentially method will be refactored to receive more parameters determinig the outcome
     * as the further mechanics will be implemented
     * @param i length of ship
     *
     */
    private boolean canPlaceShip(int i) {
        return !possibleShips.get(i).isEmpty();
    }


}
