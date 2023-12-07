package com.sudoku.SudokuApp;

import com.sudoku.SudokuApp.main.SudokuApplication;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuAppApplication {

	public static void main(String[] args) {
		Application.launch(SudokuApplication.class, args);
	}

}
