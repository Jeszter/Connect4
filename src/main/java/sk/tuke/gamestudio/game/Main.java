package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.game.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        //Tile tile = new Tile();
        //TileState state = new TileState();
        var field = new Field(6,8);
        var ui = new ConsoleUI(field);
        ui.play();

    }


}