package com.sudoku.SudokuApp.userinterface;

import com.sudoku.SudokuApp.SudokuGame;

public interface UserInterfaceContract {
    interface EventListener {
        void onSudokuInput(int x, int y, int input);
        void onDialogClick();
    }

    interface View {
        void setListener(EventListener listener);
        void updateSquare(int x, int y, int input);

        void updateBoard(SudokuGame game);
        void showDialog(String message);
        void showError(String message);
    }
}
