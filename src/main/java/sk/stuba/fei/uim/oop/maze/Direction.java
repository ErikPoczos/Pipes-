package sk.stuba.fei.uim.oop.maze;

import lombok.Getter;

public enum Direction {
    UP(0, 0, 1, 0, 0),
    DOWN(0, 1, 1, 1, 2),
    LEFT(0, 0, 0, 1, 3),
    RIGHT(1, 0, 1, 1, 1);

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    Direction(int x1, int y1, int x2, int y2, int value) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.value = value;
    }

    @Getter
    private final int value;

    public Direction getDirection(int value) {
        for (Direction direction : Direction.values()) {
            if (direction.value == value) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Invalid value for Direction enum: " + value);
    }

    public Direction getRequiredDirection(Direction direction){
        return this.getDirection((direction.getValue()+2)%4);
    }
}
