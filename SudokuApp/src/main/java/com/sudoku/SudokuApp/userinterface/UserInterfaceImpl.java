package com.sudoku.SudokuApp.userinterface;

import com.sudoku.SudokuApp.Coordinates;
import com.sudoku.SudokuApp.SudokuGame;
import com.sudoku.SudokuApp.constants.GameState;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.stream.IntStream;

public class UserInterfaceImpl implements UserInterfaceContract.View, EventHandler<KeyEvent> {

    private final Stage stage;
    private final Group root;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;
    private UserInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224, 242, 241);
    private static final String SUDOKU = "SUDOKU";

    @Override
    public void setListener(UserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }
    public void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    private void drawTextFields(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;
        final int xYDelta = 64;

        IntStream.range(0, 9)
                .forEach(xIndex -> IntStream.range(0, 9)
                        .forEach(yIndex -> {
                            int x = xOrigin + (xIndex * xYDelta);
                            int y = yOrigin + (yIndex * xYDelta);
                            SudokuTextField tile = new SudokuTextField(xIndex, yIndex);
                            styleSudokuTile(tile, x, y);
                            tile.setOnKeyPressed(this);
                            textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);
                            root.getChildren().add(tile);
                        })
                );
    }

    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }

    private void drawGridLines(Group root) {
        int xAndY = 114;

        IntStream.range(0, 8)
                .forEach(index -> {
                    int thickness = (index == 2 || index == 5) ? 3 : 2;
                    Rectangle verticalLine = getLine(
                            xAndY + 64 * index,
                            BOARD_PADDING,
                            BOARD_X_AND_Y,
                            thickness
                    );
                    Rectangle horizontalLine = getLine(
                            BOARD_PADDING,
                            xAndY + 64 * index,
                            thickness,
                            BOARD_X_AND_Y
                    );
                    root.getChildren().addAll(
                            verticalLine,
                            horizontalLine
                    );
                });
    }

    public Rectangle getLine(double x, double y, double height, double width){
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;

    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().add(boardBackground);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(input);
        value = (value.equals("0")) ? "" : value;
        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        int[][] gridState = game.getCopyOfGridState();
        GameState gameState = game.getGameState();

        textFieldCoordinates.forEach((coordinates, tile) -> {
            int xIndex = coordinates.getX();
            int yIndex = coordinates.getY();
            String value = Integer.toString(gridState[xIndex][yIndex]);
            value = (value.equals("0")) ? "" : value;
            tile.setText(value);
            if (gameState == GameState.NEW) {
                tile.setStyle(value.equals("") ? "-fx-opacity: 1;" : "-fx-opacity: 0.8;");
                tile.setDisable(!value.equals(""));
            }
        });
    }

    @Override
    public void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        error.showAndWait();
    }


    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            String inputText = event.getText();
            if (inputText.matches("[0-9]")) {
                int value = Integer.parseInt(inputText);
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume();
    }

    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }

}
