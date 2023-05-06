package sk.stuba.fei.uim.oop.maze;

import lombok.Data;

@Data
public class Connection {
    private boolean connected;
    private Tile tile;

    public Connection(Tile tile) {
        this.connected = false;
        this.tile = tile;
    }
}
