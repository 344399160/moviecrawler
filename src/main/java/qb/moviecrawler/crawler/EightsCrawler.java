package qb.moviecrawler.crawler;

import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：80s网站爬取
 * Created by 乔斌 on 2017/6/19.
 */
public class EightsCrawler implements PageProcessor {

    private String mainPage = "http://www.80s.tw/movie/list";

    private int index = 0;

    @Override
    public void process(Page page) {
        if (index == 0) {
            List<String> pageLinks = new ArrayList<>();
            String lastPagePath = page.getHtml().xpath(Const.EIGHTS_LAST_PAGE_XPATH).links().get();
            int lastPage = Integer.parseInt(lastPagePath.substring(lastPagePath.lastIndexOf("p") + 1, lastPagePath.length()));
            for (int i = 1; i <= lastPage; i++) {
                pageLinks.add(String.format("%s/-----p%s", mainPage, i));
            }
            page.addTargetRequests(pageLinks);
        } else {

        }
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(10000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("utf-8")
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new EightsCrawler()).addUrl("http://www.80s.tw/movie/list").thread(2).run();
    }
}
