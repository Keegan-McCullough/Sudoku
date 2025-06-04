package edu.wm.cs.cs301.sudoku.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;

public class CongratulationsDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final ExitAction exitAction;

    private final NextAction nextAction;

    private final SudokuFrame view;

    private final SudokuPuzzle puzzle;

    public CongratulationsDialog(SudokuFrame view, SudokuPuzzle puzzle) {
        super(view.getFrame(), "Congratulations", true);
        this.view = view;
        this.puzzle = puzzle;
        this.exitAction = new ExitAction();
        this.nextAction = new NextAction();

        add(createMainPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(view.getFrame());

        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        panel.add(createTopPanel(), BorderLayout.NORTH);
        panel.add(createBottomPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        panel.add(createTitlePanel(), BorderLayout.NORTH);
        panel.add(createSummaryPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTitlePanel() {
        // Changed this Jpanel to a grid layout so the messages are stacked on top of one another
        JPanel panel = new JPanel(new GridLayout(2, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        // Calls new closing message if user failed

        // new function to allow for closing message to be above statistics
        panel.add(createTitleMessage());

        return panel;
    }

    // Outputs the closing message with the word
    // adds statistic in the correct place
    private JPanel createTitleMessage() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JLabel label = new JLabel("Congratulations");
        label.setFont(AppFonts.getTitleFont());
        panel.add(label);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        panel.add(createSubtitlePanel(), BorderLayout.NORTH);

        return panel;
    }

    private JPanel createSubtitlePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JLabel label = new JLabel("Congrats on completing the puzzle, you can exit or play a new game!");
        label.setFont(AppFonts.getTextFont());
        panel.add(label);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        return panel;
    }


    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "nextAction");
        ActionMap actionMap = panel.getActionMap();
        actionMap.put("nextAction", nextAction);
        actionMap.put("exitAction", exitAction);

        JButton nextButton = new JButton("Play Again");
        nextButton.addActionListener(nextAction);
        panel.add(nextButton);

        JButton exitButton = new JButton("Exit Sudoku");
        exitButton.addActionListener(exitAction);
        panel.add(exitButton);

        Dimension nextDimension = nextButton.getPreferredSize();
        Dimension exitDimension = exitButton.getPreferredSize();
        int maxWidth = Math.max(nextDimension.width, exitDimension.width) + 10;
        nextButton.setPreferredSize(new Dimension(maxWidth, nextDimension.height));
        exitButton.setPreferredSize(new Dimension(maxWidth, exitDimension.height));

        return panel;
    }

    private class ExitAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent event) {
            dispose();
            view.shutdown();
        }

    }

    private class NextAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent event) {
            view.getFrame().dispose();
            new SudokuFrame(new SudokuPuzzle());
        }

    }
}
