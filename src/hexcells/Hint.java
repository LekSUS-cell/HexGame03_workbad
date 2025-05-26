package hexcells;

public class Hint {
    private final HexCoord coord;
    private final HintType type;

    public enum HintType {
        REVEAL,
        FLAG
    }

    public Hint(HexCoord coord, HintType type) {
        this.coord = coord;
        this.type = type;
    }

    public HexCoord getCoord() {
        return coord;
    }

    public HintType getType() {
        return type;
    }
}