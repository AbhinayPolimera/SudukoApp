package com.sudoku.SudokuApp.userinterface.controlLogic;

import com.sudoku.SudokuApp.SudokuGame;
import com.sudoku.SudokuApp.constants.GameState;
import com.sudoku.SudokuApp.constants.Messages;
import com.sudoku.SudokuApp.dao.Storage;
import com.sudoku.SudokuApp.gameLogic.GameLogic;
import com.sudoku.SudokuApp.userinterface.UserInterfaceContract;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class ControlLogic implements UserInterfaceContract.EventListener {
    private Storage storage;
    private UserInterfaceContract.View view;

    @Override
    public void onSudokuInput(int x, int y, int input) {
        try {
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();
            newGridState[x][y] = input;

            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState
            );
            storage.updateGameData(gameData);
            view.updateSquare(x, y, input);
            if (gameData.getGameState() == GameState.COMPLETE) view.showDialog(Messages.GAME_COMPLETE);
        } catch (IOException e) {
            e.printStackTrace();
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        try {
            storage.updateGameData(
                    GameLogic.getNewGame()
            );
            view.updateBoard(storage.getGameData());
        } catch (IOException e) {
            view.showError(Messages.ERROR);
        }
    }
}
