package edu.wm.cs.cs301.sudoku;

import javax.swing.*;

import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;
import edu.wm.cs.cs301.sudoku.view.InstructionsDialog;
import edu.wm.cs.cs301.sudoku.view.SudokuFrame;

public class Sudoku implements Runnable {
    public static void main (String[] args) {

        SwingUtilities.invokeLater(new Sudoku());

        //Can't use the Cross-Platform Look and Feel on Windows - Needs investigation
        if (!System.getProperty("os.name").contains("Windows")) {
            //Must use cross-platform look and feel so button backgrounds work on Mac
            try {
                System.out.println("OS: " + System.getProperty("os.name"));

                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Failed to load Cross-Platform Look and Feel on non-Windows system.");
                System.out.println("GUI may not function correctly");
            }
        }
    }

    @Override
    public void run() {
        // These must be out of loop to add to the same board
        SudokuPuzzle sudokuPuzzle = new SudokuPuzzle();
        // creates an initial instruction window for the user
        new InstructionsDialog(new SudokuFrame(sudokuPuzzle));

    }
}
