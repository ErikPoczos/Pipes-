package sk.stuba.fei.uim.oop.maze;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.pipes.Pipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tile extends JPanel {

    public static final int NODE_SIZE = 40;
    public static final int OFFSET = 20;
    public static final int START_FINISH_OFFSET = 2;

    @Getter
    private final Map<Direction, Connection> neighbours;
    @Getter
    private final int x;
    @Getter
    private final int y;
    @Getter @Setter
    private Tile previous;
    @Getter @Setter
    private Direction direction;
    @Setter @Getter
    private boolean finish;
    @Setter @Getter
    private boolean start;
    @Setter
    private boolean highlight;
    @Setter @Getter
    private Pipe pipeType;
    @Setter @Getter
    private boolean connected;

    public Tile(int x, int y) {
        this.x = x * NODE_SIZE + OFFSET;
        this.y = y * NODE_SIZE + OFFSET;
        this.finish = false;
        this.neighbours = new HashMap<>();
        this.previous = null;
        this.pipeType = new Pipe(){};
    }

    public void addNeighbour(Direction direction, Tile tile) {
        this.neighbours.put(direction, new Connection(tile));
    }

    public ArrayList<Tile> getAllNeighbour() {
        ArrayList<Tile> all = new ArrayList<>();
        this.neighbours.values().forEach(connection -> all.add(connection.getTile()));
        return all;
    }

    public Tile getNeighborAtDirection(Direction direction) {
        Connection connection = this.neighbours.get(direction);
        return (connection != null) ? connection.getTile() : null;
    }

    public void draw(Graphics g) {
        int startX;
        int startY;
        int endX;
        int endY;

        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.drawRect(this.x, this.y, Tile.NODE_SIZE, Tile.NODE_SIZE);

        if (this.highlight) {
            g.setColor(Color.BLUE);
            g.fillRect(this.x, this.y, Tile.NODE_SIZE, Tile.NODE_SIZE);
            g.setColor(Color.BLACK);
            this.highlight = false;
        }

        if (this.finish) {
            g.setColor(Color.RED);
            g.fillRect(this.x, this.y, Tile.NODE_SIZE, Tile.NODE_SIZE);
            g.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(6));

            if (this.direction == Direction.RIGHT){
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE / 2;
                endX = x + Tile.NODE_SIZE - START_FINISH_OFFSET;
                endY = y + Tile.NODE_SIZE / 2;
            } else if (this.direction == Direction.LEFT) {
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE / 2;
                endX = x + START_FINISH_OFFSET;
                endY = y + Tile.NODE_SIZE / 2;
            } else {
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE / 2;
                endX = x + Tile.NODE_SIZE / 2;
                endY = y + START_FINISH_OFFSET;
            }

            g.drawLine(startX, startY, endX, endY);
            g2d.setStroke(new BasicStroke(1));
            return;

        }
        if (this.start){
            g.setColor(Color.GREEN);
            g.fillRect(this.x, this.y, Tile.NODE_SIZE, Tile.NODE_SIZE);
            g.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(5));

            if (this.direction == Direction.RIGHT){
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE / 2;
                endX = x + Tile.NODE_SIZE - START_FINISH_OFFSET;
                endY = y + Tile.NODE_SIZE / 2;
            } else if (this.direction == Direction.LEFT) {
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE / 2;
                endX = x + START_FINISH_OFFSET;
                endY = y + Tile.NODE_SIZE / 2;
            } else {
                startX = x + Tile.NODE_SIZE / 2;
                startY = y + Tile.NODE_SIZE;
                endX = x + Tile.NODE_SIZE / 2;
                endY = y + Tile.NODE_SIZE / 2 - START_FINISH_OFFSET;
            }

            g.drawLine(startX, startY, endX, endY);
            g2d.setStroke(new BasicStroke(1));
            return;
        }

        g2d.setStroke(new BasicStroke(12));
        this.getPipeType().draw(g, this.x, this.y);

        g.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(5));
        this.getPipeType().draw(g, this.x, this.y);
        g.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        if (this.connected) {
            g.setColor(Color.CYAN);
            g2d.setStroke(new BasicStroke(5));
            this.getPipeType().draw(g, this.x, this.y);
            g.setColor(Color.BLACK);
            this.connected = false;
        }
        g2d.setStroke(new BasicStroke(1));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (this.highlight){
            g.setColor(Color.BLUE);
        } else{
            g.setColor(Color.black);
        }
        g.setColor(Color.BLACK);

    }
}
