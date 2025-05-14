package test;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.game.enums.FieldState;
import sk.tuke.gamestudio.game.enums.GameMode;
import sk.tuke.gamestudio.game.enums.TileState;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Random randomGenerator = new Random();
    private Field field;
    private int rowCount;
    private int columnCount;

    public FieldTest() {
        rowCount = randomGenerator.nextInt(10) + 5;
        columnCount = rowCount;
        field = new Field(rowCount, columnCount);
    }

    @Test
    public void checkFieldInitialization() {
        assertEquals(rowCount, field.getRowCount());
        assertEquals(columnCount, field.getColumnCount());
        assertEquals(FieldState.MENU, field.getState());
    }

    @Test
    public void checkGameModeChange() {
        field.setMode(GameMode.PvB);
        assertEquals(GameMode.PvB, field.getMode());
    }

    @Test
    public void checkTileInitialization() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                assertNotNull(field.getTile(row, col));
                assertEquals(TileState.EMPTY, field.getTile(row, col).getState());
            }
        }
    }

    @Test
    public void checkDropDisc() {
        int column = randomGenerator.nextInt(columnCount);
        field.dropDisc(column);
        assertNotEquals(TileState.EMPTY, field.getTile(rowCount - 1, column).getState());
    }

    @Test
    public void checkWinCondition() {
        for (int i = 0; i < 4; i++) {
            field.getTile(rowCount - 1 - i, 0).setState(TileState.PLAYER1);
        }
        field.result();
        assertEquals(FieldState.WIN, field.getState());
    }

    @Test
    public void checkFullBoardDraw() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                if ((col / 3) % 2 == 0) {
                    if (row % 2 == 0) {
                        field.getTile(row, col).setState(TileState.PLAYER2);
                    } else {
                        field.getTile(row, col).setState(TileState.PLAYER1);
                    }
                } else {
                    if (row % 2 == 0) {
                        field.getTile(row, col).setState(TileState.PLAYER1);
                    } else {
                        field.getTile(row, col).setState(TileState.PLAYER2);
                    }
                }
            }
        }

        /*
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                System.out.print(field.getTile(row, col).getState() + " ");
            }
            System.out.println();
        }*/

        field.result();
        assertEquals(FieldState.DRAW, field.getState());
    }


}
