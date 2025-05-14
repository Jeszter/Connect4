package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        List<Rating> existingRatings = entityManager.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultList();

        if (existingRatings.isEmpty()) {
            entityManager.persist(rating);
        } else {
            Rating existingRating = existingRatings.get(0);
            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(rating.getRatedOn());
            entityManager.merge(existingRating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Double avg = (Double) entityManager.createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
                    .setParameter("game", game)
                    .getSingleResult();
            return avg != null ? (int) Math.round(avg) : 0;
        } catch (Exception e) {
            throw new RatingException("Problem fetching average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Rating rating = (Rating) entityManager.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return rating != null ? rating.getRating() : 0;
        } catch (Exception e) {
            throw new RatingException("Problem fetching rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            entityManager.createQuery("DELETE FROM Rating r").executeUpdate();
        } catch (Exception e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }
}
