package hexcells;

import java.util.*;

public class HintBot {
    private final Board board;

    public HintBot(Board board) {
        this.board = board;
    }

    public Hint giveHint() {
        for (Rule rule : board.getActiveRules()) {
            Hint hint = analyzeRule(rule);
            if (hint != null) {
                return hint;
            }
        }
        return null;
    }

    private Hint analyzeRule(Rule rule) {
        if (rule instanceof EdgeRule edgeRule) {
            return analyzeEdgeRule(edgeRule);
        } else if (rule instanceof SequenceRule sequenceRule) {
            return analyzeSequenceRule(sequenceRule);
        } else if (rule instanceof GroupRule groupRule) {
            return analyzeGroupRule(groupRule);
        }
        return null;
    }

    private Hint analyzeEdgeRule(EdgeRule rule) {
        HexCoord coord = rule.getCellCoord();
        Cell center = board.getCell(coord);
        if (center == null || center.isRevealed()) {
            return null;
        }

        int expectedMines = rule.getExpectedNeighborMines();
        List<HexCoord> neighbors = board.getNeighbors(coord);
        int flaggedCount = 0;
        int unrevealedCount = 0;
        List<HexCoord> unrevealedNeighbors = new ArrayList<>();

        for (HexCoord neighborCoord : neighbors) {
            Cell neighbor = board.getCell(neighborCoord);
            if (neighbor != null) {
                if (neighbor.isFlagged()) {
                    flaggedCount++;
                } else if (!neighbor.isRevealed()) {
                    unrevealedCount++;
                    unrevealedNeighbors.add(neighborCoord);
                }
            }
        }

        if (unrevealedCount == 0) {
            return null;
        }

        if (flaggedCount == expectedMines && unrevealedCount > 0) {
            return new Hint(unrevealedNeighbors.get(0), Hint.HintType.REVEAL);
        }

        if (flaggedCount + unrevealedCount == expectedMines) {
            return new Hint(unrevealedNeighbors.get(0), Hint.HintType.FLAG);
        }

        return null;
    }

    private Hint analyzeSequenceRule(SequenceRule rule) {
        List<HexCoord> cells = rule.getCellsInSequence();
        int expectedMines = rule.getExpectedConsecutiveMines();
        int flaggedCount = 0;
        int unrevealedCount = 0;
        List<HexCoord> unrevealedCells = new ArrayList<>();

        for (HexCoord coord : cells) {
            Cell cell = board.getCell(coord);
            if (cell != null) {
                if (cell.isFlagged()) {
                    flaggedCount++;
                } else if (!cell.isRevealed()) {
                    unrevealedCount++;
                    unrevealedCells.add(coord);
                }
            }
        }

        if (unrevealedCount == 0) {
            return null;
        }

        if (flaggedCount == expectedMines && unrevealedCount > 0) {
            return new Hint(unrevealedCells.get(0), Hint.HintType.REVEAL);
        }

        if (flaggedCount + unrevealedCount == expectedMines) {
            return new Hint(unrevealedCells.get(0), Hint.HintType.FLAG);
        }

        return null;
    }

    private Hint analyzeGroupRule(GroupRule rule) {
        List<HexCoord> cells = rule.getCellsInGroup();
        int expectedMines = rule.getExpectedGroupedMines();
        int flaggedCount = 0;
        int unrevealedCount = 0;
        List<HexCoord> unrevealedCells = new ArrayList<>();

        for (HexCoord coord : cells) {
            Cell cell = board.getCell(coord);
            if (cell != null) {
                if (cell.isFlagged()) {
                    flaggedCount++;
                } else if (!cell.isRevealed()) {
                    unrevealedCount++;
                    unrevealedCells.add(coord);
                }
            }
        }

        if (unrevealedCount == 0) {
            return null;
        }

        if (flaggedCount == expectedMines && unrevealedCount > 0) {
            return new Hint(unrevealedCells.get(0), Hint.HintType.REVEAL);
        }

        if (flaggedCount + unrevealedCount == expectedMines) {
            return new Hint(unrevealedCells.get(0), Hint.HintType.FLAG);
        }

        return null;
    }

    private Hint analyzeEdgeRuleForSafeReveal(EdgeRule rule) {
        HexCoord coord = rule.getCellCoord();
        Cell center = board.getCell(coord);
        if (center == null || !center.isRevealed()) {
            return null;
        }

        int expectedMines = rule.getExpectedNeighborMines();
        List<HexCoord> neighbors = board.getNeighbors(coord);
        int flaggedCount = 0;
        int unrevealedCount = 0;
        List<HexCoord> unrevealedNeighbors = new ArrayList<>();

        for (HexCoord neighborCoord : neighbors) {
            Cell neighbor = board.getCell(neighborCoord);
            if (neighbor != null) {
                if (neighbor.isFlagged()) {
                    flaggedCount++;
                } else if (!neighbor.isRevealed()) {
                    unrevealedCount++;
                    unrevealedNeighbors.add(neighborCoord);
                }
            }
        }

        if (flaggedCount == expectedMines && unrevealedCount > 0) {
            return new Hint(unrevealedNeighbors.get(0), Hint.HintType.REVEAL);
        }

        return null;
    }

    private Hint analyzeSequenceRuleForSafeReveal(SequenceRule rule) {
        List<HexCoord> cells = rule.getCellsInSequence();
        int expectedMines = rule.getExpectedConsecutiveMines();
        int flaggedCount = 0;
        int unrevealedCount = 0;
        List<HexCoord> unrevealedCells = new ArrayList<>();

        for (HexCoord coord : cells) {
            Cell cell = board.getCell(coord);
            if (cell != null) {
                if (cell.isFlagged()) {
                    flaggedCount++;
                } else if (!cell.isRevealed()) {
                    unrevealedCount++;
                    unrevealedCells.add(coord);
                }
            }
        }

        if (flaggedCount == expectedMines && unrevealedCount > 0) {
            return new Hint(unrevealedCells.get(0), Hint.HintType.REVEAL);
        }

        return null;
    }
}