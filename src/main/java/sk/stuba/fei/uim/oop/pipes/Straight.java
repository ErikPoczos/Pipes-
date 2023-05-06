package sk.stuba.fei.uim.oop.pipes;

import sk.stuba.fei.uim.oop.maze.Direction;
import sk.stuba.fei.uim.oop.maze.Tile;

import java.awt.*;
import java.util.ArrayList;

public class Straight extends Pipe {

    public Straight(){
        this.holes = new ArrayList<>();
        int randomDirection = random.nextInt(4);
        this.holes.add(Direction.UP.getDirection(randomDirection));
        this.holes.add(Direction.UP.getDirection((randomDirection +2)%4));
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        if (holes.contains(Direction.UP)){
            this.startX = x + Tile.NODE_SIZE / 2;
            this.startY = y+PIPE_OFFSET;
            this.endX = x + Tile.NODE_SIZE / 2;
            this.endY = y + Tile.NODE_SIZE -PIPE_OFFSET;
        } else {
            this.startX = x+PIPE_OFFSET;
            this.startY = y + Tile.NODE_SIZE / 2;
            this.endX = x + Tile.NODE_SIZE-PIPE_OFFSET;
            this.endY = y + Tile.NODE_SIZE / 2;
        }

        g.drawLine(startX, startY, endX, endY);
    }

    @Override
    public void rotate(){
        this.holes.replaceAll(orientation -> orientation.getDirection((orientation.getValue()+1)%4));
    }
}
