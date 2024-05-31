package project.model;

public record Position(int row, int column) {

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, column);
    }

    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        else if (o instanceof Position other){
            return this.row == other.row && this.column == other.column;
        }
        return false;
    }
}
