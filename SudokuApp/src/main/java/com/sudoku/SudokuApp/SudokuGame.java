package com.sudoku.SudokuApp;

import com.sudoku.SudokuApp.constants.GameState;
import com.sudoku.SudokuApp.utils.UtilClasses;
import lombok.Data;

import java.io.Serializable;

@Data
public class SudokuGame implements Serializable {
    private final GameState gameState;

    private final int[][] gridState;

    public static final int GRID_BOUNDARY = 9;

    public int[][] getCopyOfGridState() {
        return UtilClasses.copyToNewArray(gridState);
    }

}
