package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.config.JwtTokenProvider;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.enums.FieldState;
import sk.tuke.gamestudio.game.enums.GameMode;
import sk.tuke.gamestudio.game.enums.TileState;
import sk.tuke.gamestudio.game.game.Bot;
import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.service.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/connect4")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class Connect4Controller {
    private Field field = new Field(6, 8);
    private Bot bot;

    private String player1Name = "Player 1";
    private String player2Name = "Player 2";
    private String player1Skin = "classic";
    private String player2Skin = "classic";

    private static final String GAME_NAME = "Connect Four";

    private static final Map<String, Map<String, String>> SKIN_COLORS = new HashMap<>();
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }



    @GetMapping("/login")
    public String showLoginForm() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username,
                                          @RequestParam String email,
                                          @RequestParam String password) {
        try {
            User user = new User(username, password, email);
            userService.register(user);
            return ResponseEntity.ok("Registration successful");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username,
                                       @RequestParam String password) {
        try {
            User user = userService.login(username, password);
            String token = tokenProvider.generateToken(user.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UserException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(e.getMessage());
        }
    }
}
    private User loggedUser;



    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/connect4/login";
    }


    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    @RequestMapping
    public String connect4(@RequestParam(required = false) Integer column) {
        if (column != null && field.getState() == FieldState.PLAYING) {
            field.dropDisc(column - 1);
            field.result();

            if (field.getState() != FieldState.PLAYING) {
                printResult();
            } else if (field.getMode() == GameMode.PvB && field.currentPlayer == TileState.PLAYER2) {
                bot.makeMove();
                field.result();
                if (field.getState() != FieldState.PLAYING) {
                    printResult();
                }
            }
        }
        return "game";
    }


    @RequestMapping("/set")
    public String redirectToStart() {
        return "set";
    }

    @RequestMapping("/set-mode")
    public String setMode(@RequestParam String mode) {
        if ("PvP".equals(mode)) {
            field.setMode(GameMode.PvP);
        } else if ("PvB".equals(mode)) {
            field.setMode(GameMode.PvB);
            player2Name = "Bot";
            bot = new Bot(field);
        }
        return "game";
    }

    @RequestMapping("/set-players-pvp")
    public String setPlayers(@RequestParam(required = false) String player1,
                             @RequestParam(required = false) String player2) {

        this.player1Name = (player1 != null && !player1.isEmpty()) ? player1 : "Player 1";
        this.player2Name = (player2 != null && !player2.isEmpty()) ? player2 : "Player 2";

        System.out.println("Player 1: " + player1Name + ", Player 2: " + player2Name);
        return "redirect:/connect4/new?mode=PvP";
    }

    @RequestMapping("/set-players-pvb")
    public String setPlayersPvB(@RequestParam String player1, @RequestParam String player2) {

        this.player1Name = (player1 != null && !player1.isEmpty()) ? player1 : "Player 1";
        this.player2Name = (player2 != null && !player2.isEmpty()) ? player2 : "Bot";
        System.out.println("Player 1: " + player1Name + ", Player 2: " + player2Name);
        return "redirect:/connect4/new?mode=PvB";
    }


    @RequestMapping("/new")
    public String newGame(@RequestParam(required = false) String mode) {
        field = new Field(6, 7);
        field.setState(FieldState.PLAYING);

        if ("PvP".equals(mode)) {
            field.setMode(GameMode.PvP);
        } else if ("PvB".equals(mode)) {
            field.setMode(GameMode.PvB);
            player2Name = "Bot";
            bot = new Bot(field);
        }

        return "redirect:/connect4";
    }

    @RequestMapping("/menu")
    public String menu() {
        field.setState(FieldState.MENU);
        return "menu";
    }



    @GetMapping("/menu")
    public String menu(Model model) {
        int averageRating = ratingService.getAverageRating("Connect Four");
        model.addAttribute("averageRating", averageRating);
        field.setState(FieldState.MENU);
        return "menu";
    }


    @RequestMapping("/")
    public String game() {
        field.setState(FieldState.MENU);
        return "game";
    }



    public FieldState getGameState() {
        return field.getState();
    }

    public GameMode getGameMode() {
        return field.getMode();
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getPlayer1Score() {
        return field.getPlayer1Score();
    }

    public int getPlayer2Score() {
        return field.getPlayer2Score();
    }


    @GetMapping("/getPlayerScores")
    @ResponseBody
    public Map<String, Integer> getPlayerScores() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("player1", field.getPlayer1Score());
        scores.put("player2", field.getPlayer2Score());
        return scores;
    }

    @GetMapping("/getGameStatus")
    @ResponseBody
    public String getGameStatus() {
        return getGameStatusMessage();
    }


    public String getGameStatusMessage() {
        switch (field.getState()) {
            case PLAYING:
                return "Current player: " + (field.currentPlayer == TileState.PLAYER1 ? player1Name : player2Name);
            case WIN:
                return (field.getWinner() == TileState.PLAYER1 ? player1Name : player2Name) + " wins!";
            case FAIL:
                return "Bot wins!";
            case DRAW:
                return "It's a draw!";
            case MENU:
                return "Welcome to Connect Four!";
            default:
                return "";
        }
    }

    @RequestMapping("/set-skins")
    @ResponseBody
    public String setSkins(@RequestParam String player1Skin, @RequestParam String player2Skin) {
        this.player1Skin = player1Skin;
        this.player2Skin = player2Skin;
        return "OK";
    }

    @RequestMapping("/get-skins")
    @ResponseBody
    public Map<String, String> getSkins() {
        Map<String, String> skins = new HashMap<>();
        skins.put("player1Skin", player1Skin);
        skins.put("player2Skin", player2Skin);
        return skins;
    }

    public String getHtmlField() {
        return HtmlFieldRenderer.render(field, player1Skin, player2Skin);
    }


    private void printResult() {
        if (field.getState() == FieldState.WIN) {
            System.out.println("Winner: " + (field.getWinner() == TileState.PLAYER1 ? player1Name : player2Name));
            printScores();
        } else if (field.getState() == FieldState.FAIL) {
            System.out.println("Bot wins!");
            printScores();
        } else if (field.getState() == FieldState.DRAW) {
            System.out.println("Draw!");
            printScores();
        }
    }

    private void printScores() {
        Score score = new Score(GAME_NAME, player1Name, field.getPlayer1Score(), new Date());
        scoreService.addScore(score);

        System.out.println("Score: " + player1Name + " = " + field.getPlayer1Score());

        if (field.getMode() == GameMode.PvP) {
            System.out.println("Score: " + player2Name + " = " + field.getPlayer2Score());
        }
    }

    @RequestMapping("/comments")
    public String showComments(Model model) {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        model.addAttribute("comments", comments);
        return "comments";
    }

    @RequestMapping("/scores")
    public String showScores(Model model) {
        List<Score> scores = scoreService.getTopScores(GAME_NAME);
        model.addAttribute("scores", scores);
        return "leaderboard";
    }

    @PostMapping("/comment")
    @ResponseBody
    public String addComment(@RequestParam("commentText") String commentText) {
        if (commentText == null || commentText.trim().isEmpty()) {
            return "Comment cannot be empty";
        }

        Comment comment = new Comment(GAME_NAME, player1Name, commentText.trim(), new Date());
        commentService.addComment(comment);

        return "OK";
    }

    @PostMapping("/rating")
    @ResponseBody
    public String addRating(@RequestParam("userRating") int userRating) {
        if (userRating < 1 || userRating > 5) {
            return "Invalid rating value";
        }

        Rating rating = new Rating(GAME_NAME, player1Name, userRating, new Date());
        ratingService.setRating(rating);

        System.out.println("Thank you for your rating: " + userRating);
        return "OK";
    }




}
