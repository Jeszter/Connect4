package testjpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

@SpringBootTest(classes = SpringClient.class)
@Transactional
public class RatingServiceJPATest {

    @Autowired
    private RatingService ratingService;

    @Test
    public void testSetRating() {
        Rating rating = new Rating("TestGame", "Player1", 4, new java.util.Date());
        ratingService.setRating(rating);

        int actualRating = ratingService.getRating("TestGame", "Player1");

        Assertions.assertEquals(4, actualRating);
    }

    @Test
    public void testGetAverageRating() {
        ratingService.setRating(new Rating("TestGame", "Player1", 4, new java.util.Date()));
        ratingService.setRating(new Rating("TestGame", "Player2", 3, new java.util.Date()));
        ratingService.setRating(new Rating("TestGame", "Player3", 5, new java.util.Date()));

        int averageRating = ratingService.getAverageRating("TestGame");

        Assertions.assertEquals(4, averageRating);
    }

    @Test
    public void testGetRatingForPlayer() {
        ratingService.setRating(new Rating("TestGame", "Player1", 4, new java.util.Date()));

        int playerRating = ratingService.getRating("TestGame", "Player1");

        Assertions.assertEquals(4, playerRating);
    }

    @Test
    public void testResetRatings() {
        ratingService.setRating(new Rating("TestGame", "Player1", 4, new java.util.Date()));
        ratingService.setRating(new Rating("TestGame", "Player2", 3, new java.util.Date()));

        ratingService.reset();

        int averageRating = ratingService.getAverageRating("TestGame");

        Assertions.assertEquals(0, averageRating);
    }
}
