package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.ui.ConsoleUI;
import sk.tuke.gamestudio.game.game.Field;
import sk.tuke.gamestudio.service.*;


@SpringBootApplication
@Configuration

@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
        //SpringApplication.run(SpringClient.class);
    }


    // @Bean
    //public CommandLineRunner runner(ConsoleUI ui) {
    //    return args -> ui.play();
    //}

    @Bean
    public ConsoleUI consoleUI(Field field) {
        return new ConsoleUI(field);
    }

    @Bean
    public Field field() {
        return new Field(6,8);
    }

//    @Bean
//    public ScoreService scoreService() {
//         return new ScoreServiceJPA();
//        // return new ScoreServiceRestClient();
//    }
//
//    @Bean
//    public RatingService ratingService() {
//        //return new RatingServiceRestClient();
//        return new RatingServiceJPA();
//    }
//
//
//    @Bean
//    public CommentService commentService() {
//        //return new CommentServiceRestClient();
//        return new CommentServiceJPA();
//    }
//

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserService userService() {
        return new UserServiceJPA();
    }

}
