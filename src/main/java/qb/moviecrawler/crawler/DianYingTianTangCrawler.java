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
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
           enterPage = page.getUrl().all().get(0);
           enterPage = enterPage.replace("index.html", "");
           //获取分页html名称
           List<String> htmlName = page.getHtml().xpath(Const.PAGE_LINKS_XPATH).all();
           List<String> pageLinks = htmlName.stream().map(pageLink -> {
               return enterPage + CommonUtil.readElement(pageLink, "option", "value");
           }).collect(Collectors.toList());
           //添加分页链接
           page.addTargetRequests(pageLinks);
//           page.addTargetRequest(pageLinks.get(0));
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
               movie.setId(UUID.randomUUID().toString());
               movie.setName(CommonUtil.getMovieName(title));
               movie.setYear(year.replace("　",""));
               movie.setCountry(country.replace("　",""));
               movie.setType(type.replace("　",""));
               movie.setSubtitle(subtitle.replace("　",""));
               movie.setFilmLength(filmLength.replace("　",""));
               movie.setDirector(director.replace("　",""));
               movie.setActor(actor.replace("　",""));
               movie.setCoverImg(imgs[0]);
               movie.setScreenshotImg(imgs[1]);
               movie.setLinks(link);
               movie.setAbs(abs);
               movie.setClassify(EnumConst.CLASSIFY.get(classify).getNAME());
               movieRepository.save(movie);
           }
        }
        index++;
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
                .setSleepTime(1000)
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }

}