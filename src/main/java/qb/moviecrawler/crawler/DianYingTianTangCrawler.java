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
               //内容
               String content = page.getHtml().xpath("//div[@id='Zoom']/span/p").get();
               //下载连接
               String link = page.getHtml().xpath("//div[@id='Zoom']/span/table/tbody/tr/td/a").links().get();

               String year = CommonUtil.parseProperty(content, "◎年　　代");
               String country = CommonUtil.parseProperty(content, "◎产　　地");
               String type = CommonUtil.parseProperty(content, "◎类　　别");
               String subtitle = CommonUtil.parseProperty(content, "◎字　　幕");
               String filmLength = CommonUtil.parseProperty(content, "◎片　　长");
               String director = CommonUtil.parseProperty(content, "◎导　　演");
               String actorRegix = "◎主　　演";
               String actor = content.substring(content.indexOf(actorRegix) + actorRegix.length() + 1, content.indexOf("◎简　　介"));
               System.out.println(actor.trim());
               String absRegix = "◎简　　介";
               String abs = content.substring(content.indexOf(absRegix) + absRegix.length() + 1, content.indexOf("</p>"));
               System.out.println(abs.trim());

               String[] imgs = CommonUtil.readElements(content, "img", "src");
               System.out.println("封面："+imgs[0]);
               System.out.println("截图"+imgs[1]);

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