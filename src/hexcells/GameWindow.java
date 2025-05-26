package hexcells;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JFrame {
    private final Board gameBoard;
    private final GridPanel gridPanel;
    private final HintBot hintBot;

    public GameWindow(Board board) {
        this.gameBoard = board;
        this.hintBot = new HintBot(board);
        setTitle("Hexcells");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new GridPanel(board);
        add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Подсказка");
        JButton menuButton = new JButton("Меню");
        buttonPanel.add(hintButton);
        buttonPanel.add(menuButton);
        add(buttonPanel, BorderLayout.SOUTH);

        hintButton.addActionListener(e -> {
            Hint hint = hintBot.giveHint();
            if (hint != null) {
                HexCoord coord = hint.getCoord();
                if (hint.getType() == Hint.HintType.REVEAL) {
                    gameBoard.revealCell(coord);
                } else if (hint.getType() == Hint.HintType.FLAG) {
                    gameBoard.toggleFlag(coord);
                }
                gridPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Нет доступных подсказок!");
            }
        });

        menuButton.addActionListener(e -> {
            dispose();
            new MenuWindow().setVisible(true);
        });

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HexCoord coord = gridPanel.pixelToHex(e.getX(), e.getY());
                if (coord != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        gameBoard.revealCell(coord);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        gameBoard.toggleFlag(coord);
                    }
                    gridPanel.repaint();
                }
            }
        });

        gameBoard.addPropertyChangeListener(evt -> {
            if (gameBoard.isGameOver()) {
                String message = gameBoard.isGameWon() ? "Победа!" : "Поражение!";
                JOptionPane.showMessageDialog(this, message);
                dispose();
                new MenuWindow().setVisible(true);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }
}