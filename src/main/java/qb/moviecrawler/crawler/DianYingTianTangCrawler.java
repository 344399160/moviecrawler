package qb.moviecrawler.crawler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.EnumConst;
import qb.moviecrawler.common.UserAgentUtils;
import qb.moviecrawler.database.model.DownloadLink;
import qb.moviecrawler.database.model.Movie;
import qb.moviecrawler.database.repository.DownloadLinkRepository;
import qb.moviecrawler.database.repository.MovieRepository;
import qb.moviecrawler.database.util.jpa.Criteria;
import qb.moviecrawler.database.util.jpa.Restrictions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 电影天堂
 * Created by 乔斌 on 2017/6/6.
 */
public class DianYingTianTangCrawler implements PageProcessor {

    private MovieRepository movieRepository;

    private DownloadLinkRepository downloadLinkRepository;

    //电影分类
    private String classify;

    //爬取类型（grap 抓取， regrap 错误链接重新爬取）
    private String grapType;

    public DianYingTianTangCrawler(MovieRepository movieRepository, DownloadLinkRepository downloadLinkRepository, String classify, String grapType) {
        this.movieRepository = movieRepository;
        this.classify = classify;
        this.grapType = grapType;
        this.downloadLinkRepository = downloadLinkRepository;
    }

    private int index = 0;
    //根据addUrl去掉index.html, 用于拼接分页链接
    private static String enterPage;

    @Override
    public void process(Page page) {
        //第一次进入只添加分页链接
       if (index == 0 && grapType.equals("grap")) {
           enterPage = page.getUrl().all().get(0);
           enterPage = enterPage.replace("index.html", "");
           //获取分页html名称
           List<String> htmlName = page.getHtml().xpath(Const.PAGE_LINKS_XPATH).all();
           List<String> pageLinks = htmlName.stream().map(pageLink -> {
               return enterPage + CommonUtil.readElement(pageLink, "option", "value");
           }).collect(Collectors.toList());
           //添加分页链接
//           page.addTargetRequests(pageLinks);
           page.addTargetRequest(pageLinks.get(0));
       } else {
           if (!page.getUrl().regex(Const.DETAIL_PAGE_REGIX).match()) { //如果不符合详情页正则添加所有电影标题连接
               List<String> titleLinks = page.getHtml().xpath(Const.MOVIE_LINKS).links().all();
               titleLinks.stream().map(titleLink -> {
                   return Const.MAINPAGE + titleLink;
               }).collect(Collectors.toList());
               page.addTargetRequests(titleLinks);
           } else { //如果符合详情页正则读取电影标题 及 分类
               Movie movie = this.parseContent(page);
               Movie savedMoive = this.existMovie(movie.getName());
               if (null != savedMoive && !CollectionUtils.isEmpty(movie.getLinks())) {   //如果有记录存在， 比较下载链接，如果不存在新链接则保存
                   List<DownloadLink> oldLinks = savedMoive.getLinks();
                   List<DownloadLink> links = movie.getLinks();
                   List<DownloadLink> combineList = new ArrayList<>();
                   if (CollectionUtils.isEmpty(oldLinks)) {
                       savedMoive.setLinks(links);
                       savedMoive.setLastTime(new Date());
                       movieRepository.save(savedMoive);
                   }
                   if (!CollectionUtils.isEmpty(oldLinks)) {
                       //查询库里相同id记录是否有该link
                       for (DownloadLink link : links) {
                           if (downloadLinkRepository.linkExist(link.getLink(), savedMoive.getId()) <= 0) {
                               combineList.add(link);
                           }
                       }
                       if (combineList.size() > 0) {
                           combineList.addAll(oldLinks);
                           savedMoive.setLinks(combineList);
                           savedMoive.setLastTime(new Date());
                           movieRepository.save(savedMoive);
                       }
                   }
               } else {
                   movieRepository.save(movie);
               }
           }
        }
        index++;
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(10000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("gb2312")
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }

    private Movie existMovie(String movieName) {
        Criteria<Movie> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("name", movieName));
        return movieRepository.findOne(criteria);
    }

    //解析文章并返回电影实体
    private Movie parseContent(Page page) {
        Movie movie = new Movie();
        //电影名
        String title = page.getHtml().xpath(Const.MOVIE_TITLE_XPATH).get();
        title = StringUtils.isNotEmpty(CommonUtil.getMovieName(title)) ? CommonUtil.getMovieName(title) : title;
        //内容
        String content = page.getHtml().xpath(Const.CONTENT_XPATH).get();
        //发布时间
        String pubTimeDiv = page.getHtml().xpath(Const.PUBTIME_XPATH).get();
        //下载连接
        List<String> links = page.getHtml().xpath(Const.DOWNLOAD_LINKS_XPATH).links().all();
        //年代
        String year = CommonUtil.parseProperty(content, "◎年　　代", "<br>");
        //产地
        String country = CommonUtil.parseProperty(content, "◎产　　地", "<br>");
        if (!StringUtils.isNotEmpty(country)) {
            country = CommonUtil.parseProperty(content, "◎地　　区", "<br>");
        }
        if (!StringUtils.isNotEmpty(country)) {
            country = CommonUtil.parseProperty(content, "◎国　　家", "<br>");
        }
        //类别
        String type = CommonUtil.parseProperty(content, "◎类　　别", "<br>");
        if (!StringUtils.isNotEmpty(type)) {
            type = CommonUtil.parseProperty(content, "◎类　　型", "<br>");
        }
        //字幕
        String subtitle = CommonUtil.parseProperty(content, "◎字　　幕", "<br>");
        //片长
        String filmLength = CommonUtil.parseProperty(content, "◎片　　长", "<br>");
        //导演
        String director = CommonUtil.parseProperty(content, "◎导　　演", "<br>");
        //上映日期
        String releaseDate = CommonUtil.parseProperty(content, "◎上映日期", "<br>");
        if (!StringUtils.isNotEmpty(releaseDate)) {
            releaseDate = CommonUtil.parseProperty(pubTimeDiv, "发布时间：", "&nbsp;");
        }
        //主演
        String actor = null;
        try {
            String actorRegix = "◎主　　演";
            actor = content.substring(content.indexOf(actorRegix) + actorRegix.length() + 1, content.indexOf("◎简　　介"));
            String[] split = actor.split("<br>");
            actor = StringUtils.join(split, ",").trim();
        } catch (Exception e) {
            actor = "";
        }
        //简介
        String abs = null;
        try {
            String absRegix = "◎简　　介";
            abs = content.substring(content.indexOf(absRegix) + absRegix.length(), content.lastIndexOf("<img")).replace("<br>", "");
        } catch (Exception e) {
            abs = "";
        }
        //图片
        String[] imgs = CommonUtil.readElements(content, "img", "src");
        if (null != imgs) {
            if (imgs.length >= 2) {
                movie.setCoverImg(imgs[0]);
                List<String> list = new ArrayList<>();
                for (int i = 1; i < imgs.length; i++) {
                    list.add(imgs[i]);
                }
                movie.setScreenshotImg(StringUtils.join(list, ","));
            } else if (imgs.length == 1){
                String jianjie = "◎简　　介";
                String temp = content.substring(content.indexOf(jianjie) + jianjie.length(), content.length());
                if (temp.indexOf("<img") == -1) {
                    movie.setCoverImg(imgs[0]);
                } else {
                    movie.setScreenshotImg(imgs[0]);
                }
            }
        }
        movie.setFirstTime(new Date());
        movie.setLastTime(new Date());
        movie.setId(UUID.randomUUID().toString());
        movie.setSourceURL(page.getUrl().get());
        movie.setName(title);
        movie.setYear(year);
        movie.setCountry(country);
        movie.setType(type);
        movie.setSubtitle(subtitle);
        movie.setFilmLength(filmLength);
        movie.setDirector(director);
        movie.setActor(actor);
        if (!CollectionUtils.isEmpty(links)) {
            List<DownloadLink> linksList = new ArrayList<>();
            for (String link : links) {
                DownloadLink downloadLink = new DownloadLink();
                downloadLink.setId(UUID.randomUUID().toString());
                downloadLink.setFirstDate(new Date());
                downloadLink.setLink(link);
                linksList.add(downloadLink);
            }
            movie.setLinks(linksList);
        }
        movie.setAbs(abs);
        movie.setClassify(EnumConst.CLASSIFY.get(classify).getNAME());
        movie.setReleaseDate(releaseDate);
        return movie;
    }

}