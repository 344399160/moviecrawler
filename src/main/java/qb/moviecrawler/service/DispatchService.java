package qb.moviecrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.crawler.WuYouAgencyCrawler;
import qb.moviecrawler.database.repository.AgencyRepository;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * 功能：任务分配
 * Created by 乔斌 on 2017/6/7.
 */
@Service
public class DispatchService {

    @Autowired
    private AgencyRepository repository;

    /**
     * 功能描述：代理信息爬取
     * @author qiaobin
     * @param
     */
    public void grapAgencies() {
        HttpClientDownloader httpClientDownloader = CommonUtil.makeProxy(repository.findAll());
        Spider spider = Spider.create(new WuYouAgencyCrawler(repository)).addUrl(Const.WUYOU_WEBSITE);
        if (null != httpClientDownloader) {
            spider.setDownloader(httpClientDownloader);
        }
        spider.thread(1).run();

    }
}
