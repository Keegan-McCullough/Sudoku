package edu.wm.cs.cs301.sudoku.controller;

import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;
import edu.wm.cs.cs301.sudoku.view.CongratulationsDialog;
import edu.wm.cs.cs301.sudoku.view.SudokuFrame;
import edu.wm.cs.cs301.sudoku.view.SudokuGridPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class KeyboardAction extends AbstractAction{

    private static final long serialVersionUID = 1L;
    private SudokuPuzzle puzzle;
    private static JLabel selectedLabel;
    private int[][] board;
    private final SudokuFrame view;
    private static int buttonRow;
    private static int buttonCol;

    public KeyboardAction(SudokuFrame view, SudokuPuzzle puzzle) {
        this.puzzle = puzzle;
        this.view = view;
        this.board = puzzle.getCurrent();
    }

    public void setSelectedCell(JLabel selectedLabel, int buttonRow, int buttonCol) {
        this.selectedLabel = selectedLabel;
        this.buttonRow = buttonRow;
        this.buttonCol = buttonCol;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
            // necessary to prevent key from firing twice
            Object source = event.getSource();
            if (source instanceof JButton) {
                JButton button = (JButton) event.getSource();
                String text = "" + buttonRow + buttonCol;
                if (button.getActionCommand().equals("clear")) {
                    text = text + "0";
                } else {
                    text = text + button.getActionCommand();
                }
                updateSelectedCell(text, board);
            }
    }

    public void updateSelectedCell(String value, int[][] board) {
        //split value if change needs to be done
        String[] spaces = value.split("");

        boolean changedBoard = puzzle.validate(value, board);
        // if move is valid change the board accordingly
        if (changedBoard) {
            if (spaces[2].equals("0")){
                selectedLabel.setText("");
            } else {
                selectedLabel.setText(spaces[2]);
            }
        } else{
            doesLabelContainNumber(spaces[2]);
        }
        view.repaintSudokuGridPanel();
        boolean isComplete = puzzle.checkGrid(board);
        if (isComplete){
            new CongratulationsDialog(view, puzzle);
        }
    }
    public void doesLabelContainNumber(String number) {
        SudokuGridPanel grid = view.getSudokuGridPanel();
        for (JLabel label : grid.getSubgrids().getLabels()){
            // revert original color back
            Color original = view.getSudokuGridPanel().getSubgrids().getOriginalColors().get(label);
            if (original != null) {
                label.setBackground(original);
            }

            // Set red if the label matches
            if (label.getText().equals(number)) {
                label.setBackground(Color.RED);
            }
        }
    }

}