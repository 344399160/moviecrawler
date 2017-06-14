package qb.moviecrawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.crawler.WuYouAgencyCrawler;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.repository.AgencyRepository;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.List;

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
//    @Scheduled(cron = "*/5 * * * * ?")
    public void grapAgencies() {
        System.out.println("定时启动");
        Spider spider = Spider.create(new WuYouAgencyCrawler(repository)).addUrl(Const.WUYOU_WEBSITE);
        spider.thread(5).run();
    }

    /**
     * 功能描述：获取代理
     * @author qiaobin
     * @param
     */
    public List<Agency> getAgencies(int number) {
        Page<Agency> all = repository.findAll(new PageRequest(0, number, null));
        return all.getContent();
    }
}
