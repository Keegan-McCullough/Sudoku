package edu.wm.cs.cs301.sudoku.view;

import edu.wm.cs.cs301.sudoku.controller.KeyboardAction;
import edu.wm.cs.cs301.sudoku.model.AppColors;
import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class SudokuSubGrid {

    private int buttonIndex, buttonCount, labelIndex;

    private final JButton[] buttons;

    private JButton selectedButton;

    private JLabel selectedLabel;

    private JButton previousSelectedButton = null;

    private int[][] board;

    private int buttonCol;
    private int buttonRow;

    private final KeyboardAction action;

    public JPanel[][] subgrids;

    private JLabel[] labels;

    private HashMap<JLabel, Color> originalColors;

    private SudokuGridPanel gridPanel;

    private final SudokuPuzzle puzzle;

    public SudokuSubGrid(SudokuFrame view, SudokuPuzzle puzzle, SudokuGridPanel gridPanel){
        this.puzzle = puzzle;
        this.gridPanel = gridPanel;

        this.buttonIndex = 0;
        this.buttonCount = 81;

        this.buttons = new JButton[buttonCount];
        this.labels = new JLabel[buttonCount];

        this.originalColors = new HashMap<>();
        this.action = new KeyboardAction(view, puzzle);

        // First create 9 subgrid panels
        subgrids = new JPanel[3][3];
        createSubGrids(subgrids);
        fillSubGrids();

    }

    public void createSubGrids(JPanel[][] subgrids) {
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                JPanel subgrid = new JPanel(new GridLayout(3, 3, 1, 3));
                subgrid.setBackground(AppColors.OUTLINE); // To see the border
                subgrids[blockRow][blockCol] = subgrid;
                gridPanel.add(subgrid);
            }
        }
    }

    public void fillSubGrids() {
        buttonIndex = 0;
        labelIndex = 0;

        // Now add button+label panels into the correct subgrid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int blockRow = row / 3;
                int blockCol = col / 3;

                // Create a small panel to hold label + button
                JPanel cellPanel = new JPanel(null); // null layout essential for button label
                cellPanel.setPreferredSize(new Dimension(50, 50));

                // set the board numbers
                board = puzzle.getCurrent();
                int currentRow = row;
                int currentCol = col;
                int value = board[currentRow][currentCol];
                String number;

                // check if value is zero if true do not have zero as label
                if (value == 0) {
                    number = "";
                } else {
                    number = "" + value;
                }

                // Create Label that will be in the front and the button behind
                JLabel currentLabel = setLabel(number);
                JButton currentButton = setButton(value, currentLabel, currentRow, currentCol);

                // store button and label
                buttons[buttonIndex++] = currentButton;
                labels[labelIndex++] = currentLabel;

                // Add label first
                cellPanel.add(currentLabel);
                cellPanel.add(currentButton);

                // Add the cellPanel to the subgrid
                subgrids[blockRow][blockCol].add(cellPanel);
            }
            // create hashmap to help revert back to original color
            for (JLabel label : getLabels()) {
                if (label != null) {
                    originalColors.put(label, label.getBackground());
                }
            }
            // Add key listener to capture number and backspace key presses
            gridPanel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent event) {
                    int keyCode = event.getKeyCode();
                    String move = "" + buttonRow + buttonCol;
                    if (keyCode == KeyEvent.VK_BACK_SPACE) {
                        move = move + "0";
                        action.updateSelectedCell(move, board);
                    }
                }
            });
        }

    }

    private JButton setButton(int value, JLabel label, int row, int col) {
        JButton button = new JButton();

        // differentiate the labels that cannot be changed
        if (value != 0) {
            label.setBackground(Color.WHITE);
        } else {
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> {
                // revert label back to original colors on next mouse click
                for (JLabel l : getLabels()) {
                    Color original = getOriginalColors().get(l);
                    l.setBackground(original);
                }

                // revert previous button back to the original state when mouse hovered
                if (previousSelectedButton != null) {
                    previousSelectedButton.setBackground(null);
                    previousSelectedButton.setOpaque(false);
                    previousSelectedButton.setContentAreaFilled(false);
                    previousSelectedButton.setFocusPainted(false);
                }

                // Set current button as selected and change its background
                selectedButton = button;
                selectedLabel = label;
                buttonRow = row;
                buttonCol = col;

                action.setSelectedCell(selectedLabel, buttonRow, buttonCol);

                selectedButton.setBackground(Color.GRAY);
                selectedButton.setOpaque(true);
                selectedButton.setContentAreaFilled(true);
                selectedButton.setFocusPainted(true);

                // Update the previous selected button
                previousSelectedButton = selectedButton;
                gridPanel.repaint();
                gridPanel.requestFocusInWindow();   // make sure the panel has keyboard focus
            });
            // create invisible button
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setBounds(0, 0, 50, 50);
        }
        return button;
    }

    private JLabel setLabel(String number){
        Font textfont = AppFonts.getTextFont();
        JLabel label = new JLabel(number, SwingConstants.CENTER);
        label.setFont(textfont);
        label.setBounds(0, 0, 50, 50);
        label.setEnabled(false); // allows clicks to pass through
        label.setOpaque(true);
        label.setForeground(Color.BLACK);
        return label;
    }

    public JLabel[] getLabels(){return labels;}

    public HashMap<JLabel, Color> getOriginalColors(){return originalColors;}
}