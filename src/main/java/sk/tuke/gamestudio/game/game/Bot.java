package sk.tuke.gamestudio.game.game;

import sk.tuke.gamestudio.game.enums.TileState;

public class Bot {
    private final Field field;

    public Bot(Field field) {
        this.field = field;
    }

    public void makeMove() {
        int column = findWinningMove(TileState.PLAYER2);
        if (column == -1) {
            column = findWinningMove(TileState.PLAYER1);
        }
        if (column == -1) {
            column = findFirstValidColumn();
        }

        if (column == -1) {
            column = findAnyValidColumn();
        }

        column = Math.max(1, Math.min(7, column));

        if (isValidMove(column)) {
            field.dropDisc(column - 1);
        } else {
            System.out.println("BOT ERROR: Invalid column selected: " + column);
        }
    }

    private int findWinningMove(TileState player) {
        for (int col = 1; col <= 7; col++) {
            if (!isValidMove(col)) continue;

            int row = getAvailableRow(col);
            if (row == -1) continue;

            // Симулируем ход
            field.getTile(row, col - 1).setState(player);
            boolean win = simulateCheckWin(player);
            field.getTile(row, col - 1).setState(TileState.EMPTY);

            if (win) return col;
        }
        return -1;
    }

    private boolean simulateCheckWin(TileState player) {
        return new FieldSimulator(field).checkWinSimulated(player);
    }

    private boolean isValidMove(int column) {
        if (column < 1 || column > 7) return false;
        return field.getTile(0, column - 1).getState() == TileState.EMPTY;
    }

    private int getAvailableRow(int column) {
        for (int row = field.getRowCount() - 1; row >= 0; row--) {
            if (field.getTile(row, column - 1).getState() == TileState.EMPTY) {
                return row;
            }
        }
        return -1;
    }

    private int findFirstValidColumn() {
        int[] preferredColumns = {4, 3, 5, 2, 6, 1, 7};
        for (int col : preferredColumns) {
            if (isValidMove(col)) return col;
        }
        return -1;
    }

    private int findAnyValidColumn() {
        for (int col = 1; col <= 7; col++) {
            if (isValidMove(col)) return col;
        }
        return -1;
    }
}