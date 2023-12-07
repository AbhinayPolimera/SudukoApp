package com.sudoku.SudokuApp.gameLogic;


import com.sudoku.SudokuApp.SudokuGame;
import com.sudoku.SudokuApp.dao.LocalStorage;
import com.sudoku.SudokuApp.dao.Storage;
import com.sudoku.SudokuApp.userinterface.UserInterfaceContract;
import com.sudoku.SudokuApp.userinterface.controlLogic.ControlLogic;

import java.io.IOException;

public class BuildLogic {
    public static void build(UserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        Storage storage = new LocalStorage();

        try {
            initialState = storage.getGameData();
        } catch (IOException e) {

            initialState = GameLogic.getNewGame();
            storage.updateGameData(initialState);
        }

        UserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);
    }
}
