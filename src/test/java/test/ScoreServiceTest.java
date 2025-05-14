package test;

import sk.tuke.gamestudio.entity.Score;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceTest {

    @Test
    public void testRest(){
        ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
        scoreService.reset();
    }


    @Test
    public void testAddScore() {
        ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
        scoreService.reset();
        scoreService.addScore(new Score("ConnectFour", "Player1", 100, new java.util.Date()));
        assertEquals(1, scoreService.getTopScores("ConnectFour").size());
        assertEquals("Player1", scoreService.getTopScores("ConnectFour").get(0).getPlayer());
        assertEquals(100, scoreService.getTopScores("ConnectFour").get(0).getPoints());
    }

    @Test
    public void testGetTopScores() {
        ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
        scoreService.reset();
        scoreService.addScore(new Score("ConnectFour", "Player1", 100, new java.util.Date()));
        scoreService.addScore(new Score("ConnectFour", "Player2", 200, new java.util.Date()));
        List<Score> topScores = scoreService.getTopScores("ConnectFour");
        assertEquals(2, topScores.size());
        assertEquals("Player2", topScores.get(0).getPlayer());
        assertEquals(200, topScores.get(0).getPoints());
        assertEquals("Player1", topScores.get(1).getPlayer());
        assertEquals(100, topScores.get(1).getPoints());
    }

}