package hexcells;

import java.util.List;

public class GroupRule implements Rule {
    private final List<HexCoord> cellsInGroup;
    private final int expectedGroupedMines;

    public GroupRule(List<HexCoord> cellsInGroup, int expectedGroupedMines) {
        this.cellsInGroup = cellsInGroup;
        this.expectedGroupedMines = expectedGroupedMines;
    }

    public List<HexCoord> getCellsInGroup() {
        return cellsInGroup;
    }

    public int getExpectedGroupedMines() {
        return expectedGroupedMines;
    }

    @Override
    public boolean isSatisfied(Board board) {
        int mineCount = 0;
        for (HexCoord coord : cellsInGroup) {
            Cell cell = board.getCell(coord);
            if (cell != null && (cell.isMine() || cell.isFlagged())) {
                mineCount++;
            }
        }
        return mineCount == expectedGroupedMines;
    }
}