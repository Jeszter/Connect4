package sk.tuke.gamestudio.game.game;

import sk.tuke.gamestudio.game.enums.TileState;

public class FieldSimulator {
    private final Field field;

    public FieldSimulator(Field field) {
        this.field = field;
    }

    public boolean checkWinSimulated(TileState player) {
        int rows = field.getRowCount();
        int cols = field.getColumnCount();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (checkDirection(row, col, 1, 0, player) ||
                        checkDirection(row, col, 0, 1, player) ||
                        checkDirection(row, col, 1, 1, player) ||
                        checkDirection(row, col, 1, -1, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, TileState player) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;

            if (r >= 0 && r < field.getRowCount() &&
                    c >= 0 && c < field.getColumnCount() &&
                    field.getTile(r, c).getState() == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }
}
