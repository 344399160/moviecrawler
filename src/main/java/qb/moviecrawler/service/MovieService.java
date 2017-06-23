package qb.moviecrawler.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qb.moviecrawler.common.*;
import qb.moviecrawler.crawler.DianYingTianTangCrawler;
import qb.moviecrawler.crawler.EightsCrawler;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.model.DownloadLink;
import qb.moviecrawler.database.model.Movie;
import qb.moviecrawler.database.repository.DownloadLinkRepository;
import qb.moviecrawler.database.repository.MovieRepository;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Autowired
    private DownloadLinkRepository downloadLinkRepository;


    /**
     * 功能描述：爬取电影信息
     * @author qiaobin
     * @param
     */
    public void grapMovieDetail() {
        String logName = CommonUtil.getLogName();
        File logFile = new File(logName);
        Map<String, String> map = Const.DIANYINGTIANTANG_MAP;
        for (String classify : map.keySet()) {
            Spider.create(new DianYingTianTangCrawler(movieRepository, downloadLinkRepository, "grap"))
                    .addUrl(map.get(classify))
                    .thread(2).run();
            //爬取结束后, 解析错误日志，将未执行成功url取出重新爬取
            this.reGrapDetailFromLog(logFile);
        }
    }

    /**
     * 功能描述：解析错误日志中的失败url并重新爬取
     * @author qiaobin
     * @param
     */
    public void reGrapDetailFromLog(File logFile) {
        String[] pages = CommonUtil.parseLog(logFile);
        logFile.delete();
        if (null != pages) {
            HttpClientDownloader httpClientDownloader = this.getDownloader();
            Spider spider = Spider.create(new DianYingTianTangCrawler(movieRepository, downloadLinkRepository, "regrap"))
                    .addUrl(pages);
            if (null != httpClientDownloader) {
                spider.setDownloader(httpClientDownloader);
            }
            spider.thread(2).run();
        }
    }

    /**
     * 功能描述：爬取80s电影网电影信息
     * @author qiaobin
     * @param
     */
    public void grap80sMovieDetail() {
        String logName = CommonUtil.getLogName();
        File logFile = new File(logName);
        Spider.create(new EightsCrawler(movieRepository, downloadLinkRepository, "grap")).addUrl("http://www.80s.tw/movie/list").thread(2).run();
        //爬取结束后, 解析错误日志，将未执行成功url取出重新爬取
//        this.reGrap80sDetailFromLog(logFile);
    }

    /**
     * 功能描述：解析错误日志中的失败url并重新爬取 - 80s
     * @author qiaobin
     * @param
     */
    public void reGrap80sDetailFromLog(File logFile) {
        String[] pages = CommonUtil.parseLog(logFile);
        logFile.delete();
        if (null != pages) {
            HttpClientDownloader httpClientDownloader = this.getDownloader();
            Spider spider = Spider.create(new EightsCrawler(movieRepository, downloadLinkRepository, "regrap"))
                    .addUrl(pages);
            if (null != httpClientDownloader) {
                spider.setDownloader(httpClientDownloader);
            }
            spider.thread(2).run();
        }
    }

    /**
     * 功能描述：获取代理
     * @author qiaobin
     * @param
     */
    private HttpClientDownloader getDownloader() {
        List<Agency> agencies = dispatchService.getAgencies(50);
        List<Agency> usefulAgencies = new ArrayList<>();
        for (Agency agency : agencies) {
            if (CheckIPUtils.checkValidIP(agency.getIp(), Integer.parseInt(agency.getPort()))) {
                usefulAgencies.add(agency);
            }
        }
        HttpClientDownloader httpClientDownloader = null;
        if (usefulAgencies.size() > 0) {
            Proxy[] proxies = new Proxy[usefulAgencies.size()];
            for (int i = 0; i < usefulAgencies.size(); i++) {
                Agency agency = usefulAgencies.get(i);
                proxies[i] = new Proxy(agency.getIp(), Integer.parseInt(agency.getPort()));
            }
            httpClientDownloader = new HttpClientDownloader();
            SimpleProxyProvider proxyProvider = SimpleProxyProvider.from();
            httpClientDownloader.setProxyProvider(proxyProvider);

        }
        return httpClientDownloader;
    }
}
