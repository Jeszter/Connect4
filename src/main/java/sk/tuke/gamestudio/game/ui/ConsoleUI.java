package sk.tuke.gamestudio.game.ui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.game.game.Tile;
import sk.tuke.gamestudio.game.enums.FieldState;
import sk.tuke.gamestudio.game.enums.GameMode;
import sk.tuke.gamestudio.game.enums.TileState;
import sk.tuke.gamestudio.entity.Comment;
import java.util.Date;
import sk.tuke.gamestudio.service.*;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {
    private static final Pattern INPUT_PATTERN = Pattern.compile("1-7");
    private static final String GAME_NAME = "Connect Four";
    private final Field field;

   private int getAverageRating() {
       return ratingService.getAverageRating("Connect Four");
   }


  @Autowired
  private ScoreService scoreService;
  @Autowired
  private RatingService ratingService;
  @Autowired
  private CommentService commentService;


    private Scanner scanner = new Scanner(System.in);
    private String player1Name;
    private String player2Name;

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        do {
            printField();
            field.result();
            processInput();
        } while (field.getState() == FieldState.PLAYING || field.getState() == FieldState.MENU);
        printField();
        prinResult();
        printScores();
        leaveComment();
        rating();
        reload();
    }


    private void rating() {
        System.out.println("Would you like to rate the game?(Y/N)");
        String input = scanner.nextLine().toUpperCase();
        if (input.equals("Y")) {
            System.out.println("Please rate the game (1-5): ");
            String userRatingInput = scanner.nextLine();
            int userRating = 0;
            if (userRatingInput.equals("1") || userRatingInput.equals("2") || userRatingInput.equals("3") || userRatingInput.equals("4") || userRatingInput.equals("5")) {
                userRating = Integer.parseInt(userRatingInput);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                return;
            }
            Rating rating = new Rating(GAME_NAME, player1Name, userRating, new Date());
            ratingService.setRating(rating);
            System.out.println("Thank you for your rating: " + userRating);
        }
    }




    private void reload() {
        System.out.println("Do you want to play again? (Y/N)");
        String input = scanner.nextLine().toUpperCase();

        if (input.equals("Y")) {
            field.setState(FieldState.MENU);
            field.restScore();
            field.generate();
            field.currentPlayer = TileState.PLAYER1;
            play();
        } else {
            System.out.println("Thank you for playing! Goodbye!");
            System.exit(0);
        }
    }

    private void leaveComment() {
        System.out.println("Would you like to leave a comment about the game? (Y/N)");
        String input = scanner.nextLine().toUpperCase();

        if (input.equals("Y")) {
            System.out.println("Please enter your comment:");
            String commentText = scanner.nextLine();
            Comment comment = new Comment(GAME_NAME, player1Name, commentText, new Date());
            commentService.addComment(comment);
            System.out.println("Thank you for your feedback: \"" + commentText + "\"");
        } else {
            System.out.println("No problem! Thanks for playing!");
        }
    }


    private void printScores() {
        System.out.println("Scores:");
        System.out.println(player1Name + " (Player 1): " + field.getPlayer1Score());
        Score score = new Score(GAME_NAME, player1Name, field.getPlayer1Score(), new Date());
        scoreService.addScore(score);

        if (field.getMode() == GameMode.PvP) {
            System.out.println(player2Name + " (Player 2): " + field.getPlayer2Score());
        }

    }




    private void prinResult() {
        if (field.getState() == FieldState.WIN) {
            if (field.getWinner() == TileState.PLAYER1) {
                System.out.println("Congratulations! Player 1 wins! ");
            } else if (field.getWinner() == TileState.PLAYER2) {
                System.out.println("Congratulations! Player 2 wins! ");
            }
        } else if (field.getState() == FieldState.FAIL) {
            System.out.println("You lost! The bot wins. ");
        } else if (field.getState() == FieldState.DRAW) {
            System.out.println("It's a draw! ");
        }
    }


    private void printField() {
        if (field.getState() == FieldState.PLAYING) {
            System.out.println();
            System.out.println();
            int rows = field.getRowCount();
            int columns = field.getColumnCount();

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    var tile = field.getTile(row, column);
                    printTile(tile);
                    if (column < columns) {
                        System.out.print("|");
                    }
                }
                System.out.println();
            }
            System.out.print(" |");
            for (int col = 1; col < field.getColumnCount(); col++) {
                System.out.print((col) + "|");
            }

            System.out.println();
        }
    }
    private static void printTile(Tile tile) {
        switch (tile.getState()) {
            case EMPTY:
                System.out.print(" ");
                break;
            case PLAYER1:
                System.out.print("\u001B[31mX\u001B[0m");
                break;
            case PLAYER2:
                System.out.print("\u001B[34mO\u001B[0m");
                break;
        }
    }

    private void processInput() {
        if (field.getState() == FieldState.PLAYING) {
            System.out.println("Commands: (X - Exit ❌ , 1-7 Choose a column )");
            var line = scanner.nextLine().toUpperCase();

            if (line.equals("X")) {
                System.exit(0);
            } else if (line.matches("^[1-7]$")) {
                int column = Integer.parseInt(line);
                field.dropDisc(column);
            } else {
                System.out.println("Invalid input");
            }
        }

        if (field.getState() == FieldState.MENU) {
            System.out.println("========================================");
            System.out.println("            Welcome to Connect Four!     ");
            System.out.println("========================================");
            System.out.println("Please choose an option to start:");
            System.out.println("========================================");
            System.out.println("P - Player vs Player (PvP)");
            System.out.println("B - Player vs Bot (PvB)");
            System.out.println("========================================");
            System.out.println("L - Leaderboard (Top 10)");
            System.out.println("C - Comments");
            System.out.println("X - Exit");
            System.out.println("========================================");
            System.out.println("            Average Rating: " + getAverageRating());
            System.out.println("========================================");
            System.out.print("Your choice: ");




            var line = scanner.nextLine().toUpperCase();
            ScoreService scoreService = new ScoreServiceJDBC();

            switch (line) {
                case "P":
                    field.setMode(GameMode.PvP);
                    askForPlayerNames();
                    field.setState(FieldState.PLAYING);
                    System.out.println("You have selected Player vs Player (PvP).");
                    break;
                case "B":
                    field.setMode(GameMode.PvB);
                    askForPlayerNames();
                    field.setState(FieldState.PLAYING);
                    System.out.println("You have selected Player vs Bot (PvB).");
                    break;
                case "L":
                    System.out.println("===================== Top 10 Players =====================");
                    List<Score> topScores = scoreService.getTopScores("Connect Four");
                        int rank = 1;
                        for (Score score : topScores) {
                            System.out.printf("%d. %s - %d points (played on %s)%n",
                                    rank++, score.getPlayer(), score.getPoints(), score.getPlayedOn());
                        }
                    System.out.println("==========================================================");
                    break;
                case "C":
                    System.out.println("===========================================================");
                    List<Comment> lastComment = commentService.getComments("Connect Four");
                    for (Comment comment : lastComment) {
                        System.out.printf("%s - %s (%s)%n",
                                comment.getPlayer(), comment.getComment(), comment.getCommentedOn());
                    }
                    System.out.println("==========================================================");
                    break;
                case "X":
                    System.out.println("Exiting... Thank you for playing!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }

    }
    private void askForPlayerNames() {
        System.out.println("========================================");
        System.out.println("        Enter Player Names               ");
        System.out.println("========================================");
        System.out.print("Enter Player 1 name: ");
        player1Name = scanner.nextLine();

        if (field.getMode() == GameMode.PvP) {
            System.out.print("Enter Player 2 name: ");
            player2Name = scanner.nextLine();
        }
        System.out.println("========================================");
    }

}
