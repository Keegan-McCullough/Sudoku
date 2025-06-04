package edu.wm.cs.cs301.sudoku.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.*;

import edu.wm.cs.cs301.sudoku.controller.KeyboardAction;
import edu.wm.cs.cs301.sudoku.model.AppColors;
import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;

public class KeyboardPanel {
    private int buttonIndex, buttonCount;
    private final JButton[] buttons;
    private final JPanel panel;
    private final SudokuPuzzle puzzle;
    private final KeyboardAction action;

    public KeyboardPanel(SudokuFrame view, SudokuPuzzle puzzle) {
        this.puzzle = puzzle;
        this.buttonIndex = 0;
        this.buttonCount = firstRow().length;
        this.buttons = new JButton[buttonCount];
        this.action = new KeyboardAction(view, puzzle);
        this.panel = createMainPanel();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

        panel.add(createNumbersPanel());
        panel.add(createTotalPanel());

        return panel;
    }

    private JPanel createNumbersPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        Font textfont = AppFonts.getTextFont();

        String[] numbers = firstRow();

        for (int index = 0; index < numbers.length; index++) {
            JButton button = new JButton(numbers[index]);
            setKeyBinding(button, numbers[index]);
            button.setFont(textfont);
            button.addActionListener(action);
            buttons[buttonIndex++] = button;
            panel.add(button);
        }
        return panel;
    }

    private String[] firstRow() {
        String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "clear" };
        return numbers;
    }

    private void setKeyBinding(JButton button, String text) {
        InputMap inputMap = button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW);
        if (text.equalsIgnoreCase("Backspace")) {
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
                    "action");
        } else {
            inputMap.put(KeyStroke.getKeyStroke(text.toUpperCase()), "action");
        }
        ActionMap actionMap = button.getActionMap();
        actionMap.put("action", action);
    }
    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        return panel;
    }
    public void setColor(String letter, Color backgroundColor,
                         Color foregroundColor) {
        for (JButton button : buttons) {
            if (button.getActionCommand().equals(letter)) {
                Color color = button.getBackground();
                if (color.equals(AppColors.GREEN)) {
                    // Do nothing
                } else if (color.equals(AppColors.YELLOW)
                        && backgroundColor.equals(AppColors.GREEN)) {
                    button.setBackground(backgroundColor);
                    button.setForeground(foregroundColor);
                } else {
                    button.setBackground(backgroundColor);
                    button.setForeground(foregroundColor);
                }
                break;
            }
        }
    }
    public void resetDefaultColors() {
        for (JButton button : buttons) {
            button.setBackground(null);
            button.setForeground(null);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

}