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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoviecrawlerApplication.class)
@WebAppConfiguration
public class MoviecrawlerApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void test() {
        try {
            URL url = new URL("thunder://QUFodHRwOi8vZGwxMjMuODBzLmltOjkyMC8xNzA2L+eUn2jljbHmnLrvvJpm5LuHL+eUn2jljbHmnLrvvJpm5LuHLm1wNFpa");
            HttpURLConnection  urlcon=(HttpURLConnection)url.openConnection();
            int i;
            i =urlcon.getContentLength();
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
