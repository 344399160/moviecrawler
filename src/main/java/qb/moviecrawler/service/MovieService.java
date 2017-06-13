package qb.moviecrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.EnumConst;
import qb.moviecrawler.crawler.DianYingTianTangCrawler;
import qb.moviecrawler.database.repository.MovieRepository;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * 功能：
 * Created by 乔斌 on 2017/6/13.
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    /**
     * 功能描述：爬取电影信息
     * @author qiaobin
     * @param
     */
    public void grapMovieDetail() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader(); SimpleProxyProvider proxyProvider = SimpleProxyProvider.from(new Proxy("118.178.86.181", 80));
        httpClientDownloader.setProxyProvider(proxyProvider);
        Spider.create(new DianYingTianTangCrawler(movieRepository, EnumConst.CLASSIFY.OUMEIMOVIE.getID()))
                .addUrl("http://www.ygdy8.net/html/gndy/oumei/index.html")
                .setDownloader(httpClientDownloader)
                .thread(1).run();
    }
}
