package test;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {
    @Test
    public void testReset() {
        CommentServiceJDBC commentService = new CommentServiceJDBC();
        commentService.reset();
    }

    @Test
    public void testAddComment() {
        CommentServiceJDBC commentService = new CommentServiceJDBC();
        commentService.reset();
        Comment comment = new Comment("ConnectFour", "Player1", "Great game!", new Date());
        commentService.addComment(comment);
        List<Comment> comments = commentService.getComments("ConnectFour");
        assertEquals(1, comments.size());
        assertEquals("Player1", comments.get(0).getPlayer());
        assertEquals("Great game!", comments.get(0).getComment());
    }

    @Test
    public void testGetComments() {
        CommentServiceJDBC commentService = new CommentServiceJDBC();
        commentService.reset();
        commentService.addComment(new Comment("ConnectFour", "Player1", "Awesome!", new Date()));
        commentService.addComment(new Comment("ConnectFour", "Player2", "Loved it!", new Date()));
        List<Comment> comments = commentService.getComments("ConnectFour");
        assertEquals(2, comments.size());
        assertEquals("Player2", comments.get(0).getPlayer());
        assertEquals("Loved it!", comments.get(0).getComment());
        assertEquals("Player1", comments.get(1).getPlayer());
        assertEquals("Awesome!", comments.get(1).getComment());
    }


}