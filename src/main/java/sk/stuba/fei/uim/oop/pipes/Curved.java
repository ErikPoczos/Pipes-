package sk.stuba.fei.uim.oop.pipes;

import sk.stuba.fei.uim.oop.maze.Direction;
import sk.stuba.fei.uim.oop.maze.Tile;

import java.awt.*;
import java.util.ArrayList;

public class Curved extends Pipe {

    public Curved(){
        this.holes = new ArrayList<>();
        int randomDirection = random.nextInt(4);
        this.holes.add(Direction.RIGHT.getDirection(randomDirection));
        this.holes.add(Direction.UP.getDirection((randomDirection +1)%4));
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if (this.holes.contains(Direction.DOWN)){
            this.startX = x + Tile.NODE_SIZE / 2;
            this.startY = y + Tile.NODE_SIZE - PIPE_OFFSET;
            this.endX = x + Tile.NODE_SIZE / 2;
            this.endY = y + Tile.NODE_SIZE / 2;

            g.drawLine(startX, startY, endX, endY);
            getLeftOrRight(g, x, y);

        } else {
            this.startX = x + Tile.NODE_SIZE / 2;
            this.startY = y + Tile.NODE_SIZE / 2;
            this.endX = x + Tile.NODE_SIZE / 2;
            this.endY = y + PIPE_OFFSET;

            g.drawLine(startX, startY, endX, endY);
            getLeftOrRight(g, x, y);
        }
    }

    private void getLeftOrRight(Graphics g, int x, int y) {

        if (this.holes.contains(Direction.RIGHT)) {
            this.startX = x + Tile.NODE_SIZE / 2;
            this.startY = y + Tile.NODE_SIZE / 2;
            this.endX = x + Tile.NODE_SIZE - PIPE_OFFSET;
            this.endY = y + Tile.NODE_SIZE / 2;
        } else {
            this.startX = x + Tile.NODE_SIZE / 2;
            this.startY = y + Tile.NODE_SIZE / 2;
            this.endX = x + PIPE_OFFSET;
            this.endY = y + Tile.NODE_SIZE / 2;
        }

        g.drawLine(startX, startY, endX, endY);
    }

    @Override
    public void rotate(){
        this.holes.replaceAll(direction -> direction.getDirection((direction.getValue()+1)%4));
    }
}