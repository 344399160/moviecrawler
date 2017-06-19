package qb.moviecrawler.crawler;

import org.apache.commons.lang3.StringUtils;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.UserAgentUtils;
import qb.moviecrawler.database.model.Movie;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能：80s网站爬取
 * Created by 乔斌 on 2017/6/19.
 */
public class EightsCrawler implements PageProcessor {

    private String movieMainPage = "http://www.80s.tw/movie/list";

    private String mainPage = "http://www.80s.tw";

    private int index = 0;

    @Override
    public void process(Page page) {
        if (index == 0) {
            List<String> pageLinks = new ArrayList<>();
            String lastPagePath = page.getHtml().xpath(Const.EIGHTS_LAST_PAGE_XPATH).links().get();
            int lastPage = Integer.parseInt(lastPagePath.substring(lastPagePath.lastIndexOf("p") + 1, lastPagePath.length()));
            for (int i = 1; i <= lastPage; i++) {
                pageLinks.add(String.format("%s/-----p%s", movieMainPage, i));
            }
//            page.addTargetRequests(pageLinks);
            page.addTargetRequest(pageLinks.get(0));
        } else {
            if (page.getUrl().regex(Const.EIGHTS_PAGE_REGIX).match()) {  //匹配分页链接
                List<String> titleLinks = page.getHtml().xpath(Const.EIGHTS_PAGE_LINKS_XPATH).links().all();
                titleLinks.stream().map(titleLink -> {
                    return mainPage + titleLink;
                }).collect(Collectors.toList());
                page.addTargetRequests(titleLinks);
            } else { //详情页
                parseContent(page);
            }
        }
        index++;
    }

    private Movie parseContent(Page page) {
        Movie movie = new Movie();
        //电影名
        String title = CommonUtil.getValue(page, Const.EIGHTS_MOVIE_TITLE_XPATH);
        //别名
        String alias = CommonUtil.getValue(page, Const.EIGHTS_ALIAS_XPATH);
        if (StringUtils.isNotEmpty(alias)) {
            alias = StringUtils.join(alias.split(","), "/").trim().replace(" ", "");
            title = String.format("%s/%s", title, alias);
        }
        //海报图片
        String coverImg = CommonUtil.getValue(page, Const.EIGHTS_COVER_IMG_XPATH);
        //截图
        String screenShotImg = CommonUtil.getValue(page, Const.EIGHTS_SCREENSHOT_IMG_XPATH);
        //概述
        String describe = CommonUtil.getValue(page, Const.EIGHTS_DESCRIBE_XPATH);
        //导演
        String director = CommonUtil.getValue(page, Const.EIGHTS_DIRECTOR_XPATH);
        //上映时间
        String releaseDate = CommonUtil.getValue(page, Const.EIGHTS_RELEASEDATE_XPATH);
        //片长
        String filmLength = CommonUtil.getValue(page, Const.EIGHTS_FILMLENGTH_XPATH);
        //评分
        String score = CommonUtil.getValue(page, Const.EIGHTS_SCORE_XPATH);
        //主演
        List<String> actors = CommonUtil.getValues(page, Const.EIGHTS_ACTOR_XPATH);
        //类型
        List<String> types = CommonUtil.getValues(page, Const.EIGHTS_TYPE_XPATH);
        String type = StringUtils.join(types, "/");
        //地区
        List<String> countryList = CommonUtil.getValues(page, Const.EIGHTS_AREA_XPATH);
        countryList.remove("豆瓣短评 ");
        String country = StringUtils.join(countryList, "/");
        //内容
        String content = CommonUtil.getValue(page, Const.EIGHTS_CONTENT_XPATH);

        return movie;
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
