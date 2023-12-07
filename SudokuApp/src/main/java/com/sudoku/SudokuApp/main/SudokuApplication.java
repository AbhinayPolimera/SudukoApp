package com.sudoku.SudokuApp.main;

import com.sudoku.SudokuApp.gameLogic.BuildLogic;
import com.sudoku.SudokuApp.userinterface.UserInterfaceContract;
import com.sudoku.SudokuApp.userinterface.UserInterfaceImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuApplication extends Application {
    private UserInterfaceContract.View userInterface;

    @Override
    public void start(Stage primaryStage) throws IOException {
        userInterface = new UserInterfaceImpl(primaryStage);
        try{
            BuildLogic.build(userInterface);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}