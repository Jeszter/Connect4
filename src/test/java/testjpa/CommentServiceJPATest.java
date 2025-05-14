package testjpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = SpringClient.class)
@Transactional
public class CommentServiceJPATest {

    @Autowired
    private CommentService commentService;

    @Test
    public void testAddComment() {
        Comment comment = new Comment("TestGame", "TestPlayer", "Great game!", new Date());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("TestGame");

        Assertions.assertFalse(comments.isEmpty());
        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals("TestPlayer", comments.get(0).getPlayer());
        Assertions.assertEquals("Great game!", comments.get(0).getComment());
    }

    @Test
    public void testGetComment() {
        Date now = new Date();
        commentService.addComment(new Comment("TestGame", "Player2", "Not bad!", now));

        List<Comment> comments = commentService.getComments("TestGame");

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals("Player2", comments.get(0).getPlayer());
        Assertions.assertEquals("Not bad!", comments.get(0).getComment());
    }

    @Test
    public void testReset() {
        commentService.addComment(new Comment("TestGame", "Player1", "Great graphics!", new Date()));
        commentService.addComment(new Comment("TestGame", "Player2", "Really fun to play!", new Date()));

        commentService.reset();

        List<Comment> comments = commentService.getComments("TestGame");

        Assertions.assertTrue(comments.isEmpty());
    }
}
