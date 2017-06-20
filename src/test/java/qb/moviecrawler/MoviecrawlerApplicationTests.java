package qb.moviecrawler;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import qb.moviecrawler.database.model.DownloadLink;
import qb.moviecrawler.database.model.Movie;
import qb.moviecrawler.database.repository.MovieRepository;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoviecrawlerApplication.class)
@WebAppConfiguration
public class MoviecrawlerApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void test() {
//        Movie movie = new Movie();
//        movie.setId("1");
//        movie.setName("name");
//
//        Set<DownloadLink> list = new HashSet<>();
//        DownloadLink l1 = new DownloadLink();
//        l1.setLink("link");
//        l1.setId("abc");
////        l1.setMovieId("1");
//        list.add(l1);
//        movie.setLinks(list);
//        movieRepository.save(movie);
    }



}
