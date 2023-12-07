package com.sudoku.SudokuApp.gameLogic;

import com.sudoku.SudokuApp.Coordinates;

import java.util.stream.IntStream;

import static com.sudoku.SudokuApp.SudokuGame.GRID_BOUNDARY;

public class SudokuSolver {

    public static boolean puzzleIsSolvable(int[][] puzzle) {
        Coordinates[] emptyCells = typeWriterEnumerate(puzzle);
        int index = 0;
        int input = 1;
        while (index >= 0 && index < 40) {
            Coordinates current = emptyCells[index];
            puzzle[current.getX()][current.getY()] = input;
            if (GameLogic.sudokuIsInvalid(puzzle)) {
                if (input == GRID_BOUNDARY) {
                    puzzle[current.getX()][current.getY()] = 0;
                    index--;
                }
                input++;
            } else {
                index++;
                if (index == 40) {
                    return true;
                }
                input = 1;
            }
        }
        return false;
    }

    private static Coordinates[] typeWriterEnumerate(int[][] puzzle) {
        return IntStream.range(0, GRID_BOUNDARY)
                .boxed()
                .flatMap(y -> IntStream.range(0, GRID_BOUNDARY)
                        .filter(x -> puzzle[x][y] == 0)
                        .mapToObj(x -> new Coordinates(x, y)))
                .limit(40)
                .toArray(Coordinates[]::new);
    }

}
