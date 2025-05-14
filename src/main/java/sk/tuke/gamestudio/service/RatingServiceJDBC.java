package sk.tuke.gamestudio.service;

import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;
import java.util.Random;

@Component
public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    private static final String SELECT_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    private static final String INSERT_RATING = "INSERT INTO rating (ident, player, game, rating, rated_on) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_RATING = "UPDATE rating SET rating = ?, rated_on = ? WHERE game = ? AND player = ?";
    private static final String SELECT_AVG_RATING = "SELECT AVG(rating) FROM rating WHERE game = ?";
    private static final String DELETE_RATINGS = "DELETE FROM rating";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int ident = generateIdent();

            try (PreparedStatement checkStmt = connection.prepareStatement(SELECT_RATING)) {
                checkStmt.setString(1, rating.getGame());
                checkStmt.setString(2, rating.getPlayer());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_RATING)) {
                            updateStmt.setInt(1, rating.getRating());
                            updateStmt.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                            updateStmt.setString(3, rating.getGame());
                            updateStmt.setString(4, rating.getPlayer());
                            updateStmt.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_RATING)) {
                            insertStmt.setInt(1, ident);
                            insertStmt.setString(2, rating.getPlayer());
                            insertStmt.setString(3, rating.getGame());
                            insertStmt.setInt(4, rating.getRating());
                            insertStmt.setTimestamp(5, new Timestamp(rating.getRatedOn().getTime()));
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem inserting or updating rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_AVG_RATING)) {

            ps.setString(1, game);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    return (int) Math.round(avg);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem fetching average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {

            ps.setString(1, game);
            ps.setString(2, player);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem fetching rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = connection.createStatement()) {

            st.executeUpdate(DELETE_RATINGS);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }

    private int generateIdent() {
        return (int)(System.currentTimeMillis() % Integer.MAX_VALUE) + new Random().nextInt(1000);
    }
}
