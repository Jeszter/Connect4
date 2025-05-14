package sk.tuke.gamestudio.game.game;

import sk.tuke.gamestudio.game.enums.TileState;

public class Tile {
    private TileState state = TileState.EMPTY;

    public TileState getState() {
        return state;
    }
    public void setState(TileState state) {
        this.state = state;
    }

}
