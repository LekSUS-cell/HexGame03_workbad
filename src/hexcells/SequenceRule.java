package hexcells;

import java.util.List;

public class SequenceRule implements Rule {
    private final List<HexCoord> cellsInSequence;
    private final int expectedConsecutiveMines;

    public SequenceRule(List<HexCoord> cellsInSequence, int expectedConsecutiveMines) {
        this.cellsInSequence = cellsInSequence;
        this.expectedConsecutiveMines = expectedConsecutiveMines;
    }

    public List<HexCoord> getCellsInSequence() {
        return cellsInSequence;
    }

    public List<HexCoord> getCells() {
        return cellsInSequence; // Для совместимости с HintBot
    }

    public int getExpectedConsecutiveMines() {
        return expectedConsecutiveMines;
    }

    public int getExpectedMines() {
        return expectedConsecutiveMines; // Для совместимости с HintBot
    }

    @Override
    public boolean isSatisfied(Board board) {
        int consecutiveMines = 0;
        int maxConsecutive = 0;

        for (HexCoord coord : cellsInSequence) {
            Cell cell = board.getCell(coord);
            if (cell != null && (cell.isMine() || cell.isFlagged())) {
                consecutiveMines++;
                maxConsecutive = Math.max(maxConsecutive, consecutiveMines);
            } else {
                consecutiveMines = 0;
            }
        }

        return maxConsecutive == expectedConsecutiveMines;
    }
}