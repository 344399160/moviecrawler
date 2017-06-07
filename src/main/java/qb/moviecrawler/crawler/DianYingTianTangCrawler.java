package qb.moviecrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 电影天堂
 * Created by 乔斌 on 2017/6/6.
 */
public class DianYingTianTangCrawler implements PageProcessor {

    private int index = 0;
    //根据addUrl去掉index.html, 用于拼接分页链接
    private static String enterPage;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

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
//           page.addTargetRequests(pageLinks);
           page.addTargetRequest(pageLinks.get(0));
       } else {
           if (!page.getUrl().regex(Const.DETAIL_PAGE_REGIX).match()) { //如果不符合详情页正则添加电影标题连接
               List<String> titleLinks = page.getHtml().xpath(Const.MOVIE_LINKS).links().all();
               titleLinks.stream().map(titleLink -> {
                   return Const.MAINPAGE + titleLink;
               }).collect(Collectors.toList());
               page.addTargetRequests(titleLinks);
           } else { //如果符合详情页正则读取电影标题 及 分类
               //电影名
               String title = page.getHtml().xpath("//div[@class='co_area2']/div/h1/font/text()").get();
               String content = page.getHtml().xpath("//div[@id='Zoom']/span/p").get();
               String link = page.getHtml().xpath("//div[@id='Zoom']/span/table/tbody/tr/td/a").get();
               Document document = page.getHtml().getDocument();
               Elements elements = document.select("a");
               for (Element element : elements) {
                   //抓取到URL
                   String movieURL = element.attr("abs:href");
                   //提取到有用的的URL
                   if (movieURL.contains("thread") && movieURL.contains("html") && !movieURL.contains("83494")) {
                       //将抓取到的URL添加到List中
                       System.out.println(movieURL);
                   }
               }
               System.out.println(CommonUtil.getMovieName(title));
               System.out.println(content);
               System.out.println(link);
           }
        }
        index++;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DianYingTianTangCrawler()).addUrl("http://www.ygdy8.net/html/gndy/oumei/index.html").thread(1).run();
    }
}