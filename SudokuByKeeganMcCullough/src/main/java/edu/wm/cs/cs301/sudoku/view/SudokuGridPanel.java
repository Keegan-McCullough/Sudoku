package edu.wm.cs.cs301.sudoku.view;

import java.awt.*;

import javax.swing.*;

import edu.wm.cs.cs301.sudoku.model.AppColors;
import edu.wm.cs.cs301.sudoku.model.SudokuPuzzle;
import edu.wm.cs.cs301.sudoku.model.SudokuResponse;

public class SudokuGridPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int topMargin, leftMargin, letterWidth;

    private final Insets insets;

    private final Rectangle[][] grid;

    private final SudokuPuzzle puzzle;

    public SudokuSubGrid subgrids;


    public SudokuGridPanel(SudokuFrame view, SudokuPuzzle puzzle, int width) {
        this.puzzle = puzzle;
        this.topMargin = 0;
        this.letterWidth = 48;
        this.insets = new Insets(0, 0, 0, 0);

        int wordWidth = (letterWidth + insets.right) * puzzle.getNumCols();
        this.leftMargin = (width - wordWidth) / 2;
        int height = (letterWidth + insets.bottom) * puzzle.getNumRows()
                + 2 * topMargin;
        this.setPreferredSize(new Dimension(width, height));


        this.grid = calculateRectangles();
        setLayout(new GridLayout(3, 3, 6, 4));
        setBorder(BorderFactory.createEmptyBorder(5, 53, 3, 50));

        // Make the panel focusable to receive key events
        setFocusable(true);
        requestFocusInWindow();
        subgrids = new SudokuSubGrid(view, this.puzzle, this);

    }

    private Rectangle[][] calculateRectangles() {
        Rectangle[][] grid = new Rectangle[puzzle.getNumCols()][puzzle.getNumRows()];

        int x = leftMargin;
        int y = topMargin;

        for (int row = 0; row < puzzle.getNumRows(); row++) {
            for (int column = 0; column < puzzle.getNumCols(); column++) {
                grid[row][column] = new Rectangle(x, y, letterWidth,
                        letterWidth);
                x += letterWidth + insets.right;
            }
            x = leftMargin;
            y += letterWidth + insets.bottom;
        }

        return grid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font titleFont = AppFonts.getTitleFont();
        SudokuResponse[][] SudokuGrid = puzzle.getSudokuGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Rectangle r = grid[row][column];
                SudokuResponse SudokuResponse = SudokuGrid[row][column];
                drawBasicOutline(g2d, r);

                // creating the proper subgrid outline in the sudoku board
                if (row % 3 == 0 && column % 3 == 0) {
                    drawTopOutline(g2d, r);
                    drawSideOutline(g2d, r);
                } else if (row % 3 == 0) {
                    drawTopOutline(g2d, r);
                } else if (column % 3 == 0) {
                    drawSideOutline(g2d, r);
                }
                if (row == 8) {
                    drawBottomOutline(g2d, r);
                }
                if (column == 8) {
                    drawRightOutline(g2d, r);
                }
                drawSudokuResponse(g2d, SudokuResponse, r, titleFont);
            }
        }
    }

    private void drawBasicOutline(Graphics2D g2d, Rectangle r) {
        int x = r.x + 1;
        int y = r.y + 1;
        int width = r.width - 2;
        int height = r.height - 2;
        g2d.setColor(AppColors.OUTLINE);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(x, y, x + width, y);
        g2d.drawLine(x, y + height, x + width, y + height);
        g2d.drawLine(x, y, x, y + height);
        g2d.drawLine(x + width, y, x + width, y + height);
    }

    private void drawTopOutline(Graphics2D g2d, Rectangle r) {
        int x = r.x + 1;
        int y = r.y + 1;
        int width = r.width - 2;
        int height = r.height - 2;
        g2d.setColor(AppColors.BLACK);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawLine(x, y, x + width, y);
    }

    private void drawSideOutline(Graphics2D g2d, Rectangle r) {
        int x = r.x + 1;
        int y = r.y + 1;
        int width = r.width - 2;
        int height = r.height - 2;
        g2d.setColor(AppColors.BLACK);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawLine(x, y, x, y + height);
    }

    private void drawBottomOutline(Graphics2D g2d, Rectangle r) {
        int x = r.x + 1;
        int y = r.y + 1;
        int width = r.width - 2;
        int height = r.height - 2;
        g2d.setColor(AppColors.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        g2d.drawLine(x, y + height, x + width, y + height);

    }

    private void drawRightOutline(Graphics2D g2d, Rectangle r) {
        int x = r.x + 1;
        int y = r.y + 1;
        int width = r.width - 2;
        int height = r.height - 2;
        g2d.setColor(AppColors.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        g2d.drawLine(x + width, y, x + width, y + height);
    }

    private void drawSudokuResponse(Graphics2D g2d,
                                    SudokuResponse SudokuResponse, Rectangle r, Font titleFont) {
        if (SudokuResponse != null) {
            g2d.setColor(SudokuResponse.getBackgroundColor());
            g2d.fillRect(r.x, r.y, r.width, r.height);
            g2d.setColor(SudokuResponse.getForegroundColor());
            drawCenteredString(g2d,
                    Character.toString(SudokuResponse.getChar()), r, titleFont);
        }
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g2d  The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    private void drawCenteredString(Graphics2D g2d, String text, Rectangle rect,
                                    Font font) {
        FontMetrics metrics = g2d.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2)
                + metrics.getAscent();

        g2d.setFont(font);
        g2d.drawString(text, x, y);
    }

    public SudokuSubGrid getSubgrids() {return subgrids;}
}