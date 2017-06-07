package qb.moviecrawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import qb.moviecrawler.database.model.Agency;

/**
 * 功能：无忧代理爬取
 * Created by 乔斌 on 2017/6/7.
 */
@Component
public interface AgencyRepository extends JpaRepository<Agency, String>, PagingAndSortingRepository<Agency, String> {
}
