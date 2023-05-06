package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;

import sk.stuba.fei.uim.oop.gui.GraphicsHandler;
import sk.stuba.fei.uim.oop.maze.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameLogic extends UniversalAdapter {

    public static final int INITIAL_BOARD_SIZE = 8;
    public static final String RESTART = "RESTART";
    public static final String CHECK = "CHECK";

    private final JFrame mainGame;
    @Getter
    private Board board;
    @Getter
    private final GraphicsHandler graphicsHandler;
    @Getter
    private int level;
    @Getter
    private final JLabel label;
    @Getter
    private final JLabel boardSizeLabel;

    private int currentBoardSize;

    public GameLogic(JFrame mainGame) {
        this.mainGame = mainGame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.initializeNewBoard(this.currentBoardSize);
        this.graphicsHandler = new GraphicsHandler(this.board);
        this.graphicsHandler.addMouseListener(this);
        this.graphicsHandler.addMouseMotionListener(this);
        this.level = 1;
        this.label = new JLabel();
        this.boardSizeLabel = new JLabel();
        this.updateCounterLabel();
        this.updateBoardSizeLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        switch (button.getText()) {
            case RESTART:
                this.gameRestart();
                this.repaint();
                break;
            case CHECK:
                checkWin();
                this.repaint();
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Tile current = this.board.getNode(convertPosition(e.getX()), convertPosition(e.getY()));
        if (current == null) {
            return;
        }
        current.getPipeType().rotate();
        current.setHighlight(true);
        this.graphicsHandler.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Tile current = this.board.getNode(convertPosition(e.getX()), convertPosition(e.getY()));
        if (current == null) {
            return;
        }
        current.setHighlight(true);
        this.graphicsHandler.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Tile current = this.board.getNode(convertPosition(e.getX()), convertPosition(e.getY()));
        if (current == null) {
            return;
        }
        this.graphicsHandler.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int newSize = ((JSlider) e.getSource()).getValue();
        if (newSize == this.currentBoardSize) {
            return;
        }
        this.currentBoardSize = newSize;

        this.updateBoardSizeLabel();
        this.gameRestart();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                this.gameRestart();
                this.repaint();
                break;
            case KeyEvent.VK_ESCAPE:
                this.mainGame.dispose();
                System.exit(0);
                break;
            case KeyEvent.VK_ENTER:
                checkWin();
                this.repaint();
                break;
        }
    }

    private int convertPosition(int screenPosition) {
        return ((screenPosition - Tile.OFFSET) < 0) ? -1 : (screenPosition - Tile.OFFSET) / Tile.NODE_SIZE;
    }

    private void updateBoardSizeLabel() {
        this.boardSizeLabel.setText("CURRENT BOARD SIZE: " + this.currentBoardSize);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void initializeNewBoard(int dimension) {
        this.board = new Board(dimension);
        this.board.addMouseMotionListener(this);
        this.board.addMouseListener(this);
    }

    public void repaint() {
        this.graphicsHandler.repaint();
    }

    public void gameWon() {
        this.board.generateBoard(this.currentBoardSize);
        this.level++;
        this.updateCounterLabel();
    }

    public void gameRestart() {
        this.gameWon();
        this.level = 1;
        this.updateCounterLabel();
    }

    private void updateCounterLabel() {
        this.label.setText("CURRENT LEVEL: " + this.level);
    }

    private void checkWin(){
        if (this.checkPath()){
            gameWon();
        }
    }

    private boolean checkPath() {
        Direction usedHole;
        Direction freeHole = null;
        Tile currentTile = this.board.getPathToFinish().get(0);
        Tile nextTile;

        while (true) {
            if (currentTile == null){
                return false;
            }
            if (currentTile.isStart()){
                nextTile = currentTile.getNeighborAtDirection(currentTile.getDirection());
                usedHole = this.checkConnection(nextTile.getPipeType().getHoles(), currentTile.getDirection());
                if (usedHole == null){
                    return false;
                }
                freeHole = getHoleToConnect(usedHole, nextTile);
                currentTile = nextTile.getNeighborAtDirection(freeHole);
            } else if(currentTile.isFinish()){
                nextTile = currentTile.getPrevious();
                usedHole = this.checkConnection(nextTile.getPipeType().getHoles(), currentTile.getDirection());
                if (usedHole == null){
                    return false;
                } else {
                    nextTile.setConnected(true);
                    return true;
                }
            }

            else {
                var holes = currentTile.getPipeType().getHoles();
                usedHole = this.checkConnection(holes, freeHole);
                if (usedHole == null){
                    return false;
                }
                freeHole = getHoleToConnect(usedHole, currentTile);
                currentTile = currentTile.getNeighborAtDirection(freeHole);
            }
        }
    }

    private Direction checkConnection(ArrayList<Direction> toConnect, Direction freeHole){
        for (Direction direction : toConnect){
            if (direction == freeHole.getRequiredDirection(freeHole)){
                return direction;
            }
        }
        return null;
    }

    private Direction getHoleToConnect(Direction usedHole, Tile toConnect){
        toConnect.setConnected(true);
        return toConnect.getPipeType().getHoles().stream().filter(x -> x != usedHole).collect(Collectors.toList()).get(0);
    }
}
