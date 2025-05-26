package hexcells;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Board {
    private final Cell[][] cells;
    private final int rows;
    private final int cols;
    private final List<Rule> activeRules;
    private boolean gameOver;
    private boolean gameWon;
    private final PropertyChangeSupport pcs;

    public Board(LevelConfig config) {
        rows = config.getRows();
        cols = config.getCols();
        cells = new Cell[rows][cols];
        activeRules = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
        gameOver = false;
        gameWon = false;

        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                cells[r][q] = new Cell();
            }
        }

        for (HexCoord mineCoord : config.getMines()) {
            Cell cell = getCell(mineCoord);
            if (cell != null) {
                cell.setMine(true);
            }
        }

        activeRules.addAll(config.getRules()); // Прямо добавляем правила

        for (HexCoord revealedCoord : config.getRevealedCells()) {
            Cell cell = getCell(revealedCoord);
            if (cell != null && !cell.isMine()) {
                revealCell(revealedCoord);
            }
        }

        updateAllRevealedValues();
    }

    public Cell getCell(HexCoord coord) {
        int q = coord.getQ();
        int r = coord.getR();
        if (q >= 0 && q < cols && r >= 0 && r < rows) {
            return cells[r][q];
        }
        return null;
    }

    public void revealCell(HexCoord coord) {
        Cell cell = getCell(coord);
        if (cell != null && !cell.isRevealed() && !cell.isFlagged()) {
            cell.setRevealed(true);
            if (cell.isMine()) {
                gameOver = true;
                pcs.firePropertyChange("state", null, null);
            } else {
                int mineCount = countNeighborMines(coord);
                cell.setRevealedValue(mineCount);
                if (mineCount == 0) {
                    for (HexCoord neighbor : getNeighbors(coord)) {
                        revealCell(neighbor);
                    }
                }
                checkWinCondition();
                pcs.firePropertyChange("state", null, null);
            }
        }
    }

    public void toggleFlag(HexCoord coord) {
        Cell cell = getCell(coord);
        if (cell != null && !cell.isRevealed()) {
            cell.setFlagged(!cell.isFlagged());
            pcs.firePropertyChange("state", null, null);
        }
    }

    public List<HexCoord> getNeighbors(HexCoord coord) {
        List<HexCoord> neighbors = new ArrayList<>();
        int q = coord.getQ();
        int r = coord.getR();
        int[][] directions = {
                {+1, 0}, {-1, 0}, {0, +1}, {0, -1}, {+1, -1}, {-1, +1}
        };
        for (int[] dir : directions) {
            int newQ = q + dir[0];
            int newR = r + dir[1];
            if (newQ >= 0 && newQ < cols && newR >= 0 && newR < rows) {
                neighbors.add(new HexCoord(newQ, newR));
            }
        }
        return neighbors;
    }

    private int countNeighborMines(HexCoord coord) {
        int count = 0;
        for (HexCoord neighbor : getNeighbors(coord)) {
            Cell cell = getCell(neighbor);
            if (cell != null && cell.isMine()) {
                count++;
            }
        }
        return count;
    }

    private void updateAllRevealedValues() {
        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                Cell cell = cells[r][q];
                if (cell.isRevealed() && !cell.isMine()) {
                    cell.setRevealedValue(countNeighborMines(new HexCoord(q, r)));
                }
            }
        }
    }

    protected boolean checkWinCondition() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;
        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                Cell cell = cells[r][q];
                if (cell.isMine() && !cell.isFlagged()) {
                    allMinesFlagged = false;
                }
                if (!cell.isMine() && !cell.isRevealed()) {
                    allNonMinesRevealed = false;
                }
            }
        }

        boolean allRulesSatisfied = true;
        for (Rule rule : activeRules) {
            if (!rule.isSatisfied(this)) {
                allRulesSatisfied = false;
                break;
            }
        }

        gameWon = allMinesFlagged && allNonMinesRevealed && allRulesSatisfied;
        if (gameWon) {
            gameOver = true;
            pcs.firePropertyChange("state", null, null);
        }
        return allMinesFlagged;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Rule> getActiveRules() {
        return Collections.unmodifiableList(activeRules);
    }

    public Cell[][] getGrid() {
        return null;
    }
}