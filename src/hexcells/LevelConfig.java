package hexcells;

import java.util.*;

public class LevelConfig {
    private final int rows;
    private final int cols;
    private final List<HexCoord> mines;
    private final List<Rule> rules;
    private final List<HexCoord> revealedCells;

    public LevelConfig(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.mines = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.revealedCells = new ArrayList<>();
    }

    public void addMine(HexCoord coord) {
        mines.add(coord);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void addRevealedCell(HexCoord coord) {
        revealedCells.add(coord);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<HexCoord> getMines() {
        return Collections.unmodifiableList(mines);
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public List<HexCoord> getRevealedCells() {
        return Collections.unmodifiableList(revealedCells);
    }
}