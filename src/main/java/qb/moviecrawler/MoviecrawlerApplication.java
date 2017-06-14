package qb.moviecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoviecrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviecrawlerApplication.class, args);
	}
}
