package com.sudoku.SudokuApp.gameLogic;

import com.sudoku.SudokuApp.SudokuGame;
import com.sudoku.SudokuApp.constants.GameState;
import com.sudoku.SudokuApp.constants.Rows;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sudoku.SudokuApp.SudokuGame.GRID_BOUNDARY;


public class GameLogic {
    public static SudokuGame getNewGame() {
        return new SudokuGame(
                GameState.NEW,
                GameGenerator.getNewGameGrid()
        );
    }

    public static GameState checkForCompletion(int[][] grid) {
        if (sudokuIsInvalid(grid) || tilesAreNotFilled(grid)) {
            return GameState.ACTIVE;
        }
        return GameState.COMPLETE;
    }

    public static boolean tilesAreNotFilled(int[][] grid) {
        return Arrays.stream(grid)
                .flatMapToInt(Arrays::stream)
                .anyMatch(value -> value == 0);
    }

    public static boolean sudokuIsInvalid(int[][] grid) {
        return rowsAreInvalid(grid) || columnsAreInvalid(grid) || squaresAreInvalid(grid);
    }

    public static boolean squaresAreInvalid(int[][] grid) {
        return Stream.of(Rows.TOP, Rows.MIDDLE, Rows.BOTTOM)
                .anyMatch(row -> rowOfSquaresIsInvalid(row, grid));
    }

    private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid) {
        switch (value) {
            case TOP:
                return IntStream.of(0, 3, 6)
                        .anyMatch(col -> squareIsInvalid(0, col, grid));

            case MIDDLE:
                return IntStream.of(0, 3, 6)
                        .anyMatch(col -> squareIsInvalid(3, col, grid));

            case BOTTOM:
                return IntStream.of(0, 3, 6)
                        .anyMatch(col -> squareIsInvalid(6, col, grid));

            default:
                return false;
        }
    }

    public static boolean squareIsInvalid(int yIndex, int xIndex, int[][] grid) {
        int yIndexEnd = yIndex + 3;
        int xIndexEnd = xIndex + 3;
        Set<Integer> square = new HashSet<>();
        for (int y = yIndex; y < yIndexEnd; y++) {
            for (int x = xIndex; x < xIndexEnd; x++) {
                if (!square.add(grid[y][x])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean columnsAreInvalid(int[][] grid) {
        return IntStream.range(0, GRID_BOUNDARY)
                .anyMatch(xIndex -> collectionHasRepeats(
                        IntStream.range(0, GRID_BOUNDARY)
                                .map(yIndex -> grid[xIndex][yIndex])
                                .boxed()
                                .collect(Collectors.toList())));
    }

    public static boolean rowsAreInvalid(int[][] grid) {
        return IntStream.range(0, GRID_BOUNDARY)
                .anyMatch(yIndex -> collectionHasRepeats(
                        Arrays.stream(grid[yIndex])
                                .boxed()
                                .collect(Collectors.toList())));
    }

    public static boolean collectionHasRepeats(List<Integer> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream()
                .anyMatch(count -> count > 1);
    }
}
