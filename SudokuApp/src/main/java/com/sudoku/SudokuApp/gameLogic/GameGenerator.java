package com.sudoku.SudokuApp.gameLogic;

import com.sudoku.SudokuApp.Coordinates;
import com.sudoku.SudokuApp.utils.UtilClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.sudoku.SudokuApp.SudokuGame.GRID_BOUNDARY;


public class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unsolveGame(getSolvedGame());
    }

    private static int[][] getSolvedGame() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int[][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        for (int value = 1; value <= GRID_BOUNDARY; value++) {
            int allocations = 0;
            List<Coordinates> allocTracker = new ArrayList<>();

            while (allocations < GRID_BOUNDARY) {
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;

                    if (!GameLogic.sudokuIsInvalid(newGrid)) {
                        allocTracker.add(new Coordinates(xCoordinate, yCoordinate));
                        allocations++;
                    } else {
                        newGrid[xCoordinate][yCoordinate] = 0;
                    }
                }
            }
        }
        return newGrid;
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        do {
            UtilClasses.copySudokuArrayValues(solvedGame, solvableArray);

            IntStream.range(0, 40)
                    .forEach(index -> {
                        int xCoordinate;
                        int yCoordinate;
                        do {
                            xCoordinate = random.nextInt(GRID_BOUNDARY);
                            yCoordinate = random.nextInt(GRID_BOUNDARY);
                        } while (solvableArray[xCoordinate][yCoordinate] == 0);
                        solvableArray[xCoordinate][yCoordinate] = 0;
                    });

        } while (!SudokuSolver.puzzleIsSolvable(Arrays.copyOf(solvableArray, solvableArray.length)));

        return solvableArray;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
