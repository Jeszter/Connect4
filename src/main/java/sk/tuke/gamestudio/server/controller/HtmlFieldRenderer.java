package sk.tuke.gamestudio.server.controller;

import sk.tuke.gamestudio.game.enums.FieldState;
import sk.tuke.gamestudio.game.enums.TileState;
import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.game.game.SkinData;

import java.util.Map;

public class HtmlFieldRenderer {

    public static String render(Field field, String player1Skin, String player2Skin) {
        System.out.println("P1 skin key: " + player1Skin + "P1");
        System.out.println("P2 skin key: " + player2Skin + "P2");

        Map<String, String> p1Colors = SkinData.SKIN_COLORS.getOrDefault(player1Skin + "P1", SkinData.SKIN_COLORS.get("classicP1"));
        Map<String, String> p2Colors = SkinData.SKIN_COLORS.getOrDefault(player2Skin + "P2", SkinData.SKIN_COLORS.get("classicP2"));

        StringBuilder sb = new StringBuilder();
        sb.append("<div id='connect4-game-container'>");
        sb.append("<div class='score-container'>");
        sb.append("</div>");
        sb.append("<div class='game-board'>");
        sb.append("<table align='center' style='background-color:#611d8c; width:100%; max-width:600px; aspect-ratio:7/6; border-radius:15px; box-shadow: inset 0 0 25px rgba(0, 0, 0, 0.6); border:10px solid #4a166a;'>");
        sb.append("<tr><td style='padding:20px;'>");
        sb.append("<table border='0' cellspacing='15' cellpadding='0' style='width:100%; height:100%; table-layout:fixed;'>");

        int lastMoveRow = field.getLastMoveRow();
        int lastMoveCol = field.getLastMoveColumn();
        boolean isLastMove = (lastMoveRow != -1 && lastMoveCol != -1 && field.getTile(lastMoveRow, lastMoveCol).getState() != TileState.EMPTY);

        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>");
            for (int col = 0; col < field.getColumnCount(); col++) {
                var tile = field.getTile(row, col);
                sb.append("<td style='text-align:center; vertical-align:middle; padding:0;'>");
                sb.append("<div style='width:100%; aspect-ratio:1/1; border-radius:50%; background-color:#1e1e2f; border:4px solid #4a166a; box-shadow: inset 0 4px 8px rgba(30, 30, 47, 0.6), 0 0 10px rgba(0, 0, 0, 0.5); display:flex; align-items:center; justify-content:center; position:relative; overflow:hidden;'>");

                if (field.getState() == FieldState.PLAYING) {
                    sb.append("<div onclick='makeMove(").append(col).append(")' style='display:block; width:100%; height:100%; cursor:pointer;'>");
                }

                String gradient;
                if (tile.getState() == TileState.PLAYER1) {
                    gradient = p1Colors.get("light") + ", " + p1Colors.get("dark");
                } else if (tile.getState() == TileState.PLAYER2) {
                    gradient = p2Colors.get("light") + ", " + p2Colors.get("dark");
                } else {
                    gradient = "transparent, transparent";
                }

                boolean isAnimatedChip = isLastMove && row == lastMoveRow && col == lastMoveCol;

                sb.append("<div style='width:90%; height:90%; margin:auto; border-radius:100%; background: radial-gradient(circle at 30% 30%, ").append(gradient).append("); box-shadow: 0 6px 12px rgba(0, 0, 0, 0.5); border: 2px solid ").append(tile.getState() == TileState.PLAYER1 ? p1Colors.get("border") : tile.getState() == TileState.PLAYER2 ? p2Colors.get("border") : "rgba(255, 255, 255, 0.3)").append("; transition: transform 0.2s;");

                if (isAnimatedChip) {
                    sb.append(" animation: chipFall 0.5s ease-out;");
                }

                sb.append("'></div>");

                if (field.getState() == FieldState.PLAYING) {
                    sb.append("</div>");
                }

                sb.append("</div></td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table></td></tr></table>");
        sb.append("</div>");

        if (isLastMove) {
            sb.append("<style>@keyframes chipFall {0% { transform: translateY(-500%); opacity: 0; }80% { transform: translateY(10%); opacity: 1; }100% { transform: translateY(0); }}</style>");
            sb.append("<audio id='drop-sound' src='/sounds/chip-drop.mp3' preload='auto'></audio><script>document.getElementById('drop-sound').play();</script>");
        }

        sb.append("<script>function makeMove(column) {fetch('/connect4?column=' + (column + 1), {method: 'GET',headers: { 'Accept': 'text/html' }}).then(response => response.text()).then(html => {const parser = new DOMParser();const doc = parser.parseFromString(html, 'text/html');const newGameContainer = doc.getElementById('connect4-game-container');if (newGameContainer) {document.getElementById('connect4-game-container').innerHTML = newGameContainer.innerHTML;updateGameStatus();}}).catch(error => console.error('Error:', error));}function updateGameStatus() {fetch('/connect4/status', {method: 'GET',headers: { 'Accept': 'application/json' }}).then(response => response.json()).then(data => {if (data.gameOver) {document.getElementById('gameEndMessage').textContent = data.message;showModal('gameEndModal');}});}</script>");
        sb.append("</div>");
        return sb.toString();
    }
}
