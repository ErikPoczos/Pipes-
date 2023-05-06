package sk.stuba.fei.uim.oop.maze;

import lombok.Getter;

import sk.stuba.fei.uim.oop.pipes.Curved;
import sk.stuba.fei.uim.oop.pipes.Straight;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Board extends JPanel {
    private Tile[][] board;
    @Getter
    private int boardSize;
    @Getter
    private final ArrayList<Tile> pathToFinish;
    Random random;

    public Board(int boardSize) {
        this.random = new Random();
        this.pathToFinish = new ArrayList<>();
        this.boardSize = boardSize;
        this.generateBoard(this.boardSize);
    }

    public Tile getNode(int x, int y) {
        if (x >= this.boardSize || x < 0 || y >= this.boardSize || y < 0) {
            return null;
        }
        return this.board[y][x];
    }

    public void generateBoard(int newBoardSize) {
        this.boardSize = newBoardSize;
        int start = random.nextInt(this.boardSize);
        int finish = random.nextInt(this.boardSize);

        this.initializeMaze(start, finish);

        HashSet<Tile> visitedTiles = new HashSet<>();
        ArrayList<Step> stack = new ArrayList<>();
        stack.add(new Step(this.board[0][start], null));

        while (!stack.isEmpty()) {
            Step step = stack.remove(0);
            Tile currentTile = step.getCurrent();

            if (currentTile.isFinish()) {
                currentTile.setPrevious(step.getPrevious());
                while (currentTile.getPrevious() != null) {
                    this.pathToFinish.add(0, currentTile);
                    currentTile = currentTile.getPrevious();
                }
                this.pathToFinish.add(0, this.board[0][start]);
                initializePipes();
                break;
            }

            if (visitedTiles.contains(currentTile)) {
                continue;
            }

            if (step.getPrevious() != null) {
                currentTile.setPrevious(step.getPrevious());
            }

            ArrayList<Tile> allNeighbours = currentTile.getAllNeighbour();
            Collections.shuffle(allNeighbours);
            Tile finalCurrentTile = currentTile;
            allNeighbours.forEach(neighbour -> {
                if (!visitedTiles.contains(neighbour)) {
                    stack.add(0, new Step(neighbour, finalCurrentTile));
                }
            });
            visitedTiles.add(currentTile);
        }
    }

    private void initializeMaze(int start, int finish) {
        this.board = new Tile[this.boardSize][this.boardSize];
        this.setLayout(new GridLayout(this.boardSize, this.boardSize));
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                this.board[i][j] = new Tile(j, i);
                this.add(this.board[i][j]);
            }
        }
        this.board[0][start].setStart(true);
        this.board[this.boardSize - 1][finish].setFinish(true);

        for (int x = 0; x < this.boardSize; x++) {
            for (int y = 0; y < this.boardSize; y++) {
                if (x != 0) {
                    this.board[x][y].addNeighbour(Direction.UP, this.board[x-1][y]);
                }
                if (x != this.boardSize-1) {
                    this.board[x][y].addNeighbour(Direction.DOWN, this.board[x+1][y]);
                }
                if (y != 0) {
                    this.board[x][y].addNeighbour(Direction.LEFT, this.board[x][y-1]);
                }
                if (y != this.boardSize-1) {
                    this.board[x][y].addNeighbour(Direction.RIGHT, this.board[x][y+1]);
                }
            }
        }
    }

    public void initializePipes() {
        for (int i = 0; i < this.pathToFinish.size(); i++) {
            Tile currentTile = pathToFinish.get(i);
            if (currentTile.isStart()){
                Tile nextTile = pathToFinish.get(i+1);
                findOrientation(currentTile, nextTile);
            }

            if (currentTile.isFinish()){
                Tile nextTile = pathToFinish.get(i-1);
                findOrientation(currentTile, nextTile);
            }

            Tile previousTile = i > 0 ? pathToFinish.get(i - 1) : null;
            Tile nextTile = i < pathToFinish.size() - 1 ? pathToFinish.get(i + 1) : null;

            if (previousTile != null && nextTile != null) {
                if (this.isStraight(previousTile, currentTile, nextTile)) {
                    currentTile.setPipeType(new Straight());
                } else {
                    currentTile.setPipeType(new Curved());
                }
            }
        }
    }

    private void findOrientation(Tile currentTile, Tile nextTile) {
        if (nextTile.getY() == currentTile.getY()){
            if (nextTile.getX() < currentTile.getX()){
                currentTile.setDirection(Direction.LEFT);
            } else {
                currentTile.setDirection(Direction.RIGHT);
            }
        } else {
            if (nextTile.getY() < currentTile.getY()){
                currentTile.setDirection(Direction.UP);
            } else {
                currentTile.setDirection(Direction.DOWN);
            }
        }
    }

    private boolean isStraight(Tile previousTile, Tile currentTile, Tile nextTile) {
        return  ((previousTile.getX() == currentTile.getX()) && (currentTile.getX() == nextTile.getX())) ||
                ((previousTile.getY() == currentTile.getY()) && (currentTile.getY() == nextTile.getY()));
    }

    public void draw(Graphics g) {
        for (int x = 0; x < this.boardSize; x++) {
            for (int y = 0; y < this.boardSize; y++)  {
                this.board[x][y].draw(g);
            }
        }
        g.drawRect(Tile.OFFSET, Tile.OFFSET, this.boardSize*Tile.NODE_SIZE, this.boardSize*Tile.NODE_SIZE);
    }
}
