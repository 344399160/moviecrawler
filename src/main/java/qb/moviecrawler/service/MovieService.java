package qb.moviecrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qb.moviecrawler.common.CheckIPUtils;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.EnumConst;
import qb.moviecrawler.crawler.DianYingTianTangCrawler;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.repository.MovieRepository;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：
 * Created by 乔斌 on 2017/6/13.
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DispatchService dispatchService;
    /**
     * 功能描述：爬取电影信息
     * @author qiaobin
     * @param
     */
    public void grapMovieDetail() {
        List<Agency> agencies = dispatchService.getAgencies(10);
        agencies = agencies.stream().map(agency -> {
            if (CheckIPUtils.checkValidIP(agency.getIp(), Integer.parseInt(agency.getPort()))) {
                return agency;
            } else {
                return null;
            }
        }).collect(Collectors.toList());
        Spider spider = Spider.create(new DianYingTianTangCrawler(movieRepository, EnumConst.CLASSIFY.OUMEIMOVIE.getID()))
                .addUrl("http://www.ygdy8.net/html/gndy/oumei/index.html");
        if (agencies.size() > 0) {
            agencies.toArray(new Agency[agencies.size()]);
            HttpClientDownloader httpClientDownloader = new HttpClientDownloader(); SimpleProxyProvider proxyProvider = SimpleProxyProvider.from(new Proxy("118.178.86.181", 80));
            httpClientDownloader.setProxyProvider(proxyProvider);
            spider.setDownloader(httpClientDownloader);
        }
        spider.thread(2).run();

    }
}
