package qb.moviecrawler.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.EnumConst;
import qb.moviecrawler.common.UserAgentUtils;
import qb.moviecrawler.database.model.Movie;
import qb.moviecrawler.database.repository.MovieRepository;
import qb.moviecrawler.database.util.jpa.Criteria;
import qb.moviecrawler.database.util.jpa.Restrictions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
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

    //电影分类
    private String classify;

    private int count;

    public DianYingTianTangCrawler(MovieRepository movieRepository, String classify) {
        this.movieRepository = movieRepository;
        this.classify = classify;
    }

    private int index = 0;
    //根据addUrl去掉index.html, 用于拼接分页链接
    private static String enterPage;

    @Override
    public void process(Page page) {
        //第一次进入只添加分页链接
       if (index == 0) {
           count = this.getClassifyCount();
           enterPage = page.getUrl().all().get(0);
           enterPage = enterPage.replace("index.html", "");
           //获取分页html名称
           List<String> htmlName = page.getHtml().xpath(Const.PAGE_LINKS_XPATH).all();
           List<String> pageLinks = htmlName.stream().map(pageLink -> {
               return enterPage + CommonUtil.readElement(pageLink, "option", "value");
           }).collect(Collectors.toList());
           //添加分页链接
           page.addTargetRequests(pageLinks);
       } else {
           if (!page.getUrl().regex(Const.DETAIL_PAGE_REGIX).match()) { //如果不符合详情页正则添加电影标题连接
               List<String> titleLinks = page.getHtml().xpath(Const.MOVIE_LINKS).links().all();
               titleLinks.stream().map(titleLink -> {
                   return Const.MAINPAGE + titleLink;
               }).collect(Collectors.toList());
               page.addTargetRequests(titleLinks);
           } else { //如果符合详情页正则读取电影标题 及 分类
               Movie movie = new Movie();
               //电影名
               String title = page.getHtml().xpath("//div[@class='co_area2']/div/h1/font/text()").get();
               //内容
               String content = page.getHtml().xpath("//div[@id='Zoom']/span/p").get();
               //下载连接
               String link = page.getHtml().xpath("//div[@id='Zoom']/span/table/tbody/tr/td/a").links().get();
               //年代
               String year = CommonUtil.parseProperty(content, "◎年　　代");
               //产地
               String country = CommonUtil.parseProperty(content, "◎产　　地");
               //类别
               String type = CommonUtil.parseProperty(content, "◎类　　别");
               //字幕
               String subtitle = CommonUtil.parseProperty(content, "◎字　　幕");
               //片长
               String filmLength = CommonUtil.parseProperty(content, "◎片　　长");
               //导演
               String director = CommonUtil.parseProperty(content, "◎导　　演");
               //上映日期
               String releaseDate = CommonUtil.parseProperty(content, "◎上映日期");
               //主演
               String actor = null;
               try {
                   String actorRegix = "◎主　　演";
                   actor = content.substring(content.indexOf(actorRegix) + actorRegix.length() + 1, content.indexOf("◎简　　介"));
                   String[] split = actor.split("<br>");
                   actor = StringUtils.join(split, ";").trim();
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
                       movie.setScreenshotImg(StringUtils.join(list, ";"));
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
               if (count > 0) {
                   //更新
                   Criteria<Movie> criteria = new Criteria<>();
                   criteria.add(Restrictions.eq("name", CommonUtil.getMovieName(title)));
                   Movie one = movieRepository.findOne(criteria);
                   if (null != one) {
                       movie.setId(one.getId());
                       movie.setFirstTime(one.getFirstTime());
                       movie.setLastTime(new Date());
                   }
               } else {
                   movie.setFirstTime(new Date());
                   movie.setLastTime(new Date());
                   movie.setId(UUID.randomUUID().toString());
               }
               movie.setName(CommonUtil.getMovieName(title));
               movie.setYear(year);
               movie.setCountry(country);
               movie.setType(type);
               movie.setSubtitle(subtitle);
               movie.setFilmLength(filmLength);
               movie.setDirector(director);
               movie.setActor(actor);
               movie.setLinks(link);
               movie.setAbs(abs);
               movie.setClassify(EnumConst.CLASSIFY.get(classify).getNAME());
               movie.setReleaseDate(releaseDate);
               movieRepository.save(movie);
           }
        }
        index++;
    }

    private int getClassifyCount() {
        try {
            Criteria<Movie> criteria = new Criteria<>();
            criteria.add(Restrictions.eq("classify", EnumConst.CLASSIFY.get(classify).NAME));
            int count = movieRepository.findAll(criteria).size();
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(10000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("gb2312")
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }

}