package sk.stuba.fei.uim.oop.gui;


import sk.stuba.fei.uim.oop.maze.Board;

import javax.swing.*;
import java.awt.*;

public class GraphicsHandler extends JPanel {
    private final Board board;

    public GraphicsHandler(Board board) {
        this.board = board;
        this.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.board.draw(g);
    }
}
