package qb.moviecrawler.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 功能：全局常量
 * Created by 乔斌 on 2017/6/7.
 */
public class Const {
    //-------------无忧代理-------------
    //无忧代理获取网站
    public final static String WUYOU_WEBSITE = "http://www.data5u.com/";
    //浏览器Header
    public final static String HEADER = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36";
    //无忧代理了网站获取代理列表XPATH
    public final static String WUYOU_AGENCIES_XPATH = "//div[@class='wlist']/li[2]/ul[@class='l2']";

    //-------------电影天堂-------------
    //电影标题连接
    public final static String MOVIE_LINKS = "//div[@class='co_content8']/ul/table/tbody/tr[2]/td[2]/b/a[2]";
    //分页列表(1 - n)  生成例子 ： <option value="list_7_1.html">1</option>
    public final static String PAGE_LINKS_XPATH = "//div[@class='x']/select/option";
    //电影天堂主页
    public final static String MAINPAGE = "http://www.ygdy8.net";
    //详情页匹配正则
    public final static String DETAIL_PAGE_REGIX = "html/[a-z]*/[a-z]*/[0-9]+/[0-9]+.html";
    //电影标题
    public final static String MOVIE_TITLE_XPATH = "//div[@class='co_area2']/div/h1/font/text()";
    //内容XPATH
    public final static String CONTENT_XPATH = "//div[@id='Zoom']/span/p";
    //发布时间XPATH
    public final static String PUBTIME_XPATH = "//div[@class=\"co_content8\"]/ul";
    //下载链接XPATH
    public final static String DOWNLOAD_LINKS_XPATH = "//div[@id='Zoom']/span/table/tbody/tr/td/a";

    //电影天堂爬取页面
    public final static Map<String, String> DIANYINGTIANTANG_MAP = new LinkedHashMap<>();

    static {
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.GUONEIMOVIE.ID, "http://www.ygdy8.net/html/gndy/china/index.html");   //国内电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.OUMEIMOVIE.ID, "http://www.ygdy8.net/html/gndy/oumei/index.html");   //欧美电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.RIHANMOVIE.ID, "http://www.dytt8.net/html/gndy/rihan/index.html");   //日韩电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.HUAYUTV.ID, "http://www.dytt8.net/html/tv/hytv/index.html");   //华语电视
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.OUMEITV.ID, "http://www.dytt8.net/html/tv/oumeitv/index.html");   //欧美电视
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.RIHANTV.ID, "http://www.ygdy8.net/html/tv/rihantv/index.html");   //日韩电视
//        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.LATESTVARIETY.ID, "http://www.dytt8.net/html/zongyi2013/index.html");   //最新综艺
//        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.CARTOON.ID, "http://www.dytt8.net/html/dongman/index.html");   //动漫
    }

    //-------------80s-------------
    public final static String EIGHTS_LAST_PAGE_XPATH = "//div[@class='pager']/a[6]";  //分页最后一页数
    public final static String EIGHTS_PAGE_REGIX = "http://www.80s.tw/movie/list/-----p[0-9]+";  //匹配分页链接正则
    public final static String EIGHTS_PAGE_LINKS_XPATH = "//div[@class='clearfix noborder block1']/ul[2]/li/a";  //每页所有电影链接
    public final static String EIGHTS_MOVIE_TITLE_XPATH = "//div[@class='info']/h1/text()";  //电影名称
    public final static String EIGHTS_COVER_IMG_XPATH = "//div[@id='minfo']/div/img/@src";  //封面图片
    public final static String EIGHTS_DESCRIBE_XPATH = "//div[@class='info']/span[1]/text()";  //概述
    public final static String EIGHTS_ALIAS_XPATH = "//div[@class='info']/span[2]/text()";  //别名
    public final static String EIGHTS_ACTOR_XPATH = "//div[@class='info']/span[3]/a/text()";  //主演
    public final static String EIGHTS_TYPE_XPATH = "//div[@class='clearfix']/span[1]/a/text()";  //类型
    public final static String EIGHTS_AREA_XPATH = "//div[@class='clearfix']/span[2]/a/text()";  //地区
    public final static String EIGHTS_DIRECTOR_XPATH = "//div[@class='clearfix']/span[4]/a/text()";  //导演
    public final static String EIGHTS_RELEASEDATE_XPATH = "//div[@class='clearfix']/span[5]/text()";  //上映时间
    public final static String EIGHTS_FILMLENGTH_XPATH = "//div[@class='clearfix']/span[6]/text()";  //片长
    public final static String EIGHTS_SCORE_XPATH = "//div[@class='clearfix'][2]/span[1]/text()";  //评分
    public final static String EIGHTS_CONTENT_XPATH = "//div[@id='movie_content']/text()";  //评分
    public final static String EIGHTS_SCREENSHOT_IMG_XPATH = "//div[@class='noborder block1']/img/@src";  //评分
}
