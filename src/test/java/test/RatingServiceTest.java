package test;

import sk.tuke.gamestudio.entity.Rating;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceTest {
    @Test
    public void testReset() {
        RatingServiceJDBC ratingService = new RatingServiceJDBC();
        ratingService.reset();
    }

    @Test
    public void testSetRating() {
        RatingServiceJDBC ratingService = new RatingServiceJDBC();
        ratingService.reset();
        ratingService.setRating(new Rating("ConnectFour", "Player1", 4, new Date()));
        assertEquals(4, ratingService.getRating("ConnectFour", "Player1"));
    }
    @Test
    public void testUpdateRating() {
        RatingServiceJDBC ratingService = new RatingServiceJDBC();
        ratingService.reset();
        ratingService.setRating(new Rating("ConnectFour", "Player1", 3, new Date()));
        ratingService.setRating(new Rating("ConnectFour", "Player1", 5, new Date()));
        assertEquals(5, ratingService.getRating("ConnectFour", "Player1"));
    }
    @Test
    public void testGetAverageRating() {
        RatingServiceJDBC ratingService = new RatingServiceJDBC();
        ratingService.reset();
        ratingService.setRating(new Rating("ConnectFour", "Player1", 3, new Date()));
        ratingService.setRating(new Rating("ConnectFour", "Player2", 5, new Date()));
        assertEquals(4, ratingService.getAverageRating("ConnectFour"));
    }
}
