package com.sudoku.SudokuApp.userinterface;

import javafx.scene.control.TextField;
import lombok.Data;

@Data
public class SudokuTextField extends TextField {
    private final int x;
    private final int y;

    @Override
    public void replaceText(int i, int j, String s) {
        if (!s.matches("[0-9]")) {
            super.replaceText(i, j, s);
        }
    }


    @Override
    public void replaceSelection(String s) {
        if (!s.matches("[0-9]")) {
            super.replaceSelection(s);
        }
    }
}
