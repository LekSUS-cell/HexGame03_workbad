package hexcells;

public class EdgeRule implements Rule {
    private final HexCoord cellCoord;
    private final int expectedNeighborMines;

    public EdgeRule(HexCoord coord, int expectedNeighborMines) {
        this.cellCoord = coord;
        this.expectedNeighborMines = expectedNeighborMines;
    }

    public HexCoord getCellCoord() {
        return cellCoord;
    }

    public HexCoord getCell() {
        return cellCoord; // Для совместимости с HintBot
    }

    public int getExpectedNeighborMines() {
        return expectedNeighborMines;
    }

    @Override
    public boolean isSatisfied(Board board) {
        int neighborMines = 0;
        for (HexCoord neighbor : board.getNeighbors(cellCoord)) {
            Cell cell = board.getCell(neighbor);
            if (cell != null && (cell.isMine() || cell.isFlagged())) {
                neighborMines++;
            }
        }
        return neighborMines == expectedNeighborMines;
    }
}