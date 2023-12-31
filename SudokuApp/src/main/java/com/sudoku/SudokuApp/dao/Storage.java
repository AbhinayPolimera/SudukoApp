package com.sudoku.SudokuApp.dao;

import com.sudoku.SudokuApp.SudokuGame;

import java.io.IOException;

public interface Storage {
    void updateGameData(SudokuGame game) throws IOException;
    SudokuGame getGameData() throws IOException;
}
