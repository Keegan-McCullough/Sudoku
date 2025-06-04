package edu.wm.cs.cs301.sudoku.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;

public class SudokuFrame{
    private final JFrame frame;

    private final KeyboardPanel keyboardPanel;


    private final SudokuPuzzle puzzle;

    private final SudokuGridPanel sudokuGridPanel;

    public SudokuFrame(SudokuPuzzle puzzle) {
        this.puzzle = puzzle;
        this.keyboardPanel = new KeyboardPanel(this, puzzle);
        int width = keyboardPanel.getPanel().getPreferredSize().width;
        this.sudokuGridPanel = new SudokuGridPanel(this, puzzle, width);
        this.frame = createAndShowGUI();

        System.out.println("Keyboard Panel: " + keyboardPanel.getPanel());
        System.out.println("SubGrid Panel: " + sudokuGridPanel);
    }
    private JFrame createAndShowGUI() {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                shutdown();
            }
        });

        frame.add(createTitlePanel(), BorderLayout.NORTH);
        frame.add(sudokuGridPanel, BorderLayout.CENTER);
        frame.add(keyboardPanel.getPanel(), BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        System.out.println("Frame size: " + frame.getSize());

        return frame;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // menu items
        JMenuItem instructionsItem = new JMenuItem("Instructions...");
        instructionsItem.addActionListener(event -> new InstructionsDialog(this));
        helpMenu.add(instructionsItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(event -> shutdown());
        helpMenu.add(quitItem);

        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(event -> restartGame());
        helpMenu.add(restartItem);

        return menuBar;
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        //panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
        ActionMap actionMap = panel.getActionMap();
        actionMap.put("cancelAction", new CancelAction());

        JLabel label = new JLabel("Sudoku");
        label.setForeground(Color.BLACK);
        label.setFont(AppFonts.getTitleFont());
        panel.add(label);

        return panel;
    }

    public void shutdown() {
        frame.dispose();
        System.exit(0);
    }

    public void resetDefaultColors() {
        keyboardPanel.resetDefaultColors();
    }

    public void setColor(String letter, Color backgroundColor, Color foregroundColor) {
        keyboardPanel.setColor(letter, backgroundColor, foregroundColor);
    }

    public void repaintSudokuGridPanel() {
        sudokuGridPanel.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public SudokuGridPanel getSudokuGridPanel() {return sudokuGridPanel;}

    private class CancelAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent event) {
            shutdown();
        }

    }
    // create new game if restart option is selected
    public void restartGame(){
        frame.dispose();
        new SudokuFrame(new SudokuPuzzle());
    }

}
