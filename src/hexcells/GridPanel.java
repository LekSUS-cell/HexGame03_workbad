package hexcells;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

public class GridPanel extends JPanel {
    private final Board board;
    private static final int CELL_SIZE = 30;
    private static final int MARGIN = 10;
    private HexCoord highlightedCell;

    public GridPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(board.getCols() * CELL_SIZE + 2 * MARGIN, board.getRows() * CELL_SIZE + 2 * MARGIN));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HexCoord coord = pixelToHex(e.getX(), e.getY());
                if (coord != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        board.revealCell(coord);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        board.toggleFlag(coord);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                HexCoord coord = pixelToHex(e.getX(), e.getY());
                if (coord != null && !coord.equals(highlightedCell)) {
                    highlightedCell = coord;
                    repaint();
                }
            }
        });
        board.addPropertyChangeListener(evt -> repaint());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < board.getRows(); r++) {
            for (int q = 0; q < board.getCols(); q++) {
                HexCoord coord = new HexCoord(q, r);
                Cell cell = board.getCell(coord);
                if (cell != null) {
                    Point center = hexToPixel(q, r);
                    Path2D hex = createHexagon(center.x, center.y, CELL_SIZE / 2);

                    if (cell.isRevealed()) {
                        g2.setColor(cell.isMine() ? Color.RED : Color.LIGHT_GRAY);
                        g2.fill(hex);
                        if (!cell.isMine() && cell.getRevealedValue() > 0) {
                            g2.setColor(Color.BLACK);
                            g2.drawString(String.valueOf(cell.getRevealedValue()), center.x - 5, center.y + 5);
                        }
                    } else {
                        g2.setColor(cell.isFlagged() ? Color.YELLOW : Color.GRAY);
                        g2.fill(hex);
                    }

                    g2.setColor(Color.BLACK);
                    g2.draw(hex);

                    if (coord.equals(highlightedCell)) {
                        g2.setColor(new Color(0, 255, 0, 100));
                        g2.fill(hex);
                    }
                }
            }
        }
    }

    private Path2D createHexagon(int centerX, int centerY, int size) {
        Path2D hex = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            double x = centerX + size * Math.cos(angle);
            double y = centerY + size * Math.sin(angle);
            if (i == 0) {
                hex.moveTo(x, y);
            } else {
                hex.lineTo(x, y);
            }
        }
        hex.closePath();
        return hex;
    }

    private Point hexToPixel(int q, int r) {
        int x = MARGIN + CELL_SIZE * q;
        int y = MARGIN + CELL_SIZE * r;
        return new Point(x, y);
    }

    public HexCoord pixelToHex(int x, int y) {
        int q = (x - MARGIN) / CELL_SIZE;
        int r = (y - MARGIN) / CELL_SIZE;
        if (q >= 0 && q < board.getCols() && r >= 0 && r < board.getRows()) {
            return new HexCoord(q, r);
        }
        return null;
    }
}