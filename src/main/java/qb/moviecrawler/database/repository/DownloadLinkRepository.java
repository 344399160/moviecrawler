package qb.moviecrawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import qb.moviecrawler.database.model.DownloadLink;

/**
 * 功能：
 * Created by 乔斌 on 2017/6/15.
 */
public interface DownloadLinkRepository extends JpaRepository<DownloadLink, String>, PagingAndSortingRepository<DownloadLink, String>, JpaSpecificationExecutor<DownloadLink> {

    @Query(value="select count(link) from download_links where link = :link and movie_id= :movieId", nativeQuery=true)
    int linkExist(@Param("link") String link, @Param("movieId") String movieId);
}
