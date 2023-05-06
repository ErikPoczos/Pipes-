package sk.stuba.fei.uim.oop.pipes;

import lombok.Getter;
import sk.stuba.fei.uim.oop.maze.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

abstract public class Pipe {

    public static final int PIPE_OFFSET = 6;

    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected final Random random;

    @Getter
    protected ArrayList<Direction> holes;

    public Pipe(){
        this.random = new Random();
        this.holes = new ArrayList<>();
    }

    public void draw(Graphics g, int x, int y){}

    public void rotate(){}
}
