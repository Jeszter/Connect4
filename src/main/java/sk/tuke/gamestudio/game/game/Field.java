package sk.tuke.gamestudio.game.game;

import sk.tuke.gamestudio.game.enums.FieldState;
import sk.tuke.gamestudio.game.enums.GameMode;
import sk.tuke.gamestudio.game.enums.TileState;

public class Field {

    private final Tile[][] tiles;

    private final int rowCount;

    private final int columnCount;

    private FieldState state = FieldState.MENU;

    private GameMode mode = GameMode.PvP;

    private int player1Score = 0;

    private int player2Score = 0;

    private Bot bot;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        tiles = new Tile[rowCount][columnCount];
        generate();
        bot = new Bot(this);
    }


    public void generate() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                tiles[row][col] = new Tile();
            }
        }
    }

    public GameMode getMode() {
        return mode;
    }

    public GameMode setMode(GameMode mode) {
        return this.mode = mode;
    }



    public FieldState getState() {
        return state;
    }

    public FieldState setState(FieldState state) {
        return this.state = state;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getLastMoveRow() {
        return LastMoveRow;
    }

    public int getLastMoveColumn() {
        return LastMoveColumn;
    }

    private int LastMoveColumn = 0;
    private int LastMoveRow = 0;

    public TileState currentPlayer = TileState.PLAYER1;

    private TileState winner = null;

    public TileState getWinner() {
        return winner;
    }
    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }
    public void restScore(){
        player1Score = 0;
        player2Score = 0;
    }

    public void result() {
        if (checkWin(TileState.PLAYER1)) {
            setState(FieldState.WIN);
            player1Score += 500;
            winner = TileState.PLAYER1;
            return;
        }

        if (checkWin(TileState.PLAYER2)) {
            if (getMode() == GameMode.PvB) {
                setState(FieldState.FAIL);
                winner = TileState.PLAYER2;
            } else {
                setState(FieldState.WIN);
                winner = TileState.PLAYER2;
                player2Score += 500;
            }
            return;
        }

        if (isFull()) {
            setState(FieldState.DRAW);
        }
    }

    private boolean checkWin(TileState player) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
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
            if (r >= 0 && r < rowCount && c >= 0 && c < columnCount && tiles[r][c].getState() == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    private boolean isFull() {
        for (int col = 1; col < columnCount; col++) {
            if (tiles[0][col].getState() == TileState.EMPTY) {
                return false;
            }
        }
        return true;
    }


    public void dropDisc(int column) {
        if (column < 0 || column >= columnCount) {
            System.out.println("Invalid move! Column out of bounds: " + column);
            return;
        }

        for (int row = getRowCount() - 1; row >= 0; row--) {
            var tile = getTile(row, column);

            if (tile.getState() == TileState.EMPTY) {
                tile.setState(currentPlayer);
                LastMoveRow = row;
                LastMoveColumn = column;
                if (currentPlayer == TileState.PLAYER1) {
                    player1Score += 100;
                } else {
                    player2Score += 100;
                }
                switchPlayer();
                if (getState() == FieldState.PLAYING && getMode() == GameMode.PvB && currentPlayer == TileState.PLAYER2) {
                    bot.makeMove();
                }
                return;
            }
        }

        System.out.println("Column " + column + " is full! Choose another column.");
    }



    private void switchPlayer() {
        if (currentPlayer == TileState.PLAYER1) {
            currentPlayer = TileState.PLAYER2;
        } else {
            currentPlayer = TileState.PLAYER1;
        }
        return;
    }


}
