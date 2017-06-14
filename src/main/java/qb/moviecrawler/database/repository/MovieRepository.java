package qb.moviecrawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import qb.moviecrawler.database.model.Movie;

/**
 * 功能：电影
 * Created by 乔斌 on 2017/6/13.
 */
@Component
public interface MovieRepository extends JpaRepository<Movie, String>, PagingAndSortingRepository<Movie, String>, JpaSpecificationExecutor<Movie> {
}
