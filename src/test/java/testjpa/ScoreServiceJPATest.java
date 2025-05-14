package testjpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = SpringClient.class)
@Transactional

public class ScoreServiceJPATest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void testAddScore() {
        Score score = new Score("TestGame", "NewPlayer", 250, new Date());
        scoreService.addScore(score);

        List<Score> scores = scoreService.getTopScores("TestGame");
        Assertions.assertFalse(scores.isEmpty());
        Assertions.assertEquals(250, scores.get(0).getPoints());
    }

    @Test
    public void testGetTopScores() {
        scoreService.addScore(new Score("TestGame", "PlayerOne", 80, new Date()));
        scoreService.addScore(new Score("TestGame", "PlayerTwo", 300, new Date()));
        scoreService.addScore(new Score("TestGame", "PlayerThree", 120, new Date()));

        List<Score> scores = scoreService.getTopScores("TestGame");
        Assertions.assertEquals(3, scores.size());
        Assertions.assertEquals(300, scores.get(0).getPoints());
    }

    @Test
    public void testReset() {
        scoreService.addScore(new Score("TestGame", "PlayerA", 60, new Date()));
        scoreService.addScore(new Score("TestGame", "PlayerB", 120, new Date()));
        scoreService.addScore(new Score("TestGame", "PlayerC", 180, new Date()));

        scoreService.reset();
        List<Score> scores = scoreService.getTopScores("TestGame");
        Assertions.assertTrue(scores.isEmpty());
    }

    @Test
    public void testAddMultipleScores() {
        scoreService.addScore(new Score("TestGame", "PlayerD", 200, new Date(1672531199000L)));
        scoreService.addScore(new Score("TestGame", "PlayerD", 300, new Date()));

        List<Score> scores = scoreService.getTopScores("TestGame");
        Assertions.assertEquals(2, scores.size());
        Assertions.assertEquals(300, scores.get(0).getPoints());
    }
}
